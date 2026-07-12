from pydantic import Field
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    database_url: str = "postgresql://pi:pi@localhost:5432/kineo"
    api_name: str = "Kineo API"
    secret_key: str = Field(default="tu_clave_super_secreta_aqui", env="SECRET_KEY")

    class Config:
        env_file = ".env"

settings = Settings()
