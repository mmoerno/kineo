from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List
from datetime import date
from collections import defaultdict

from app.db import get_db
from app.deps import get_current_user
from app.schemas.steps import StepSessionDto, StepCountsDto
from app.routers.challenges import update_challenge_status
from app.routers.badges import check_and_award_badges

router = APIRouter(prefix="/steps", tags=["steps"])
@router.post("/sync")
async def sync_steps(
    sessions: List[StepSessionDto],
    current_user: dict = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    daily_totals = defaultdict(lambda: {
        "steps": 0,
        "distance": 0.0,
        "calories": 0.0,
        "active_minutes": 0,
    })

    for session in sessions:
        if not session.session_date:
            raise HTTPException(status_code=422, detail="Each step session requires date or session_date")
        bucket = daily_totals[session.session_date]
        bucket["steps"] += session.steps or 0
        bucket["distance"] += session.distance or session.distance_meters or 0.0
        bucket["calories"] += session.calories or 0.0
        bucket["active_minutes"] += session.active_minutes or 0

    for session_date, totals in daily_totals.items():
        result = await db.execute(
            text("""
                UPDATE step_sessions
                SET steps = :steps,
                    distance = :distance,
                    distance_meters = :distance_meters,
                    calories = :calories,
                    active_minutes = :active_minutes,
                    updated_at = NOW()
                WHERE user_id = :user_id AND session_date = :session_date
            """),
            {
                "user_id": current_user["id"],
                "session_date": session_date,
                "steps": totals["steps"],
                "distance": totals["distance"],
                "distance_meters": totals["distance"],
                "calories": totals["calories"],
                "active_minutes": totals["active_minutes"],
            }
        )

        if result.rowcount == 0:
            await db.execute(
                text("""
                    INSERT INTO step_sessions (user_id, session_date, steps, distance, distance_meters, calories, active_minutes)
                    VALUES (:user_id, :session_date, :steps, :distance, :distance_meters, :calories, :active_minutes)
                """),
                {
                    "user_id": current_user["id"],
                    "session_date": session_date,
                    "steps": totals["steps"],
                    "distance": totals["distance"],
                    "distance_meters": totals["distance"],
                    "calories": totals["calories"],
                    "active_minutes": totals["active_minutes"],
                }
            )

    await update_challenge_status(db, current_user["id"])
    await check_and_award_badges(db, current_user["id"])
    await db.commit()
    return {"message": "Steps synced and aggregated successfully"}
@router.get("/today")
async def get_today_steps(
    current_user: dict = Depends(get_current_user), 
    db: AsyncSession = Depends(get_db)
):
    result = await db.execute(
        text("""
            SELECT session_date AS date,
                   SUM(steps) as steps,
                   SUM(COALESCE(distance_meters, distance, 0)) as distance_meters,
                   SUM(calories) as calories
            FROM step_sessions
            WHERE user_id = :user_id AND session_date = CURRENT_DATE
            GROUP BY session_date
        """),
        {"user_id": current_user["id"]}
    )
    row = result.mappings().first()
    if not row:
        return {"date": date.today().isoformat(), "steps": 0, "distance_meters": 0.0, "calories": 0.0}
    return dict(row)

@router.get("/history")
async def get_steps_history(days: int = 30, current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    result = await db.execute(
        text("""
            SELECT session_date AS date,
                   SUM(steps) as steps,
                   SUM(calories) as calories,
                   SUM(COALESCE(distance_meters, distance, 0)) as distance_meters,
                   SUM(active_minutes) as active_minutes
            FROM step_sessions
            WHERE user_id = :user_id
              AND session_date >= CURRENT_DATE - make_interval(days => :days)
            GROUP BY session_date
            ORDER BY session_date ASC
        """),
        {"user_id": current_user["id"], "days": days}
    )
    rows = result.mappings().all()
    return [dict(row) for row in rows]

@router.get("/counts", response_model=StepCountsDto)
async def get_steps_counts(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    result = await db.execute(
        text("""
            SELECT
                COALESCE(SUM(CASE WHEN session_date >= date_trunc('week', CURRENT_DATE) THEN steps END), 0) AS weekly_total,
                COALESCE(SUM(CASE WHEN session_date >= date_trunc('month', CURRENT_DATE) THEN steps END), 0) AS monthly_total
            FROM step_sessions
            WHERE user_id = :user_id
        """),
        {"user_id": current_user["id"]}
    )
    row = result.mappings().first()
    return StepCountsDto(**dict(row))
