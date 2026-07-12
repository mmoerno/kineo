from pydantic import BaseModel
from uuid import UUID
from datetime import datetime
from typing import Optional

class FriendRequestCreate(BaseModel):
    username: str

# Este es el que usa el router para devolver la información de la amistad
class FriendDto(BaseModel):
    id: Optional[UUID] = None
    requester_id: UUID
    addressee_id: UUID
    status: str
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None
    username: Optional[str] = None
    name: Optional[str] = None
    email: Optional[str] = None
    avatar_url: Optional[str] = None
    today_steps: Optional[int] = 0

    class Config:
        from_attributes = True


