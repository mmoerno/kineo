from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List
from datetime import datetime
from uuid import UUID

from app.db import get_db
from app.deps import get_current_user
from app.schemas.challenges import ChallengeDto

router = APIRouter(prefix="/challenges", tags=["challenges"])

async def update_challenge_status(db: AsyncSession, user_id: UUID):
    """
    Check active challenges for the user and update status if target is reached.
    """
    # 1. Get active challenges where the user is a participant
    result = await db.execute(
        text("""
            SELECT c.id, c.target_steps, c.starts_at, c.ends_at, c.metric
            FROM challenges c
            JOIN challenge_participants cp ON c.id = cp.challenge_id
            WHERE cp.user_id = :uid AND c.status = 'active'
        """),
        {"uid": user_id}
    )
    active_challenges = result.mappings().all()

    for challenge in active_challenges:
        # 2. Calculate progress for this user during the challenge period (summing all sessions)
        column = "COALESCE(distance_meters, distance, 0)" if challenge["metric"] == "distance" else "steps"

        steps_result = await db.execute(
            text(f"""
                SELECT COALESCE(SUM({column}), 0) as total_progress
                FROM step_sessions
                WHERE user_id = :uid
                  AND session_date >= CAST(:s_at AS DATE)
                  AND session_date <= CAST(:e_at AS DATE)
            """),
            {
                "uid": user_id,
                "s_at": challenge["starts_at"],
                "e_at": challenge["ends_at"]
            }
        )
        total_progress = steps_result.scalar() or 0

        # 3. If target reached, mark as completed and set winner
        if total_progress >= challenge["target_steps"]:
            await db.execute(
                text("""
                    UPDATE challenges
                    SET status = 'completed', winner_id = :uid, updated_at = NOW()
                    WHERE id = :cid AND status = 'active'
                """),
                {"cid": challenge["id"], "uid": user_id}
            )

@router.get("/", response_model=List[ChallengeDto])
async def get_challenges(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    await update_challenge_status(db, current_user["id"])
    await db.commit()

    result = await db.execute(
        text("""
            SELECT c.id, c.creator_id, c.challenger_id, c.title, c.description, c.target_steps, c.metric, c.period, c.starts_at, c.ends_at, c.status, c.winner_id, c.created_at, c.updated_at,
                   u.username AS creator_username, u.display_name AS creator_display_name
            FROM challenges c
            JOIN users u ON u.id = c.creator_id
            ORDER BY c.created_at DESC
        """)
    )
    rows = result.mappings().all()
    return [ChallengeDto(**row) for row in rows]

@router.post("/", response_model=ChallengeDto)
async def create_challenge(challenge: ChallengeDto, current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    initial_status = "active" if not challenge.challenger_id else "pending"
    result = await db.execute(
        text("""
            INSERT INTO challenges (creator_id, challenger_id, title, description, target_steps, metric, period, starts_at, ends_at, status)
            VALUES (:creator_id, :challenger_id, :title, :description, :target_steps, :metric, :period, :starts_at, :ends_at, :status)
            RETURNING id, creator_id, challenger_id, title, description, target_steps, metric, period, starts_at, ends_at, status, winner_id, created_at, updated_at
        """),
        {
            "creator_id": current_user["id"],
            "challenger_id": challenge.challenger_id,
            "title": challenge.title,
            "description": challenge.description,
            "target_steps": challenge.target_steps,
            "metric": challenge.metric,
            "period": challenge.period,
            "starts_at": challenge.starts_at,
            "ends_at": challenge.ends_at,
            "status": initial_status
        }
    )
    row = result.mappings().first()
    challenge_id = row["id"]
    await db.execute(
        text("INSERT INTO challenge_participants (challenge_id, user_id) VALUES (:c_id, :u_id)"),
        {"c_id": challenge_id, "u_id": current_user["id"]}
    )
    await db.commit()
    res_dict = dict(row)
    res_dict["creator_username"] = current_user.get("username")
    res_dict["creator_display_name"] = current_user.get("display_name")
    return ChallengeDto(**res_dict)

@router.get("/my", response_model=List[ChallengeDto])
async def get_my_challenges(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    await update_challenge_status(db, current_user["id"])
    await db.commit()
    result = await db.execute(
        text("""
            SELECT c.id, c.creator_id, c.challenger_id, c.title, c.description, c.target_steps, c.metric, c.period, c.starts_at, c.ends_at, c.status, c.winner_id, c.created_at, c.updated_at,
                   u.username AS creator_username, u.display_name AS creator_display_name
            FROM challenges c
            JOIN challenge_participants cp ON c.id = cp.challenge_id
            JOIN users u ON u.id = c.creator_id
            WHERE cp.user_id = :user_id
            ORDER BY c.ends_at ASC
        """),
        {"user_id": current_user["id"]}
    )
    rows = result.mappings().all()
    return [ChallengeDto(**row) for row in rows]

@router.put("/{id}/accept", response_model=ChallengeDto)
async def accept_challenge(id: str, current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    result = await db.execute(text("SELECT * FROM challenges WHERE id = :id"), {"id": id})
    challenge = result.mappings().first()
    if not challenge:
        raise HTTPException(status_code=404, detail="Challenge not found")
    await db.execute(text("UPDATE challenges SET status = 'active' WHERE id = :id AND status = 'pending'"), {"id": id})
    await db.execute(
        text("INSERT INTO challenge_participants (challenge_id, user_id) VALUES (:cid, :uid) ON CONFLICT DO NOTHING"),
        {"cid": id, "uid": current_user["id"]}
    )
    await db.commit()
    result = await db.execute(
        text("""
            SELECT c.*, u.username AS creator_username, u.display_name AS creator_display_name
            FROM challenges c
            JOIN users u ON u.id = c.creator_id
            WHERE c.id = :id
        """),
        {"id": id}
    )
    row = result.mappings().first()
    return ChallengeDto(**dict(row))
