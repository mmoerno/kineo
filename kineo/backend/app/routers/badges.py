from fastapi import APIRouter, Depends
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List
from datetime import datetime
from uuid import UUID

from app.db import get_db
from app.deps import get_current_user
from app.schemas.badges import BadgeDto

router = APIRouter(prefix="/badges", tags=["badges"])

async def check_and_award_badges(db: AsyncSession, user_id: UUID):
    # 1. Obtener totales globales sumando todas las sesiones de cada día
    stats_q = await db.execute(
        text("""
            WITH daily_totals AS (
                SELECT session_date, SUM(steps) as day_steps, SUM(COALESCE(distance_meters, distance, 0)) as day_dist
                FROM step_sessions
                WHERE user_id = :uid
                GROUP BY session_date
            )
            SELECT
                COALESCE(SUM(day_steps), 0) as total_steps,
                COALESCE(SUM(day_dist), 0) as total_dist,
                COALESCE(MAX(day_steps), 0) as max_steps_day
            FROM daily_totals
        """),
        {"uid": user_id}
    )
    stats = stats_q.mappings().first()

    # 2. Racha (Semana Activa: 7 días con suma diaria >= 5000 pasos)
    streak_q = await db.execute(
        text("""
            WITH daily_sum AS (
                SELECT session_date
                FROM step_sessions
                WHERE user_id = :uid
                GROUP BY session_date
                HAVING SUM(steps) >= 5000
            ),
            groups AS (
                SELECT session_date,
                       session_date - (CAST(row_number() OVER (ORDER BY session_date) AS INTEGER) * interval '1 day') as grp
                FROM daily_sum
            )
            SELECT COUNT(*) as streak
            FROM groups
            GROUP BY grp
            ORDER BY streak DESC
            LIMIT 1
        """),
        {"uid": user_id}
    )
    max_streak = streak_q.scalar() or 0

    # 3. Madrugador (Cualquier actividad antes de las 8 AM)
    early_q = await db.execute(
        text("""
            SELECT EXISTS (
                SELECT 1 FROM step_sessions
                WHERE user_id = :uid AND EXTRACT(HOUR FROM created_at) < 8
            ) OR EXISTS (
                SELECT 1 FROM route_points rp
                WHERE EXISTS (
                    SELECT 1 FROM routes r
                    WHERE CAST(r.id AS TEXT) = rp.route_id AND r.user_id = :uid
                )
                AND EXTRACT(HOUR FROM rp."timestamp") < 8
            )
        """),
        {"uid": user_id}
    )
    is_early_bird = early_q.scalar()

    badges = []
    if stats["total_steps"] >= 1000: badges.append('first_1000')
    if stats["max_steps_day"] >= 5000: badges.append('five_k')
    if stats["max_steps_day"] >= 10000: badges.append('ten_k')
    if stats["total_steps"] >= 70000: badges.append('andarin_utrera')
    if stats["total_steps"] >= 100000: badges.append('cachimber_compostela')
    if stats["total_dist"] >= 42195: badges.append('marathon')
    if max_streak >= 7: badges.append('week_streak')
    if is_early_bird: badges.append('early_bird')

    for bid in badges:
        await db.execute(
            text("INSERT INTO user_badges (user_id, badge_id, earned_at) VALUES (:uid, :bid, NOW()) ON CONFLICT DO NOTHING"),
            {"uid": user_id, "bid": bid}
        )

@router.get("/", response_model=List[BadgeDto])
async def get_badges(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    user_id = current_user["id"]
    await check_and_award_badges(db, user_id)
    await db.commit()

    result = await db.execute(
        text("""
            SELECT b.id, b.name, b.description,
                   CASE
                     WHEN b.id = 'first_1000' THEN 'ic_badge_steps'
                     WHEN b.id = 'five_k' THEN 'ic_badge_5k'
                     WHEN b.id = 'ten_k' THEN 'ic_badge_10k'
                     WHEN b.id = 'week_streak' THEN 'ic_badge_streak'
                     WHEN b.id = 'early_bird' THEN 'ic_badge_morning'
                     WHEN b.id = 'marathon' THEN 'ic_badge_marathon'
                     WHEN b.id = 'andarin_utrera' THEN 'ic_badge_marathon'
                     WHEN b.id = 'cachimber_compostela' THEN 'ic_badge_streak'
                     ELSE b.icon_url
                   END as icon_url,
                   CASE WHEN ub.user_id IS NOT NULL THEN true ELSE false END as is_unlocked,
                   ub.earned_at as unlocked_at
            FROM badges b
            LEFT JOIN user_badges ub ON b.id = ub.badge_id AND ub.user_id = :uid
            ORDER BY b.name ASC
        """),
        {"uid": user_id}
    )
    rows = result.mappings().all()
    return [BadgeDto(**row) for row in rows]
