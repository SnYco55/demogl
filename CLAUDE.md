# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

DemoGL is a teaching demo app for managing UMONS organizational data: faculties → departments → services, with members assigned to services (many-to-many) and roles (many-to-many), and members optionally directing a service. It has a public read-only view (filterable list of members, active-today by default) and an admin panel for CRUD on faculties/departments/services/members.

- **Backend**: Java 25 + Spring Boot 4, Gradle, at the repo root (`src/`)
- **Frontend**: Vue 3 + TypeScript + Vue Router + Tailwind CSS 4, in `frontend/`
- **Database**: PostgreSQL (Supabase in practice), accessed via Spring Data JPA
- **API docs**: springdoc-openapi, auto-generated from `@RestController` classes

```
Frontend (Vue.js) --REST--> Backend (Spring Boot) --JPA--> PostgreSQL (Supabase)
```

## Commands

### Backend (run from repo root)

The `.env` file at the repo root (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) must be exported into the shell before any Gradle command that touches the database (run, or tests that hit the real DB):

```bash
export $(cat .env | xargs)
```

```bash
./gradlew bootRun                                          # run the API on http://localhost:8080
./gradlew test                                              # run all tests (uses H2 in-memory DB, see below)
./gradlew test --tests "com.example.demo.FacultyControllerTest"   # run a single test class
./gradlew clean bootJar -x test                              # build the deployable jar (as done in Dockerfile)
```

Tests do **not** need `.env`/Supabase — `src/test/resources/application.yaml` points them at an in-memory H2 database (`ddl-auto: create-drop`), created and dropped per run. `FacultyControllerTest` is the reference pattern for controller tests: `@SpringBootTest` + `@AutoConfigureMockMvc`, repositories autowired directly to set up fixtures, `mockMvc.perform(...)` + `jsonPath` assertions.

### Frontend (run from `frontend/`)

```bash
npm install
npm run dev            # dev server on http://localhost:5173
npm run build           # type-check (vue-tsc) + vite build
npm run type-check
npm run format          # prettier --write
```

Frontend needs `frontend/.env` with `VITE_API_URL=http://localhost:8080` (or the deployed API URL). Never point frontend code at a hardcoded API URL — always import `API_URL` from `frontend/src/config/api.ts`.

### API docs (backend must be running)

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON/YAML: `http://localhost:8080/v3/api-docs` / `/v3/api-docs.yaml`

### Seeding data

With the backend running against an empty database, from `frontend/src/config/`: `node seed.ts`.

## Backend architecture

Strict layering, applied consistently across all four resources (Faculty, Department, Service, Member) plus Role:

```
Entity (JPA, src/main/java/.../entity)
  → Repository (Spring Data JPA, .../repository)
    → Service (business logic + validation, .../service)
      → Mapper (entity -> response DTO, .../mapper/Mapper.java)
        → Response DTO (.../dto/<resource>)
          → Controller (@RestController, .../controller)
```

- **One `Mapper` component** (`mapper/Mapper.java`) handles entity→DTO conversion for all resources — do not create per-resource mappers.
- **DTOs** are per-resource records under `dto/<resource>/`, split into `*CreateRequest`, `*PatchRequest`, and `*Response` (some resources also have a lighter `*ListResponse` vs a fuller `*DetailsResponse`, e.g. member and service).
- **Services** throw `ResponseStatusException` directly for error cases (400 for invalid/duplicate input, 404 for not found, 409 for conflicting deletes, e.g. deleting a faculty that still has departments, or a member who still directs a service). There is no separate exception-handling layer — controllers just propagate what the service throws.
- **Controllers** are thin: they only translate HTTP verbs/paths to service calls and set `@ResponseStatus` (e.g. 201 on create, 204 on delete).
- Entity relationships (see `entity/*.java`): `faculties 1—* departments`, `departments 1—* services`, `services *—* members` (join table `members_to_services`), `members *—* roles` (join table `members_to_roles`), `services *—1 members` (director, a plain `@ManyToOne`).
- `Faculty`/`Department`/`Service` use natural string IDs (set by the client, e.g. `"fs"`, `"informatique"`); `Member`/`Role` use DB-generated `Integer` IDs.
- `config/CorsConfig.java` allowlists frontend origins for cross-origin requests — add any new deployed frontend origin here (currently `http://localhost:5173` and the Vercel prod URL).
- `application.yaml` reads `DB_URL`/`DB_USERNAME`/`DB_PASSWORD` from the environment, uses `ddl-auto: update` (schema evolves via Hibernate, no migration tool), and has `web.error.include-message: always` so validation error messages reach the client instead of being masked by generic 400 bodies.

## Frontend architecture

- Views (`src/views/`) are route-level; `AdminView` hosts nested admin routes (`/admin/faculties`, `/admin/departments`, `/admin/services`, `/admin/members`, see `src/router/index.ts`) each rendered by a `*Management.vue` component in `src/components/`.
- `src/config/api.ts` exports `API_URL` from `VITE_API_URL` — all API calls must go through it, never hardcode `localhost:8080`.
- `src/types/type.ts` holds the shared TypeScript types mirroring backend response DTOs.
- Tailwind is wired via `@tailwindcss/vite` in `vite.config.ts` and imported in `src/assets/main.css`.

## Deployment shape (for context, not usually needed day-to-day)

Two-stage `Dockerfile` (build with `eclipse-temurin:25-jdk` + gradlew, run with `eclipse-temurin:25-jre`) builds the backend only; frontend is deployed separately (Vercel), backend on Render from the same Dockerfile, DB on Supabase. Changing the frontend's deployed origin requires updating `CorsConfig` on the backend.
