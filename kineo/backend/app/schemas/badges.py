from pydantic import BaseModel, field_serializer
from datetime import datetime
from typing import Optional

class BadgeDto(BaseModel):
    id: str | None = None
    name: str
    description: str
    icon_url: Optional[str] = None
    is_unlocked: bool = False
    unlocked_at: Optional[datetime] = None
    
    @field_serializer('unlocked_at', when_used='json')
    def serialize_unlocked_at(self, value: Optional[datetime], _info) -> Optional[int]:
        if value is None:
            return None
        return int(value.timestamp() * 1000)
