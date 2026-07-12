# Kineo

Kineo es una aplicación Android de fitness gamificado: cuenta los pasos del usuario, registra rutas mediante GPS, y añade una capa social con amigos, retos (challenges), insignias (badges) y clasificaciones (leaderboard). El proyecto está compuesto por una app Android nativa y una API REST propia que persiste los datos en PostgreSQL.

## Estructura del repositorio

```
KineoV3/
└── kineo/
    ├── backend/    API REST (FastAPI + PostgreSQL)
    ├── frontend/   App Android (Java, Clean Architecture + MVVM)
    └── kineo_db_final.sql   Dump del esquema de base de datos
```

## Backend

API REST construida con **FastAPI**, SQLAlchemy (async, `asyncpg`) sobre **PostgreSQL**, autenticación con JWT (`PyJWT`) y hashing de contraseñas con `passlib[argon2]`.

### Endpoints principales

| Recurso | Rutas |
|---|---|
| Auth | `POST /register`, `POST /login` |
| Steps | `POST /steps/sync`, `GET /steps/today`, `GET /steps/history`, `GET /steps/counts` |
| Routes | `POST /routes/sync`, `GET /routes/{session_id}` |
| Friends | `GET /friends`, `GET /friends/requests`, `POST /friends/request`, `PUT /friends/{userId}/accept`, `DELETE /friends/{userId}`, `GET /friends/weekly-leaderboard` |
| Challenges | `GET /challenges`, `POST /challenges`, `GET /challenges/my`, `PUT /challenges/{id}/accept` |
| Badges | `GET /badges` |
| Leaderboard | `GET /leaderboard` |
| Health | `GET /health/steps` |

### Puesta en marcha

```bash
cd kineo/backend
python -m venv .venv
.venv\Scripts\activate        # en Windows
pip install -r requirements.txt

cp .env.example .env          # completa DATABASE_URL y SECRET_KEY
uvicorn app.main:app --reload
```

La API queda disponible en `http://localhost:8000`. El esquema de base de datos de referencia está en [`kineo/kineo_db_final.sql`](kineo/kineo_db_final.sql) (tablas: `users`, `step_sessions` particionada por mes, `routes`, `route_points`, `friendships`, `challenges`, `challenge_participants`, `badges`, `user_badges`, `daily_goals`, `leaderboard_cache`).

## Frontend (Android)

App Android nativa (`com.dam.kineo`) escrita en Java siguiendo **Clean Architecture** (data / domain / ui) con **MVVM**, inyección de dependencias con **Hilt**, persistencia local con **Room**, y **Retrofit** para consumir el backend.

Principales módulos: Home (resumen de pasos), Auth (login/registro), Social (amigos y retos), Mapa (rutas con Google Maps y Location Services), y un servicio en segundo plano (`StepCounterService`) para el conteo de pasos.

### Puesta en marcha

1. Abre `kineo/frontend/` en Android Studio.
2. Crea `local.properties` (o edítalo si ya existe) con:
   ```properties
   API_BASE_URL=http://<ip-del-backend>:8000/
   MAPS_API_KEY=<tu-clave-de-Google-Maps>
   ```
3. Sincroniza Gradle y ejecuta la app (`minSdk 24`, `targetSdk 36`, Java 17).

### Stack principal

- AndroidX (AppCompat, Lifecycle, Navigation, ConstraintLayout)
- Hilt (DI), Room (base de datos local), Retrofit + Gson (red)
- MPAndroidChart (gráficas), Google Play Services (Location + Maps)

## Autoría

Proyecto desarrollado por [mmoerno](https://github.com/mmoerno).
