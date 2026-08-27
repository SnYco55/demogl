# DemoGL

This repository serves as a teaching example, used at by the Software Engineering Lab of UMONS in the context of some courses and projects. 
The project is intentionally minimalistic and incomplete, as it only serves as a proof-of-concept demonstrator of how to use and combine different software development technologies.
It demonstrates how to build a three-tiered software architecture of a client-server web application, with a backend containing the business logic (in Java) that connects to a relational SQL database, and a frontend containing the web interface that interacts with the backend through a REST API. The purpose of the web application itself is to show and edit the organisational structure of the university (composed of faculties, departments, services, and members). It comes with a read-only user interface, and a read-write administration interface.

# Launch guide

[Lire en français](README_FR.md)

[Full Guide](docs/GUIDE.md)

This guide walks you through getting the project and running it locally, from the database up to the frontend.

## Architecture

- **Frontend**: Vue.js + TypeScript + Tailwind CSS
- **Backend**: Java 25 + Spring Boot + Gradle
- **API**: REST + OpenAPI (Swagger)
- **Database**: PostgreSQL (Supabase)

```
Frontend (Vue.js)
       │  REST
       ▼
Backend (Spring Boot)
       │  JPA / JDBC
       ▼
PostgreSQL (Supabase)
```

---

## 1. Prerequisites

To install before starting:

| Tool | Purpose |
| --- | --- |
| Java 25 | Build/run the backend |
| Node.js + npm | Frontend |
| Git | Clone the project |

The **Gradle Wrapper** is included in the project: no need to install Gradle separately.

---

## 2. Clone the project

```bash
git clone https://github.com/sgl-umons/demogl.git
cd demogl
```

Project structure:

```
demogl/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── .env                  ← backend config
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
└── frontend/
    ├── .env               ← frontend config
    ├── src/
    ├── package.json
    └── vite.config.ts
```

---

## 3. Set up the database (Supabase)

(or any other JDBC-compatible PostgreSQL client)

1. Go to [supabase.com](https://supabase.com) and create an account / sign in (with GitHub or otherwise).
2. Create a new project.
3. **Choose a database password when creating it - keep it to connect to the DB afterwards.**
4. Once the project is created, go to **Connect** to retrieve the connection details.
5. Choose **Direct Connection string**
6. Choose **Session pooler** mode.
7. Choose Type **JDBC**.


Keep these four pieces of information handy for the next step:

- host
- username
- port
- password (the password chosen in step 3)

---

## 4. Configure the backend

### 4.1 Create the `.env` file

At the **root of the project** (where `build.gradle` is located), create a `.env` file:

```
DB_URL='jdbc:postgresql://[HOST]:[PORT]/postgres'
DB_USERNAME='[USERNAME]'
DB_PASSWORD='[PASSWORD]'
```
**(postgres is the default database name in Supabase)**

here is an example of a `.env` file:
    
```
DB_URL='jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres'
DB_USERNAME='postgres.tttrpqqpsixghjtrkenj'
DB_PASSWORD='mysecretpassword'
```

---

## 5. Run the backend

### Option A - Gradle with gradlew

The `.env` file must be loaded into the environment **before** running Gradle, from the project root. The method depends on the tool you're using.

**Linux / macOS**, in a terminal at the project root:

```bash
export $(cat .env | xargs)
```

or alternatively:

```bash
export DB_URL=url... (one variable at a time)
```

**Windows (PowerShell)**, one variable at a time:

```powershell
$env:DB_URL="..."
$env:DB_USERNAME="..."
$env:DB_PASSWORD="..."
```

Then run the API:

```bash
./gradlew bootRun       # Linux / macOS
.\gradlew bootRun       # Windows
```

To check that the variables are correctly loaded:

```bash
export -p                 # Linux / macOS
Get-ChildItem env:        # Windows PowerShell
```

### Option B - In IntelliJ IDEA

IntelliJ does not automatically load `.env` files. Install the **EnvFile** plugin (by Borys Pierov):

1. `Settings → Plugins` → search for **EnvFile** → install and restart IntelliJ.
2. Open the Spring Boot run configuration (`Run → Edit Configurations`, or see screenshot).
    
    ![image1](docs/images/image1.png)
    
3. To enable the **EnvFile** tab, check "Enable EnvFile", then add the backend's `.env`.
    
    ![image2](docs/images/image2.png)
    

Once configured, IntelliJ automatically injects the variables on every run - no need to export them manually anymore. (this setting persists)

### Result

The API is available at:

```
http://localhost:8080
```
## 5.1 Fill the database
Once your API is running, in another terminal, you can use the script `seed.ts` located in `frontend/src/config/`
(The API must be running and the database must be empty)

Run the script :
```bash
node seed.ts
```

---

## 6. API documentation

Automatically generated from the `@RestController` classes (via the `springdoc-openapi-starter-webmvc-ui` dependency):

| Format | URL |
| --- | --- |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8080/v3/api-docs.yaml` |

To test endpoints directly without an external tool (Postman/Insomnia), IntelliJ Ultimate offers a native **Endpoints** tab (`View → Tool Windows → Endpoints`) - make sure the **Spring** and **Spring Web** plugins are enabled, otherwise the endpoints won't show up there.

---

## 7. Configure and run the frontend

From the `frontend` folder:

```bash
npm install
```

Create a `frontend/.env` file:

```
VITE_API_URL=http://localhost:8080
```

Vite automatically injects variables prefixed with `VITE_`, so no additional configuration is needed.

Start the development server (from the `frontend` folder):

```bash
npm run dev
```

The frontend is available at:

```
http://localhost:5173
```

---

## 8. Full startup - summarytttrpqqpsix

Once both `.env` files are configured (backend and frontend):

```bash
# Terminal 1 - Backend (from the project root)
export $(cat .env | xargs)   # or via IntelliJ EnvFile
./gradlew bootRun

# Terminal 2 - Frontend
cd frontend
npm install
npm run dev
```

Then open:

```
http://localhost:5173 (and API at http://localhost:8080)
```

---

## 9. Troubleshooting

| Problem | Likely cause | Solution                                      |
| --- | --- |-----------------------------------------------|
| `.env` not picked up in IntelliJ | No automatic loading | Install the **EnvFile** plugin and enable it in the run configuration |
| Cannot connect to the database via Direct connection | IPv6/IPv4 network incompatibility | Use Supabase's **Session Pooler** instead     |
| `.env` variables not active in a new terminal | Exports don't persist across sessions | export $(cat .env \| xargs)                   |
| Endpoints missing from the Endpoints tab (IntelliJ) | Spring/Web plugins disabled | Check they're enabled in `Settings → Plugins` |

---

## 10. Environment variables - summary

**Backend** (`.env`, at the project root):

```
DB_URL=jdbc:postgresql://[HOST]:[PORT]/postgres
DB_USERNAME=[USERNAME]
DB_PASSWORD=[PASSWORD]
```
**(postgres is the default database name in Supabase)**

**Frontend** (`frontend/.env`):

```
VITE_API_URL=http://localhost:8080
```

In production, `VITE_API_URL` must point to the deployed backend's public URL, and you'll also need to add the deployed frontend's address to the backend's `config/CorsConfig`.

**Never commit `.env` files** - make sure they're listed in `.gitignore`.
