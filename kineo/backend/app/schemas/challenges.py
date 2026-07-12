from pydantic import BaseModel, field_serializer
from uuid import UUID
from datetime import datetime
from typing import Optional

class ChallengeDto(BaseModel):
    id: Optional[UUID] = None
    creator_id: UUID
    challenger_id: Optional[UUID] = None
    title: str
    description: Optional[str] = None
    target_steps: int
    metric: str = "steps"
    period: str = "daily"
    starts_at: datetime
    ends_at: datetime
    status: str = "active"
    winner_id: Optional[UUID] = None
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None
    creator_username: Optional[str] = None
    creator_display_name: Optional[str] = None

    class Config:
        from_attributes = True
    
    @field_serializer('starts_at', 'ends_at', 'created_at', 'updated_at', when_used='json')
    def serialize_datetime(self, value: Optional[datetime], _info) -> Optional[int]:
        if value is None:
            return None
        return int(value.timestamp() * 1000)  # Convert to milliseconds
