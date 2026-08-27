# Launch guide

*If you should encounter any ambiguities, errors or incompleteness in this guide, open a ticket (Issue) in this repository to notify us.*

[Read the French version of this guide here](/docs/LAUNCH_FR.md)

The current guide explains how to install and run the project locally, from the database through the backend up to the frontend.

---

## Prerequisites

Make sure you have the following tools installed on your machine before starting:

| Tool | Purpose |
| --- | --- |
| Java 25 LTS | To compile and run the backend |
| Node.js + npm | To run the frontend |

The **Gradle Wrapper** is included in the project: there is no need to install Gradle separately.

---

## Download the project

Locate the archive in the latest Release of this GitHub repository.

Download it in a local directory folder named "demogl" on your machine.

<!--
```bash
git clone https://github.com/sgl-umons/demogl.git
cd demogl
```
-->

The contents of this directory structure should look as follows (with possibly some extra folders and files that are not relevant for the current guide):

```
demogl/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── .env                  ← backend config
├── src/
│   ├── main/             ← Java source code
│   │   ├── java/com/example/demo/
│   │   └── resources/
│   │       └── application.yaml
│   └── test/              ← Java tests
└── frontend/
    ├── .env               ← frontend config
    ├── src/               ← frontend source code
    ├── package.json
    └── vite.config.ts
```

---

## Set up the PostgreSQL database

The instructions below are for the Supabase hosting service.
Alternatively, you can use any other JDBC-compatible PostgreSQL client.

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

## Configure the backend

### Create environment variables

You need **environment variables** for accessing the remote database from your machine.
To do so, at the **root of the project** (where `build.gradle` is located), create a `.env` file based on the following template:

```
DB_URL='jdbc:postgresql://[HOST]:[PORT]/postgres'
DB_USERNAME='[USERNAME]'
DB_PASSWORD='[PASSWORD]'
```
**(`postgres` is the default database name in Supabase. You can change it if you want, but do not forget to modify the `.env` file accordingly)**

Here is a concrete example of a `.env` file:
    
```
DB_URL='jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres'
DB_USERNAME='postgres.tttrpqqpsixghjtrkenj'
DB_PASSWORD='mysecretpassword'
```

**Attention!** Never commit and push  `.env` files to your git repository. They contain confidential and sensitive information (such as your username and password) that you do not want others to abuvse! Make sure to list `.env` files in the `.gitignore` instructions to avoid accidentally committing them.

---

## Run the backend

### Option A (recommended) - Running Gradle directly from the terminal

Load the `.env` file from the project root into the environment **before** running Gradle. The method depends on the OS you're using.

#### Linux / macOS:

Run the following command in a terminal at the project root:

```bash
export $(cat .env | xargs)
```

or alternatively, export one variable at a time from the `.env` file:

```bash
export DB_URL=...
export DB_USERNAME=...
```
Use `gradlew` to run the API:

```bash
./gradlew bootRun
```

Check that the variables are correctly loaded:

```bash
export -p
```

#### Windows (PowerShell):

Export one variable at a time:

```powershell
$env:DB_URL="..."
$env:DB_USERNAME="..."
$env:DB_PASSWORD="..."
```

Use `gradlew` to run the API:

```bash
.\gradlew bootRun
```

Check that the variables are correctly loaded:

```bash
Get-ChildItem env:
```

### Option B (useful if you need to modify the code) - Running from within an IDE

This guide explains only for IntelliJ IDEA, since it is the IDE we recommend. (You can follow a similar approach for other IDEs.)


#### Install a plugin 

IntelliJ does not automatically load `.env` files. Install the **EnvFile** plugin (by Borys Pierov):

1. `Settings → Plugins` → search for **EnvFile** → install and restart IntelliJ.
2. Open the Spring Boot run configuration (`Run → Edit Configurations`, or see screenshot).
    
    ![image1](docs/images/image1.png)
    
3. To enable the **EnvFile** tab, check "Enable EnvFile", then add the backend's `.env`.
    
    ![image2](docs/images/image2.png)
    
Once configured, IntelliJ automatically injects the variables on every run - no need to export them manually anymore. (this setting persists)

#### Result

The API is available at:

```
http://localhost:8080
```

---

## Populate the database

Once your API is running, in a separate terminal, run the script `seed.ts` located in `frontend/src/config/`
(The API must be running and the database must be empty)

```bash
node seed.ts
```

---

## API documentation

The OpenAPI documentation can be automatically generated from the `@RestController` classes (via the `springdoc-openapi-starter-webmvc-ui` dependency):

| Format | URL |
| --- | --- |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8080/v3/api-docs.yaml` |

To test endpoints directly without using an external tool (such as Postman, Insomnia or Swagger), IntelliJ Ultimate offers a native **Endpoints** tab (`View → Tool Windows → Endpoints`) - make sure the **Spring** and **Spring Web** plugins are enabled, otherwise the endpoints won't show up there.

---

## Configure and run the frontend

From the `frontend` folder:

```bash
npm install
```

Store the frontend environment variables in a `frontend/.env` file:

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

**Note:** In production, `VITE_API_URL` must point to the deployed backend's public URL, and you'll also need to add the deployed frontend's address to the backend's `config/CorsConfig`.

---

## Full startup - summary

Once the `.env` environment files for the backend and frontend have been configured and stored locally:

```bash
# Terminal 1 - Backend (from the project root)
export $(cat .env | xargs)
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

## Troubleshooting

| Problem | Likely cause | Solution                                      |
| --- | --- |-----------------------------------------------|
| `.env` not picked up in IntelliJ | No automatic loading | Install the **EnvFile** plugin and enable it in the run configuration |
| Cannot connect to the database via Direct connection | IPv6/IPv4 network incompatibility | Use Supabase's **Session Pooler** instead     |
| `.env` variables not active in a new terminal | Exports don't persist across sessions | export $(cat .env \| xargs)                   |
| Endpoints missing from the Endpoints tab (IntelliJ) | Spring/Web plugins disabled | Check they're enabled in `Settings → Plugins` |
