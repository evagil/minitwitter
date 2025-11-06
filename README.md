# MiniTwitter

Aplicación Spring Boot con PostgreSQL en Docker.

## Requisitos
- Java 17
- Maven 3.8+
- Docker y Docker Compose

## Servicios (Docker)
Levantar base de datos y pgAdmin:

```bash
docker compose up -d
```

- PostgreSQL: `localhost:5432`  (user: `minitwitter`, pass: `minitwitter`, db: `minitwitter`)
- pgAdmin: `http://localhost:5050` (usuario: `admin@local`, contraseña: `admin`)

## Crear esquema en PostgreSQL

**Opción 1: PowerShell (recomendado en Windows)**
```powershell
Get-Content .\db\minitwitter_schema_postgres.sql | docker exec -i minitwitter-postgres psql -U minitwitter -d minitwitter
```

**Opción 2: pgAdmin**
- Abre `http://localhost:5050`
- Conecta a PostgreSQL (host: `localhost`, puerto: `5432`, usuario: `minitwitter`, contraseña: `minitwitter`)
- Abre Query Tool y ejecuta el contenido de `db/minitwitter_schema_postgres.sql`

## Configuración de la app
La aplicación ya está configurada para PostgreSQL en:
- `src/main/resources/application.properties`
- `src/main/resources/application.yml`

Valores por defecto:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/minitwitter
spring.datasource.username=minitwitter
spring.datasource.password=minitwitter
```

## Ejecutar la app
```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Swagger UI

La documentación interactiva de la API está disponible en:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs (JSON)**: `http://localhost:8080/api-docs`

Aquí puedes ver todos los endpoints disponibles, probarlos directamente y ver la documentación completa de la API.

## Endpoints disponibles

### Usuarios
- `GET /api/users` - Lista todos los usuarios
- `GET /api/users/{id}` - Obtiene un usuario por ID
- `POST /api/users` - Crea un usuario (body: `{"userName": "nombre"}`)

### Tweets
- `GET /api/tweets?page=0&size=10` - Lista tweets con paginación
- `GET /api/tweets/user/{userId}?offset=0&limit=15` - Tweets de un usuario
- `POST /api/tweets` - Crea un tweet (body: `{"userId": 1, "texto": "mi tweet"}`)
- `POST /api/tweets/{tweetId}/retweet` - Hace retweet de un tweet

## Notas
- Schema de MySQL removido. El schema activo es `db/minitwitter_schema_postgres.sql`.
- Si se cambia puertos/credenciales del contenedor, actualiza `application.properties`/`application.yml` en consecuencia.
- Para detener la aplicación: `Ctrl+C` en la terminal o `taskkill /F /IM java.exe` en PowerShell.

