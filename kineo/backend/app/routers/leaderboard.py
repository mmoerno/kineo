from fastapi import APIRouter, Depends, Query
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List

from app.db import get_db
from app.deps import get_current_user
from app.schemas.leaderboard import LeaderboardEntryDto

router = APIRouter(prefix="/leaderboard", tags=["leaderboard"])

@router.get("/", response_model=List[LeaderboardEntryDto])
async def get_leaderboard(
    period: str = Query("weekly"),
    current_user: dict = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    # Calculamos el ranking en tiempo real basado en el periodo
    interval = "7 days" if period == "weekly" else "30 days"

    query = text(f"""
        SELECT
            u.id as user_id,
            COALESCE(u.display_name, u.username) as name,
            SUM(ss.steps) as total_steps,
            RANK() OVER (ORDER BY SUM(ss.steps) DESC) as rank
        FROM users u
        JOIN step_sessions ss ON u.id = ss.user_id
        WHERE ss.session_date >= CURRENT_DATE - INTERVAL '{interval}'
        GROUP BY u.id
        ORDER BY total_steps DESC
        LIMIT 50
    """)

    result = await db.execute(query)
    rows = result.mappings().all()

    return [
        LeaderboardEntryDto(
            user_id=row["user_id"],
            name=row["name"],
            rank=row["rank"],
            total_steps=row["total_steps"],
            period=period
        ) for row in rows
    ]
