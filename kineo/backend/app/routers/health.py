from fastapi import APIRouter, Depends
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from app.db import get_db

router = APIRouter(prefix="/health", tags=["health"])

@router.get("/steps")
async def debug_steps(db: AsyncSession = Depends(get_db)):
    result = await db.execute(text("SELECT * FROM step_sessions LIMIT 5"))
    rows = result.mappings().all()
    return {"rows": [dict(r) for r in rows]}
