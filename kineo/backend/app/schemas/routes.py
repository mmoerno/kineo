from pydantic import BaseModel, Field
from typing import List, Optional

class PointDto(BaseModel):
    latitude: float
    longitude: float
    altitude: Optional[float] = 0.0
    timestamp: int

class RouteDto(BaseModel):
    session_id: str = Field(..., alias="sessionId")
    points: List[PointDto]

    class Config:
        allow_population_by_field_name = True
