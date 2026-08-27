# DemoGL

This repository serves as a teaching example, used at by the Software Engineering Lab of UMONS in the context of some courses and projects. 
The project is intentionally minimalistic and incomplete, as it only serves as a proof-of-concept demonstrator of how to use and combine different software development technologies.
It demonstrates how to build a three-tiered software architecture of a client-server web application, with a backend containing the business logic (in Java) that connects to a relational SQL database, and a frontend containing the web interface that interacts with the backend through a REST API. The purpose of the web application itself is to show and edit the organisational structure of the university (composed of faculties, departments, services, and members). It comes with a read-only user interface, and a read-write administration interface.

## Architecture

- **Frontend**: Vue.js + TypeScript + Tailwind CSS
- **Backend**: Java 25 + Spring Boot + Gradle
- **REST API**: OpenAPI (Swagger)
- **Database**: PostgreSQL (Supabase)

```
Frontend (Vue.js)
       │  
       │  REST API
       ▼
Backend (Java 25 + Spring Boot)
       │  
       │  JDBC + JPA
       ▼
PostgreSQL (Supabase)
```

# Launch guide

The launch guide explains how to install and run the project locally, from the database through the backend up to the frontend.

- [Launch guide in English](/docs/LAUNCH_EN.md)
- [Guide de lancement en français](/docs/LAUNCH_FR.md)
- A full guide is available here: [Guide complet en français](/docs/GUIDE.md)
