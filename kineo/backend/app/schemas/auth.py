from pydantic import BaseModel, ConfigDict, EmailStr, Field
from uuid import UUID
from typing import Optional

class UserCreate(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    username: str
    email: str
    password: str = Field(min_length=1)
    display_name: Optional[str] = Field(default=None, alias="displayName")

class UserLogin(BaseModel):
    email: str
    password: str

class UserOut(BaseModel):
    id: UUID
    username: str
    email: str
    display_name: Optional[str] = None

class AuthResponse(BaseModel):
    message: str
    user: Optional[UserOut] = None
    token: Optional[str] = None
    access_token: Optional[str] = None       # ← NUEVO alias para la app Android
    user_id: Optional[str] = None            # ← NUEVO para la app Android
    username: Optional[str] = None           # ← NUEVO para la app Android
