from pydantic import BaseModel
from uuid import UUID

class LeaderboardEntryDto(BaseModel):
    user_id: UUID
    name: str | None = None
    rank: int
    total_steps: int
    period: str  # weekly, monthly, etc.