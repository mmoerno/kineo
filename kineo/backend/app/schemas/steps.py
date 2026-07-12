from pydantic import BaseModel, model_validator
from datetime import date
from typing import Optional

class StepCountsDto(BaseModel):
    weekly_total: int
    monthly_total: int

class StepSessionDto(BaseModel):
    id: Optional[str] = None
    user_id: Optional[str] = None  # Opcional, lo sacamos del token
    session_date: Optional[date] = None # Opcional inicialmente
    date: Optional[str] = None      # El campo que envía el móvil
    steps: int
    distance: Optional[float] = 0.0
    distance_meters: Optional[float] = None
    calories: Optional[float] = 0.0
    active_minutes: Optional[int] = 0

    @model_validator(mode='before')
    @classmethod
    def fix_incoming_data(cls, data: dict):
        # 1. Si viene "date" pero no "session_date", lo movemos
        if "date" in data and not data.get("session_date"):
            data["session_date"] = data["date"]
        
        # 2. Si viene "distance_meters" pero no "distance", lo movemos
        if "distance_meters" in data and not data.get("distance"):
            data["distance"] = data["distance_meters"]
            
        return data
