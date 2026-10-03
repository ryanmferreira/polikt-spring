<div align="center">

# Polikt REST API

**A platform for political education and civic awareness.**

[![Status](https://img.shields.io/badge/Status-In_Development-yellow?style=for-the-badge)](https://github.com/ryanmferreira/polikt-app)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

<br/>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

</div>

REST API for **Polikt** - a platform for news, guides, courses and agencies - built with **Spring Boot**.

## Table of contents

- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running](#running)
- [Running with Docker](#running-with-docker)
- [Running the tests](#running-the-tests)
- [Authentication](#authentication)
- [Endpoints](#endpoints)
- [Error responses](#error-responses)
- [Testing with Bruno](#testing-with-bruno)
- [Project structure](#project-structure)

## Technologies

- Java 26
- Spring Boot 4.1.1
- PostgreSQL
- Maven

Other dependencies:
- Spring Web MVC (`spring-boot-starter-webmvc`)
- Spring Data JPA (`spring-boot-starter-data-jpa`)
- Spring Security (`spring-boot-starter-security`)
- JSON Web Token - JJWT 0.12.6 (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`)
- PostgreSQL JDBC Driver (`org.postgresql:postgresql`, runtime scope)
- Spring Boot DevTools (`spring-boot-devtools`, runtime scope, optional)

Test dependencies:
- `spring-boot-starter-webmvc-test`
- `spring-boot-starter-data-jpa-test`
- `spring-boot-starter-security-test`

## Prerequisites

- JDK 26+
- Maven (or use the `./mvnw` wrapper)
- PostgreSQL database running

## Configuration

The application is configured through environment variables. In development, all of them are optional because they have default values. In production, you **must** set them all, especially `JWT_SECRET`, which has no default.

| Variable | Description | Default (development) |
|---|---|---|
| `DB_URL` | JDBC URL of the PostgreSQL database | `jdbc:postgresql://localhost:5432/polikt_db` |
| `DB_USERNAME` | Database user | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `PORT` | HTTP port | `8080` |
| `JWT_SECRET` | HMAC key used to sign the tokens | none (required) |

Application configuration is done in:

> `api/src/main/resources/application.properties`:

```properties
spring.application.name=api

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/polikt_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}

server.port=${PORT:8080}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET}
jwt.expiration=604800000
```

> `jwt.secret` is the HMAC key used to sign the tokens. Use a long, random value and never commit it to the repository. `jwt.expiration` is the token lifetime in milliseconds (`604800000` = 7 days).

> `ddl-auto=update` creates/updates tables automatically from the JPA entities, and `show-sql=true` prints every query to the log. Both are convenient in development, but **should not be used in production**.

> The `sql-schemes/` folder contains reference SQL scripts (`create_tables.sql`, `drop_database.sql`, `inserts.sql` and others) if you prefer to create or seed the schema manually instead of relying on `ddl-auto`.

On Linux/macOS:

```bash
#!/usr/bin/env bash

export DB_URL="jdbc:postgresql://<url>/<db_name>?sslmode=require"
export DB_USERNAME="user_name"
export DB_PASSWORD="user_password"
export PORT="8080"
export JWT_SECRET="a_long_random_secret"
```

On Windows:

```cmd
set DB_URL=jdbc:postgresql://<url>/<db_name>?sslmode=require
set DB_USERNAME=user_name
set DB_PASSWORD=user_password
set PORT=8080
set JWT_SECRET=a_long_random_secret
```

## Running

```bash
cd api

./mvnw spring-boot:run # or "mvnw.cmd spring-boot:run" on Windows
```

The API will be available, by default, at `http://localhost:8080`.

## Running with Docker

The `Dockerfile` is located at the root of the repository.

```bash
docker build -t polikt-api .

docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://<host>:5432/<db_name>" \
  -e DB_USERNAME="user_name" \
  -e DB_PASSWORD="user_password" \
  -e JWT_SECRET="a_long_random_secret" \
  polikt-api
```

## Running the tests

```bash
cd api

./mvnw test # or "mvnw.cmd test" on Windows
```

## Authentication

The API uses JSON Web Tokens (JWT). To get a token, send your credentials to `POST /users/auth`:

```bash
curl -X POST http://localhost:8080/users/auth \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "psswd@123"
  }'
```

The response is `{ "token": "..." }`. Send it in every protected request:

```
Authorization: Bearer <token>
```

Notes:

- The token identifies the user by their `id`, not by their e-mail. Because of this, **changing the e-mail does not invalidate the token**.
- Tokens expire after the time set in `jwt.expiration` (7 days by default).

### Access rules

| Routes | Access |
|---|---|
| `GET /` | Public |
| `POST /users` (sign up) and `POST /users/auth` (login) | Public |
| All other `GET` endpoints, except `GET /users/me` | Public |
| `GET /users/me` and `PATCH /users/me` | Authenticated |
| Any `POST`, `PATCH` or `DELETE` not listed above | Authenticated |

## Endpoints

All endpoints return JSON except `GET /`, which returns the welcome HTML. For `GET /{id}` and `DELETE /{id}` endpoints, a missing resource returns `404 Not Found`, and a successful deletion returns `204 No Content`.

### Root

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/` | No | Returns the API welcome message |

### Users

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/users` | No | Lists all users |
| `GET` | `/users/{id}` | No | Gets one user by ID |
| `GET` | `/users/me` | Yes | Gets the authenticated user |
| `POST` | `/users` | No | Creates a user |
| `POST` | `/users/auth` | No | Authenticates a user and returns a JWT |
| `PATCH` | `/users/{id}` | Yes | Partially updates a user |
| `PATCH` | `/users/me` | Yes | Partially updates the authenticated user |
| `DELETE` | `/users/{id}` | Yes | Deletes a user by ID |

User creation accepts `name`, `email`, `password` and the optional `phone`. The response includes `id`, `name`, `email`, `phone` and `createdAt`; `password` is write-only and is never returned.

`PATCH /users/{id}` and `PATCH /users/me` accept any subset of `name`, `email`, `password` and `phone`. If the new e-mail already belongs to another user, the API returns `409 Conflict`.

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "user@example.com",
    "password": "psswd@123",
    "phone": "11999999999"
  }'
```

```bash
curl -X PATCH http://localhost:8080/users/me \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane M. Doe",
    "phone": "11988887777"
  }'
```

### Agencies

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/agencies` | No | Lists all agencies |
| `GET` | `/agencies/{id}` | No | Gets one agency by ID |
| `POST` | `/agencies` | Yes | Creates an agency |
| `DELETE` | `/agencies/{id}` | Yes | Deletes an agency by ID |

Agency creation accepts the required fields `name` and `contact`.

```bash
curl -X POST http://localhost:8080/agencies \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "City Hall",
    "contact": "contact@cityhall.example.com"
  }'
```

### News

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/news` | No | Lists all news |
| `GET` | `/news/{id}` | No | Gets one news item by ID |
| `POST` | `/news` | Yes | Creates a news item |
| `PATCH` | `/news/{id}` | Yes | Partially updates a news item |
| `DELETE` | `/news/{id}` | Yes | Deletes a news item by ID |

News creation accepts the required fields `title`, `content`, `summary` and `body`, plus the optional fields `description` and `coverImage`. The author (`user`) is taken automatically from the JWT, so it must not be sent in the body. New items start with `upvotes` set to `0`.

```bash
curl -X POST http://localhost:8080/news \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "City council approves new transparency bill",
    "description": "The proposal aims to expand access to public data",
    "content": "Full text of the news item goes here.",
    "summary": "Short summary of the news item.",
    "body": "Full content of the news item in Markdown.",
    "coverImage": "https://example.com/images/cover.jpg"
  }'
```

### Courses

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/courses` | No | Lists all courses |
| `GET` | `/courses/{id}` | No | Gets one course by ID |
| `POST` | `/courses` | Yes | Creates a course |
| `PATCH` | `/courses/{id}` | Yes | Partially updates a course |
| `DELETE` | `/courses/{id}` | Yes | Deletes a course by ID |

Course creation accepts `title`, `description` and `coverImage`. The author is the authenticated user from the JWT. `PATCH` accepts any subset of `title`, `description` and `coverImage`.

```bash
curl -X POST http://localhost:8080/courses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Introduction to citizenship",
    "description": "Introductory course",
    "coverImage": "https://example.com/images/course.jpg"
  }'
```

### Course modules

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/courses/{courseId}/modules` | No | Lists all modules for a course |
| `GET` | `/courses/{courseId}/modules/{id}` | No | Gets one module by ID |
| `POST` | `/courses/{courseId}/modules` | Yes | Creates a module in a course |
| `PATCH` | `/courses/{courseId}/modules/{id}` | Yes | Partially updates a module |
| `DELETE` | `/courses/{courseId}/modules/{id}` | Yes | Deletes a module by ID |

Module creation accepts `title`, `position` and the optional `description` and `coverImage`. The course is resolved from `courseId`. `PATCH` accepts any subset of those fields.

```bash
curl -X POST http://localhost:8080/courses/1/modules \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "What is citizenship?",
    "position": 1,
    "description": "First module of the course",
    "coverImage": "https://example.com/images/module.jpg"
  }'
```

### Module content

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/courses/{courseId}/modules/{moduleId}/content` | No | Lists all content items for a module |
| `GET` | `/courses/{courseId}/modules/{moduleId}/content/{id}` | No | Gets one content item by ID |
| `POST` | `/courses/{courseId}/modules/{moduleId}/content` | Yes | Creates content in a module |
| `PATCH` | `/courses/{courseId}/modules/{moduleId}/content/{id}` | Yes | Partially updates a content item |
| `DELETE` | `/courses/{courseId}/modules/{moduleId}/content/{id}` | Yes | Deletes a content item by ID |

Content creation accepts `content`, `coverImage` and `position`. The module is resolved from `moduleId`.

```bash
curl -X POST http://localhost:8080/courses/1/modules/1/content \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Text of the lesson goes here.",
    "coverImage": "https://example.com/images/lesson.jpg",
    "position": 1
  }'
```

### Guides

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/guides` | No | Lists all guides |
| `GET` | `/guides/{id}` | No | Gets one guide by ID |
| `POST` | `/guides` | Yes | Creates a guide |
| `PATCH` | `/guides/{id}` | Yes | Partially updates a guide |
| `DELETE` | `/guides/{id}` | Yes | Deletes a guide by ID |

Guide creation accepts the required fields `title` and `content`, plus the optional `description` and `coverImage`. The author (`user`) comes from the JWT, while `agency` can be sent as an object containing its `id`.

```bash
curl -X POST http://localhost:8080/guides \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "How to report potholes in the street",
    "description": "Step by step to file a complaint",
    "content": "Full content of the guide goes here.",
    "coverImage": "https://example.com/images/cover.jpg",
    "agency": { "id": 1 }
  }'
```

### Guide steps

| Method | Route | Auth | Description |
|---|---|---|---|
| `GET` | `/guides/{guideId}/steps` | No | Lists all steps for a guide |
| `GET` | `/guides/{guideId}/steps/{id}` | No | Gets one guide step by ID |
| `POST` | `/guides/{guideId}/steps` | Yes | Creates a guide step |
| `PATCH` | `/guides/{guideId}/steps/{id}` | Yes | Partially updates a guide step |
| `DELETE` | `/guides/{guideId}/steps/{id}` | Yes | Deletes a guide step by ID |

Guide step creation accepts `position`, `content` and the optional `image`. The guide is resolved from `guideId`.

```bash
curl -X POST http://localhost:8080/guides/1/steps \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "position": 1,
    "content": "First step to solve the issue.",
    "image": "https://example.com/images/step-1.jpg"
  }'
```

## Error responses

| Status | Meaning |
|---|---|
| `204 No Content` | Resource deleted successfully |
| `400 Bad Request` | Malformed JSON or invalid/missing fields |
| `401 Unauthorized` | Missing, invalid or expired token, or wrong login credentials |
| `403 Forbidden` | Authenticated, but not allowed to perform the action |
| `404 Not Found` | The requested resource does not exist |
| `409 Conflict` | The value is already in use (e.g. duplicate e-mail) |

## Testing with Bruno

The Bruno collection is available in `http-requests/`:

Users:
- `http-requests/users/get_users.yml` - `GET /users`
- `http-requests/users/add_user.yml` - `POST /users`
- `http-requests/users/get_user_by_id.yml` - `GET /users/{id}`
- `http-requests/users/delete_user_by_id.yml` - `DELETE /users/{id}`
- `http-requests/users/update_user_by_id.yml` - `PATCH /users/{id}`

Login:
- `http-requests/login/login.yml` - `POST /users/auth`
- `http-requests/login/get_my_user.yml` - `GET /users/me`
- `http-requests/login/edit_my_user.yml` - `PATCH /users/me`

Agencies:
- `http-requests/agencies/get_agencies.yml` - `GET /agencies`
- `http-requests/agencies/get_agency_by_id.yml` - `GET /agencies/{id}`
- `http-requests/agencies/add_agency.yml` - `POST /agencies`
- `http-requests/agencies/delete_agency_by_id.yml` - `DELETE /agencies/{id}`

News:
- `http-requests/news/get_news.yml` - `GET /news`
- `http-requests/news/get_news_by_id.yml` - `GET /news/{id}`
- `http-requests/news/add_news.yml` - `POST /news`
- `http-requests/news/delete_news_by_id.yml` - `DELETE /news/{id}`

Guides:
- `http-requests/guides/get_guides.yml` - `GET /guides`
- `http-requests/guides/get_guide_by_id.yml` - `GET /guides/{id}`
- `http-requests/guides/add_guide.yml` - `POST /guides`
- `http-requests/guides/delete_guide_by_id.yml` - `DELETE /guides/{id}`
- `http-requests/guides/steps/get_guide_steps.yml` - `GET /guides/{guideId}/steps`
- `http-requests/guides/steps/get_guide_step_by_id.yml` - `GET /guides/{guideId}/steps/{id}`
- `http-requests/guides/steps/add_a_guide_step.yml` - `POST /guides/{guideId}/steps`
- `http-requests/guides/steps/delete_guide_step_by_id.yml` - `DELETE /guides/{guideId}/steps/{id}`

Courses:
- `http-requests/courses/get_all_courses.yml` - `GET /courses`
- `http-requests/courses/get_course_by_id.yml` - `GET /courses/{id}`
- `http-requests/courses/add_course.yml` - `POST /courses`
- `http-requests/courses/edit_course_by_id.yml` - `PATCH /courses/{id}`
- `http-requests/courses/delete_course_by_id.yml` - `DELETE /courses/{id}`
- `http-requests/courses/modules/get_all_course_modules.yml` - `GET /courses/{courseId}/modules`
- `http-requests/courses/modules/get_course_module_by_id.yml` - `GET /courses/{courseId}/modules/{id}`
- `http-requests/courses/modules/add_course_module.yml` - `POST /courses/{courseId}/modules`
- `http-requests/courses/modules/edit_module_course_by_id.yml` - `PATCH /courses/{courseId}/modules/{id}`
- `http-requests/courses/modules/delete_module_by_id.yml` - `DELETE /courses/{courseId}/modules/{id}`
- `http-requests/courses/modules/content/get_all_module_contents.yml` - `GET /courses/{courseId}/modules/{moduleId}/content`
- `http-requests/courses/modules/content/get_module_content_by_id.yml` - `GET /courses/{courseId}/modules/{moduleId}/content/{id}`
- `http-requests/courses/modules/content/add_module_content.yml` - `POST /courses/{courseId}/modules/{moduleId}/content`
- `http-requests/courses/modules/content/edit_module_content_by_id.yml` - `PATCH /courses/{courseId}/modules/{moduleId}/content/{id}`
- `http-requests/courses/modules/content/delete_module_content_by_id.yml` - `DELETE /courses/{courseId}/modules/{moduleId}/content/{id}`

## Project structure

```plaintext
.
├── api
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── polikt
│       │   │           └── api
│       │   │               ├── agency
│       │   │               │   ├── AgencyController.java
│       │   │               │   ├── Agency.java
│       │   │               │   └── AgencyRepository.java
│       │   │               ├── ApiApplication.java
│       │   │               ├── config
│       │   │               │   ├── CorsConfig.java
│       │   │               │   ├── JwtAuthFilter.java
│       │   │               │   ├── JwtService.java
│       │   │               │   └── SecurityConfig.java
│       │   │               ├── course
│       │   │               │   ├── CourseController.java
│       │   │               │   ├── Course.java
│       │   │               │   ├── CourseRepository.java
│       │   │               │   └── module
│       │   │               │       ├── content
│       │   │               │       │   ├── ContentController.java
│       │   │               │       │   ├── Content.java
│       │   │               │       │   └── ContentRepository.java
│       │   │               │       ├── ModuleController.java
│       │   │               │       ├── Module.java
│       │   │               │       └── ModuleRepository.java
│       │   │               ├── guide
│       │   │               │   ├── GuideController.java
│       │   │               │   ├── Guide.java
│       │   │               │   ├── GuideRepository.java
│       │   │               │   └── step
│       │   │               │       ├── GuideStepController.java
│       │   │               │       ├── GuideStep.java
│       │   │               │       └── GuideStepRepository.java
│       │   │               ├── news
│       │   │               │   ├── NewsController.java
│       │   │               │   ├── News.java
│       │   │               │   └── NewsRepository.java
│       │   │               └── user
│       │   │                   ├── LoginRequest.java
│       │   │                   ├── LoginResponse.java
│       │   │                   ├── UserController.java
│       │   │                   ├── User.java
│       │   │                   └── UserRepository.java
│       │   └── resources
│       │       └── application.properties
│       └── test
│           └── java
│               └── com
│                   └── polikt
│                       └── api
│                           └── ApiApplicationTests.java
├── Dockerfile
├── http-requests
│   ├── agencies
│   │   ├── add_agency.yml
│   │   ├── delete_agency_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_agencies.yml
│   │   └── get_agency_by_id.yml
│   ├── courses
│   │   ├── add_course.yml
│   │   ├── delete_course_by_id.yml
│   │   ├── edit_course_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_all_courses.yml
│   │   ├── get_course_by_id.yml
│   │   └── modules
│   │       ├── add_course_module.yml
│   │       ├── content
│   │       │   ├── add_module_content.yml
│   │       │   ├── delete_module_content_by_id.yml
│   │       │   ├── edit_module_content_by_id.yml
│   │       │   ├── folder.yml
│   │       │   ├── get_all_module_contents.yml
│   │       │   └── get_module_content_by_id.yml
│   │       ├── delete_module_by_id.yml
│   │       ├── edit_module_course_by_id.yml
│   │       ├── folder.yml
│   │       ├── get_all_course_modules.yml
│   │       └── get_course_module_by_id.yml
│   ├── guides
│   │   ├── add_guide.yml
│   │   ├── delete_guide_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_guide_by_id.yml
│   │   ├── get_guides.yml
│   │   └── steps
│   │       ├── add_a_guide_step.yml
│   │       ├── delete_guide_step_by_id.yml
│   │       ├── folder.yml
│   │       ├── get_guide_step_by_id.yml
│   │       └── get_guide_steps.yml
│   ├── login
│   │   ├── Edit my user.yml
│   │   ├── folder.yml
│   │   ├── get_my_user.yml
│   │   └── login.yml
│   ├── news
│   │   ├── add_news.yml
│   │   ├── delete_news_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_news_by_id.yml
│   │   └── get_news.yml
│   ├── opencollection.yml
│   └── users
│       ├── add_user_by_id.yml
│       ├── add_user.yml
│       ├── delete_user_by_id.yml
│       ├── folder.yml
│       ├── get_users.yml
│       └── update_user_by_id.yml
├── LICENSE
├── README.md
├── set-env.cmd
├── set-env.sh
└── sql-schemes
    ├── create_tables.sql
    ├── delete_tables.sql
    ├── drop_database.sql
    ├── inserts.sql
    └── select.sql
```