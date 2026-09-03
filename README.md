# clias-backend

API REST del sistema **CLIAS** (telemedicina — automuestreo de VPH, Universidad de Cuenca).
Da servicio a `clias-app` (paciente), `clias-admin` (panel web) y, opcionalmente, `clias-web`.

## Stack

| | |
|---|---|
| Lenguaje | Kotlin (Java 21) |
| Framework | Spring Boot 3.4.2 |
| Persistencia | Spring Data JPA + Hibernate · PostgreSQL 17 · `ddl-auto=update` |
| Seguridad | Spring Security + JWT (roles `ADMIN` / `USER`) |
| Push | Firebase Admin SDK (FCM) |
| Interoperabilidad | HAPI FHIR R4 (endpoints de solo lectura) |
| Build | Maven Wrapper (`mvnw`) |
| Puerto | `9001` |

## Requisitos

- JDK 21
- PostgreSQL 17 accesible (por defecto `localhost:5433`)
- Credenciales de Firebase (service account JSON) para el envío de notificaciones

## Configuración

El archivo real `src/main/resources/application.properties` **no está versionado**.
Crea el tuyo a partir de la plantilla:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
# edita la URL/usuario/contraseña de la base de datos
```

Coloca el JSON del *service account* de Firebase en `src/main/resources/`
(lo carga `configuration/FirebaseConfig.kt`); tampoco se versiona.

## Ejecutar

```bash
./mvnw spring-boot:run          # arranca en http://localhost:9001
./mvnw -q compile               # solo compilar
./mvnw -q test                  # tests (incl. carga de contexto)
```

Docker:

```bash
docker compose up -d            # backend + PostgreSQL
```

## Estructura

```
src/main/kotlin/com/crisordonez/registro/
  controller/     Endpoints REST (+ FHIR)
  service/        Lógica de negocio (interfaz + implementación por dominio)
  repository/     Spring Data repositories
  model/
    entities/     @Entity (JPA)
    requests/     DTOs de entrada
    responses/    DTOs de salida
    mapper/       entity ↔ DTO
    enums/
  configuration/  Seguridad, JWT, CORS, Firebase, seeders de catálogos
src/main/resources/
  application.properties.example
  static/         (clias-admin compilado, si se sirve embebido)
storage/recursos/ Binarios subidos por el admin (NO versionado)
```

## Migración a producción

`ddl-auto=update` crea tablas y columnas nuevas al desplegar, pero **no** hace
backfills, borrado de constraints ni columnas generadas. El script consolidado e
idempotente está en **clias-docs** → `dev/backend/migracion-metricas.md` (§ 0).

## Relación con el resto de CLIAS

```
clias-app      consume esta API (JWT de paciente)
clias-admin    consume esta API (JWT de admin)
clias-chatbot  el backend registra sus métricas de uso
clias-docs     documenta endpoints, modelo de datos y migraciones
```
