from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
import jwt
from datetime import datetime, timedelta
from app.core.config import settings

async def get_user_by_email(db, email: str):
    result = await db.execute(
        text("""
            SELECT id, username, email, password_hash, display_name
            FROM users
            WHERE email = :email
            LIMIT 1
        """),
        {"email": email},
    )
    return result.mappings().first()

async def create_user(db, username, email, password_hash, display_name=None):
    result = await db.execute(
        text("""
            INSERT INTO users (username, email, password_hash, display_name)
            VALUES (:username, :email, :password_hash, :display_name)
            RETURNING id, username, email, display_name
        """),
        {
            "username": username,
            "email": email,
            "password_hash": password_hash,
            "display_name": display_name,
        },
    )
    await db.commit()
    return result.mappings().first()

def create_access_token(data: dict, expires_delta: timedelta = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(days=30)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, settings.secret_key, algorithm="HS256")
    return encoded_jwt
   
