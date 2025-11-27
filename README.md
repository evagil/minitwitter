# MiniTwitter

MiniTwitter es el Trabajo Práctico Individual del Taller de Tecnologías y Producción de Software de la UNRN.  
Implementa una versión simplificada de Twitter con **Spring Boot + PostgreSQL** en el backend y **React + Vite** en el frontend.

## 1. Requisitos funcionales (TP Individual)
Todos los requerimientos detallados en el enunciado oficial `TP Individual - Taller.pdf` están cubiertos:

1. Cada usuario conoce todos sus tweets.
2. Un tweet puede ser retweet de otro y conoce al original.
3. Los retweets no tienen texto adicional.
4. Al eliminar un usuario se eliminan sus tweets (no quedan huérfanos).
5. `userName` es único.
6. `userName` debe tener entre 5 y 25 caracteres.
7. El texto del tweet va de 1 a 280 caracteres.
8. No se permite el retweet propio.
9. Frontend con Header, panel principal de tweets y panel izquierdo con usuarios.
10. Timeline paginado de a 10 tweets (sin retweets) y listado por usuario paginado de a 15.
11. Botón “Mostrar más / No hay más” en los tweets por usuario.
12. Formulario para crear tweets indicando `userId`.
13. Feedback visual ante éxitos/errores.

## 2. Stack tecnológico
- **Backend**: Spring Boot 3, Spring Data JPA, Hibernate, Maven.
- **Base de datos**: PostgreSQL 13 (contenedorizado con Docker) + pgAdmin opcional.
- **Frontend**: React 19, Vite, TailwindCSS + DaisyUI, Axios, React Router DOM.
- **Build tools**: Maven, npm.

## 3. Arquitectura general
```
[React + Vite (http://localhost:3001)]
        ↓ (Axios, rutas relativas /tweets, /usuarios)
[Spring Boot API (http://localhost:8080)]
        ↓ (JPA/Hibernate)
[PostgreSQL (docker-compose, puerto 5432)]
```

El frontend consume la API mediante rutas relativas durante el desarrollo (a través del proxy de Vite) y mediante URL absoluta en producción.

## 4. Base de datos
- Script de creación: `db/minitwitter_schema_postgres.sql`
- Contenedor: `minitwitter-postgres`
- Credenciales por defecto:
  - Host: `localhost`
  - Puerto: `5432`
  - Usuario: `minitwitter`
  - Contraseña: `minitwitter`
  - Base: `minitwitter`

### Tablas principales
| Tabla    | Campos relevantes                          | Descripción                                 |
|----------|--------------------------------------------|---------------------------------------------|
| `users`  | `id`, `username`                           | Usuarios del sistema (restricciones 5 y 6). |
| `tweets` | `id`, `texto`, `created_at`, `autor_id`, `tweet_original_id` | Tweets y retweets (restricciones 1,2,3,7,8). |

> **Nota**: Existe un índice único `uq_users_username_ci` sobre `LOWER(username)` para evitar duplicados ignorando mayúsculas/minúsculas.  
> Si la base ya tiene filas conflictivas (por ejemplo `monica` y `Monica`), elimina o renombra una antes de aplicar el índice o volver a ejecutar el script.

## 5. API del backend
Todos los endpoints responden JSON.

### Usuarios (`/usuarios`)
| Método | Ruta             | Descripción                                    |
|--------|------------------|-----------------------------------------------|
| GET    | `/usuarios`      | Lista todos los usuarios (id + userName).     |
| GET    | `/usuarios/{id}` | Obtiene un usuario por id.                    |
| POST   | `/usuarios`      | Crea un usuario (`userName` como query param).|
| DELETE | `/usuarios/{id}` | Elimina un usuario y sus tweets asociados.    |

### Tweets (`/tweets`)
| Método | Ruta                        | Descripción                                                            |
|--------|-----------------------------|------------------------------------------------------------------------|
| POST   | `/tweets/crear`             | Crea un tweet (`userId`, `texto` como query params).                   |
| POST   | `/tweets/retweet`           | Crea un retweet (`{ "userId": 1, "tweetId": 2 }`).                     |
| GET    | `/tweets/timeline`          | Timeline global sin retweets. Params: `offset`, `limit` (default 10).  |
| GET    | `/tweets/de-usuario/{id}`   | Últimos tweets/retweets de un usuario. Params: `offset`, `limit=15`.   |
| GET    | `/tweets/retweets`          | Lista retweets con detalles del original (uso interno/reportes).       |

## 6. Frontend – funcionalidades clave
- **Header** con nombre del sistema, link a home y botón “Crear Tweet”.
- **Panel izquierdo** con la lista de usuarios. Al clickear, el panel central muestra los últimos 15 tweets de ese usuario con “Mostrar más / No hay más…”.
- **Timeline** (home) muestra tweets (sin retweets) paginados de a 10 con navegación “Anterior / Siguiente”.
- **Formulario Crear Tweet**: aparece en el panel principal al presionar “Crear Tweet”. Incluye selector de usuario, textarea, contador de caracteres, mensajes de éxito/error.
- **Retweets**: selecciona un usuario activo con el botón flotante “Seleccionar Usuario”. Al presionar “Retweet” en una tarjeta:
  - se envía la petición al backend,
  - se recarga el listado,
  - los retweets muestran badge “Retuiteó”, fecha del retweet y contenido del tweet original.
- **Error Boundary y mensajes de error**: ante fallas en API se muestra feedback claro.

## 7. Prerrequisitos
- Java 17+
- Maven 3.8+
- Node.js 18+ y npm
- Docker Desktop + Docker Compose

## 8. Puesta en marcha

### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/evagil/minitwitter.git
cd MiniTwitter
```

### Paso 2: Levantar PostgreSQL con Docker
**Importante**: Asegurate de tener Docker Desktop corriendo antes de continuar.

```bash
# Levantar solo PostgreSQL (recomendado para desarrollo)
docker-compose up -d postgres

# O levantar todo (PostgreSQL + pgAdmin)
docker-compose up -d
```

Espera unos segundos a que el contenedor esté listo. Verificá con:
```bash
docker ps
```
Deberías ver el contenedor `minitwitter-postgres` corriendo.

### Paso 3: Crear el esquema de la base de datos (solo la primera vez)

**Opción A - Usando Docker (recomendado, no requiere psql instalado):**
```bash
# Ejecutar el script directamente desde el contenedor
docker exec -i minitwitter-postgres psql -U minitwitter -d minitwitter < db/minitwitter_schema_postgres.sql
```

**Opción B - Usando psql (si lo tenés instalado):**
```bash
psql -h localhost -U minitwitter -d minitwitter -f db/minitwitter_schema_postgres.sql
```
Cuando te pida la contraseña, ingresá: `minitwitter`

**Opción C - Usando pgAdmin (si levantaste todo con docker-compose):**
1. Abrí `http://localhost:5050` en tu navegador
2. Login con: `admin@local` / `admin`
3. Agregá el servidor PostgreSQL (host: `postgres`, usuario: `minitwitter`, contraseña: `minitwitter`)
4. Ejecutá el script `db/minitwitter_schema_postgres.sql` desde el editor de queries

### Paso 4: Levantar el Backend
Abrí una **nueva terminal** y ejecutá:
```bash
mvn spring-boot:run
```
El backend estará disponible en `http://localhost:8080`

**Verificación**: Podés probar que funciona visitando `http://localhost:8080/swagger-ui.html` (documentación de la API)

### Paso 5: Levantar el Frontend
Abrí **otra terminal** y ejecutá:
```bash
cd frontend
npm install        # Solo la primera vez (instala dependencias)
npm run dev       # Inicia el servidor de desarrollo
```
El frontend estará disponible en `http://localhost:3001`

### Verificación final
1. ✅ Docker: `docker ps` muestra `minitwitter-postgres` corriendo
2. ✅ Backend: `http://localhost:8080/swagger-ui.html` se abre correctamente
3. ✅ Frontend: `http://localhost:3001` muestra la aplicación

Si algo no funciona, revisá la sección de **Troubleshooting** más abajo.

## 9. Uso básico
1. Asegurate de que PostgreSQL y el backend estén corriendo.
2. Abre `http://localhost:3001`.
3. En el panel izquierdo verás los usuarios; el timeline central muestra los tweets globales.
4. Para crear tweets:
   - click en “Crear Tweet” → completa el formulario → “Crear Tweet”.
5. Para retwittear:
   - botón flotante “Seleccionar Usuario” → elegí quién retwittea.
   - click en “Retweet” dentro de cualquier tarjeta (no debe ser del mismo usuario).
6. Para volver al timeline o navegar entre paneles usa los botones provistos (“Volver al Timeline”, paginación, “Mostrar más”).

## 10. Scripts útiles
Backend:
```bash
mvn clean test        # corre tests y genera informes
mvn spring-boot:run   # levanta API
```

Frontend:
```bash
cd frontend
npm run dev           # modo desarrollo (puerto 3001)
npm run build         # build de producción (dist/)
npm run preview       # sirve el build para verificación
```

## 11. Troubleshooting (Solución de problemas)

### Error: "Cannot connect to Docker daemon"
- **Solución**: Asegurate de que Docker Desktop esté corriendo. En Windows/Mac, abrí Docker Desktop y esperá a que esté completamente iniciado.

### Error: "Port 5432 is already in use"
- **Solución**: Alguien más está usando PostgreSQL en tu máquina. Podés:
  - Detener el servicio: `docker-compose down`
  - O cambiar el puerto en `docker-compose.yml` (línea 30) y actualizar `application.properties`

### Error al ejecutar el script SQL: "permission denied" o "file not found"
- **Solución**: Verificá que el archivo `db/minitwitter_schema_postgres.sql` existe y que el contenedor está corriendo:
  ```bash
  docker ps
  docker exec -it minitwitter-postgres psql -U minitwitter -d minitwitter
  ```
  Si entrás a psql, ejecutá manualmente el contenido del script.

### Backend no se conecta a la base de datos
- **Verificá**:
  1. PostgreSQL está corriendo: `docker ps`
  2. Las credenciales en `src/main/resources/application.properties` coinciden con `docker-compose.yml`
  3. El esquema fue creado correctamente (Paso 3)

### Frontend no se conecta al backend
- **Verificá**:
  1. El backend está corriendo en `http://localhost:8080`
  2. No hay errores de CORS (el `CorsConfig.java` debería estar configurado)
  3. Abrí la consola del navegador (F12) para ver errores específicos

### "npm: command not found"
- **Solución**: Instalá Node.js desde [nodejs.org](https://nodejs.org/). Verificá con `node --version` y `npm --version`.

### "mvn: command not found"
- **Solución**: Instalá Maven o usá el wrapper incluido: `./mvnw spring-boot:run` (Linux/Mac) o `.\mvnw.cmd spring-boot:run` (Windows)

## 12. Notas finales
- Si cambias puertos o credenciales del contenedor, actualiza `src/main/resources/application.properties`.
- Para detener los servicios:
  - Backend: `Ctrl+C` en la terminal.
  - Frontend: `Ctrl+C` en la terminal.
  - Docker: `docker-compose down` (detiene y elimina contenedores) o `docker-compose stop` (solo detiene).
- El schema de referencia está optimizado para PostgreSQL; no hay scripts activos para otros motores.
- Para limpiar completamente (incluyendo volúmenes de datos): `docker-compose down -v`

¡Listo! Con estos pasos podés levantar, operar y extender MiniTwitter tanto en backend como en frontend.
