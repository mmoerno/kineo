from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.exceptions import RequestValidationError
from fastapi import Request
from sqlalchemy import text
from app.db import engine
from app.routers.health import router as health_router
from app.routers.steps import router as steps_router
from app.routers.friends import router as friends_router
from app.routers.challenges import router as challenges_router
from app.routers.leaderboard import router as leaderboard_router
from app.routers.auth import router as auth_router
from app.routers.badges import router as badges_router
from app.routers.routes import router as routes_router
from fastapi.responses import JSONResponse

app = FastAPI(
    title="Kineo API",
    version="0.1.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(health_router)
app.include_router(steps_router)
app.include_router(friends_router)
app.include_router(challenges_router)
app.include_router(leaderboard_router)
app.include_router(routes_router)
app.include_router(auth_router)
app.include_router(badges_router)

@app.on_event("startup")
async def ensure_db_compatibility():
    try:
        async with engine.begin() as conn:
            # 1. Limpiar restricciones de rutas
            await conn.execute(text("ALTER TABLE route_points DROP CONSTRAINT IF EXISTS route_points_route_id_fkey CASCADE"))
            await conn.execute(text("ALTER TABLE route_points ALTER COLUMN route_id TYPE text USING route_id::text"))

            # 2. Permitir múltiples sesiones por día (Eliminar restricción UNIQUE de pasos si existe)
            print("DB Migration: route point types fixed")
    except Exception as exc:
        print("Warning: DB migration skipped or failed:", exc)

@app.get("/")
async def root():
    return {"message": "Kineo API running"}

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError):
    return JSONResponse(status_code=422, content={"detail": exc.errors()})
