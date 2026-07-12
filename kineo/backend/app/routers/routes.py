from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import text
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List

from app.db import get_db
from app.deps import get_current_user
from app.schemas.routes import RouteDto, PointDto

router = APIRouter(prefix="/routes", tags=["routes"])

@router.post("/sync")
async def sync_route_points(
    route: RouteDto,
    current_user: dict = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    if not route.session_id:
        raise HTTPException(status_code=400, detail="sessionId is required")

    # Usamos cast explícito a TEXT para evitar que asyncpg intente validar como UUID
    await db.execute(
        text("DELETE FROM route_points WHERE route_id = CAST(:route_id AS text)"),
        {"route_id": str(route.session_id)}
    )

    if route.points:
        for point in route.points:
            await db.execute(
                text("""
                    INSERT INTO route_points (route_id, latitude, longitude, altitude, "timestamp")
                    VALUES (CAST(:route_id AS text), :latitude, :longitude, :altitude, to_timestamp(:timestamp / 1000.0))
                """),
                {
                    "route_id": str(route.session_id),
                    "latitude": point.latitude,
                    "longitude": point.longitude,
                    "altitude": point.altitude,
                    "timestamp": point.timestamp,
                }
            )

    await db.commit()
    return {"message": "Route points synced successfully"}

@router.get("/{session_id}", response_model=List[PointDto])
async def get_route_points(
    session_id: str,
    current_user: dict = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    result = await db.execute(
        text("""
            SELECT latitude, longitude, altitude,
                   CAST(EXTRACT(EPOCH FROM "timestamp") * 1000 AS bigint) AS timestamp
            FROM route_points
            WHERE route_id = CAST(:route_id AS text)
            ORDER BY "timestamp" ASC
        """),
        {"route_id": session_id}
    )
    rows = result.mappings().all()
    return [PointDto(**{**row}) for row in rows]
