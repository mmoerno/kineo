from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List
from app.schemas.friends import FriendDto, FriendRequestCreate
from app.db import get_db
from app.deps import get_current_user

router = APIRouter(prefix="/friends", tags=["friends"])

@router.get("/", response_model=List[FriendDto])
async def get_friends(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    result = await db.execute(
        text("""
            SELECT f.id,
                   f.requester_id,
                   f.addressee_id,
                   f.status,
                   f.created_at,
                   f.updated_at,
                   COALESCE(u.display_name, u.username) AS name,
                   u.username AS username,
                   u.email,
                   u.avatar_url AS avatar_url,
                   COALESCE(SUM(ss.steps), 0) AS today_steps
            FROM friendships f
            JOIN users u ON u.id = CASE WHEN f.requester_id = :user_id THEN f.addressee_id ELSE f.requester_id END
            LEFT JOIN step_sessions ss ON ss.user_id = u.id AND ss.session_date = CURRENT_DATE
            WHERE (f.requester_id = :user_id OR f.addressee_id = :user_id)
              AND f.status = 'accepted'
            GROUP BY f.id, f.requester_id, f.addressee_id, f.status, f.created_at, f.updated_at,
                     u.display_name, u.username, u.email, u.avatar_url
        """),
        {"user_id": current_user["id"]}
    )
    rows = result.mappings().all()
    return [FriendDto(**row) for row in rows]

@router.get("/requests", response_model=List[FriendDto])
async def get_friend_requests(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    result = await db.execute(
        text("""
            SELECT f.id,
                   f.requester_id,
                   f.addressee_id,
                   f.status,
                   f.created_at,
                   f.updated_at,
                   COALESCE(u.display_name, u.username) AS name,
                   u.username AS username,
                   u.email,
                   u.avatar_url AS avatar_url,
                   COALESCE(SUM(ss.steps), 0) AS today_steps
            FROM friendships f
            JOIN users u ON u.id = f.requester_id
            LEFT JOIN step_sessions ss ON ss.user_id = u.id AND ss.session_date = CURRENT_DATE
            WHERE f.addressee_id = :user_id
              AND f.status = 'pending'
            GROUP BY f.id, f.requester_id, f.addressee_id, f.status, f.created_at, f.updated_at,
                     u.display_name, u.username, u.email, u.avatar_url
        """),
        {"user_id": current_user["id"]}
    )
    rows = result.mappings().all()
    return [FriendDto(**row) for row in rows]

@router.post("/request", response_model=FriendDto)
async def send_friend_request(
    payload: FriendRequestCreate,
    current_user: dict = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    target_user_query = await db.execute(
        text("SELECT id, username, display_name, email, avatar_url FROM users WHERE username = :username"),
        {"username": payload.username}
    )
    target_user = target_user_query.mappings().first()

    if not target_user:
        raise HTTPException(status_code=404, detail=f"El usuario con username '{payload.username}' no existe")

    friend_id = target_user["id"]

    if str(friend_id) == str(current_user["id"]):
        raise HTTPException(status_code=400, detail="No puedes enviarte una solicitud a ti mismo")

    check_query = await db.execute(
        text("""
            SELECT status FROM friendships
            WHERE (requester_id = :user_id AND addressee_id = :friend_id)
               OR (requester_id = :friend_id AND addressee_id = :user_id)
        """),
        {"user_id": current_user["id"], "friend_id": friend_id}
    )

    if check_query.mappings().first():
        raise HTTPException(status_code=400, detail="Ya existe una solicitud o amistad activa")

    result = await db.execute(
        text("""
            INSERT INTO friendships (requester_id, addressee_id, status)
            VALUES (:requester_id, :addressee_id, 'pending')
            RETURNING id, requester_id, addressee_id, status, created_at, updated_at
        """),
        {"requester_id": current_user["id"], "addressee_id": friend_id}
    )

    await db.commit()
    row = result.mappings().first()
    
    # SOLUCIÓN: Convertimos a dict mutable antes de asignar
    row_dict = dict(row)
    row_dict["username"] = target_user.get("username")
    row_dict["name"] = target_user.get("display_name") or target_user.get("username")
    row_dict["email"] = target_user.get("email")
    row_dict["avatar_url"] = target_user.get("avatar_url")
    row_dict["today_steps"] = 0
    
    return FriendDto(**row_dict)

@router.put("/{userId}/accept", response_model=FriendDto)
async def accept_friend_request(
    userId: str, 
    current_user: dict = Depends(get_current_user), 
    db: AsyncSession = Depends(get_db)
):
    result = await db.execute(
        text("""
            UPDATE friendships
            SET status = 'accepted', updated_at = NOW()
            WHERE requester_id = :requester_id AND addressee_id = :addressee_id AND status = 'pending'
            RETURNING id, requester_id, addressee_id, status, created_at, updated_at
        """),
        {"requester_id": userId, "addressee_id": current_user["id"]}
    )
    row = result.mappings().first()
    if not row:
        raise HTTPException(status_code=404, detail="Friend request not found")

    friend_user_query = await db.execute(
        text("SELECT username, display_name, email, avatar_url FROM users WHERE id = :id"),
        {"id": userId}
    )
    friend_user = friend_user_query.mappings().first()
    
    # SOLUCIÓN: Convertimos a dict mutable antes de asignar
    row_dict = dict(row)
    if friend_user:
        row_dict["username"] = friend_user.get("username")
        row_dict["name"] = friend_user.get("display_name") or friend_user.get("username")
        row_dict["email"] = friend_user.get("email")
        row_dict["avatar_url"] = friend_user.get("avatar_url")
    row_dict["today_steps"] = 0

    await db.commit()
    return FriendDto(**row_dict)

@router.delete("/{userId}")
async def remove_friend(userId: str, current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    result = await db.execute(
        text("""
            DELETE FROM friendships
            WHERE (requester_id = :user_id AND addressee_id = :friend_id) 
               OR (requester_id = :friend_id AND addressee_id = :user_id)
        """),
        {"user_id": current_user["id"], "friend_id": userId}
    )
    if result.rowcount == 0:
        raise HTTPException(status_code=404, detail="Friendship not found")
    await db.commit()
    return {"message": "Friend removed"}

@router.get("/weekly-leaderboard")
async def get_friends_weekly_leaderboard(current_user: dict = Depends(get_current_user), db: AsyncSession = Depends(get_db)):
    # Compute weekly leaderboard for the current user's friends (last week starting Monday)
    result = await db.execute(
        text("""
            SELECT u.id AS user_id,
                   COALESCE(u.display_name, u.username) AS name,
                   u.username,
                   COALESCE(SUM(ss.steps), 0) AS total_steps
            FROM friendships f
            JOIN users u ON u.id = CASE WHEN f.requester_id = :user_id THEN f.addressee_id ELSE f.requester_id END
            LEFT JOIN step_sessions ss ON ss.user_id = u.id AND ss.session_date >= date_trunc('week', CURRENT_DATE)::date
            WHERE (f.requester_id = :user_id OR f.addressee_id = :user_id)
              AND f.status = 'accepted'
            GROUP BY u.id, u.display_name, u.username
            ORDER BY total_steps DESC
        """),
        {"user_id": current_user["id"]}
    )
    rows = result.mappings().all()
    # add rank
    items = [dict(r) for r in rows]
    for idx, item in enumerate(items, start=1):
        item["rank"] = idx
    return {"items": items}
