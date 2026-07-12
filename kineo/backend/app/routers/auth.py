from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from passlib.context import CryptContext

from app.db import get_db
from app.schemas.auth import UserCreate, UserLogin, AuthResponse
from app.services.auth import get_user_by_email, create_user, create_access_token

router = APIRouter(prefix="/auth", tags=["auth"])
pwd_context = CryptContext(schemes=["argon2", "bcrypt"], deprecated="auto")
@router.post("/register", response_model=AuthResponse, status_code=status.HTTP_201_CREATED)
async def register(payload: UserCreate, db: AsyncSession = Depends(get_db)):
    existing = await get_user_by_email(db, payload.email)
    if existing:
        raise HTTPException(status_code=409, detail="El usuario ya existe")

    password_hash = pwd_context.hash(payload.password)
    user = await create_user(db, payload.username, payload.email, password_hash, payload.display_name)
    token = create_access_token(data={"sub": str(user["id"])})

    return {
        "message": "Usuario creado",
        "user": user,
        "token": token,
        "access_token": token,
        "user_id": str(user["id"]),
        "username": user["username"],
        "display_name": user.get("display_name") if isinstance(user, dict) else None,
    }

@router.post("/login", response_model=AuthResponse)
async def login(payload: UserLogin, db: AsyncSession = Depends(get_db)):
    user = await get_user_by_email(db, payload.email)
    if not user:
        raise HTTPException(status_code=401, detail="Credenciales inválidas")

    if not pwd_context.verify(payload.password, user["password_hash"]):
        raise HTTPException(status_code=401, detail="Credenciales inválidas")

    token = create_access_token(data={"sub": str(user["id"])})

    return {
        "message": "Login correcto",
        "user": {
            "id": user["id"],
            "username": user["username"],
            "email": user["email"],
            "display_name": user.get("display_name"),
        },
        "token": token,
        "access_token": token,
        "user_id": str(user["id"]),
        "username": user["username"],
    }
