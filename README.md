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

REST API for **Polikt** - a platform for news, guides and agencies - built with **Spring Boot**.

## Technologies

- Java 26
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Maven

## Prerequisites

- JDK 26+
- Maven (or use the `./mvnw` wrapper)
- PostgreSQL running locally

## Configuration

You must declare the following environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `PORT`

In Linux, you can use something like:
```bash
#!/usr/bin/env bash

export DB_URL="jdbc:postgresql://<url>/<db_name>?sslmode=require"
export DB_USERNAME="user_name"
export DB_PASSWORD="user_password"
```

Or, in Windows, you can use something like:
```powershell
set DB_URL=jdbc:postgresql://<url>/<db_name>?sslmode=require
set DB_USERNAME=user_name
set DB_PASSWORD=user_password
```

Application configuration is done in:

`api/src/main/resources/application.properties`:

```properties
spring.application.name=api

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/polikt_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}

server.port=${PORT:8080}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> `ddl-auto=update` creates/updates tables automatically from the JPA entities.

## Running

```bash
cd api

./mvnw spring-boot:run # or "mvnw.cmd spring-boot:run" in Windows
```

The API will be available, by default, at `http://localhost:8080`.

## Endpoints

All endpoints return JSON except `GET /`, which returns the welcome HTML. Protected endpoints require an `Authorization: Bearer <token>` header.

### Root

| Method | Route | Description |
|---|---|---|
| `GET` | `/` | Returns the API welcome message |

### Users

| Method | Route | Description |
|---|---|---|
| `GET` | `/users` | Lists all users |
| `GET` | `/users/{id}` | Gets one user by ID |
| `POST` | `/users` | Creates a user |
| `DELETE` | `/users/{id}` | Deletes a user by ID |
| `PATCH` | `/users/{id}` | Partially updates a user |
| `POST` | `/users/auth` | Authenticates a user and returns a JWT |

User creation accepts `name`, `email`, `password` and the optional `phone`. The response includes `id`, `name`, `email`, `phone` and `createdAt`; `password` is write-only and is not returned.

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ryan Ferreira",
    "email": "dev.ryanmferreira@outlook.com",
    "password": "psswd@123",
    "phone": "11999999999"
  }'
```

### Agencies

| Method | Route | Description |
|---|---|---|
| `GET` | `/agencies` | Lists all agencies |
| `GET` | `/agencies/{id}` | Gets one agency by ID |
| `POST` | `/agencies` | Creates an agency |
| `DELETE` | `/agencies/{id}` | Deletes an agency by ID |

Agency creation accepts the required fields `name` and `contact`.

```bash
curl -X POST http://localhost:8080/agencies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Prefeitura Municipal",
    "contact": "exemplo@prefeitura.gov.br"
  }'
```

### News

| Method | Route | Description |
|---|---|---|
| `GET` | `/news` | Lists all news |
| `GET` | `/news/{id}` | Gets one news item by ID |
| `POST` | `/news` | Creates a news item |
| `DELETE` | `/news/{id}` | Deletes a news item by ID |

News creation accepts the required fields `title`, `content`, `summary` and `user`, plus the optional fields `description` and `coverImage`. The `user` relation can be sent as an object containing its `id`. New items start with `upvotes` set to `0`.

```bash
curl -X POST http://localhost:8080/news \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Camara aprova novo projeto de lei sobre transparencia",
    "description": "Proposta busca ampliar acesso a dados publicos",
    "content": "Texto completo da noticia aqui.",
    "summary": "Resumo curto da noticia.",
    "coverImage": "https://example.com/images/capa.jpg",
    "user": { "id": 1 }
}'
```

Login accepts `email` and `password` and returns `{ "token": "..." }`. User creation and login are public; the remaining user endpoints require authentication. `PATCH /users/{id}` accepts any subset of `name`, `email`, `password` and `phone`.

```bash
curl -X POST http://localhost:8080/users/auth \
  -H "Content-Type: application/json" \
  -d '{
    "email": "dev.ryanmferreira@outlook.com",
    "password": "psswd@123"
  }'
```

`GET` endpoints are public. Creating and deleting agencies require authentication.

### Courses

| Method | Route | Description |
|---|---|---|
| `GET` | `/courses` | Lists all courses |
| `GET` | `/courses/{id}` | Gets one course by ID |
| `POST` | `/courses` | Creates a course |
| `PATCH` | `/courses/{id}` | Partially updates a course |
| `DELETE` | `/courses/{id}` | Deletes a course by ID |

Course creation accepts `title`, `description`, `coverImage` and `user`, where `user` is an object containing its `id`. `PATCH` accepts any subset of `title`, `description` and `coverImage`.

```bash
curl -X POST http://localhost:8080/courses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Introducao a cidadania",
    "description": "Curso introdutorio",
    "coverImage": "https://example.com/images/curso.jpg",
    "user": { "id": 1 }
  }'
```

Course `GET` endpoints are public. Creating, updating and deleting courses require authentication.

### Course modules

| Method | Route | Description |
|---|---|---|
| `GET` | `/courses/{courseId}/modules` | Lists all modules for a course |
| `GET` | `/courses/{courseId}/modules/{id}` | Gets one module by ID |
| `POST` | `/courses/{courseId}/modules` | Creates a module in a course |
| `PATCH` | `/courses/{courseId}/modules/{id}` | Partially updates a module |
| `DELETE` | `/courses/{courseId}/modules/{id}` | Deletes a module by ID |

Module creation accepts `title`, `position` and the optional `description` and `coverImage`. The course is resolved from `courseId`. `PATCH` accepts any subset of those module fields.

### Module content

| Method | Route | Description |
|---|---|---|
| `GET` | `/courses/{courseId}/modules/{moduleId}/content` | Lists all content items for a module |
| `GET` | `/courses/{courseId}/modules/{moduleId}/content/{id}` | Gets one content item by ID |
| `POST` | `/courses/{courseId}/modules/{moduleId}/content` | Creates content in a module |
| `PATCH` | `/courses/{courseId}/modules/{moduleId}/content/{id}` | Updates a content item |
| `DELETE` | `/courses/{courseId}/modules/{moduleId}/content/{id}` | Deletes a content item by ID |

Content creation accepts `content`, `coverImage` and `position`. The module is resolved from `moduleId`.

### Guides

| Method | Route | Description |
|---|---|---|
| `GET` | `/guides` | Lists all guides |
| `GET` | `/guides/{id}` | Gets one guide by ID |
| `POST` | `/guides` | Creates a guide |
| `DELETE` | `/guides/{id}` | Deletes a guide by ID |

Guide creation accepts the required fields `title`, `content`, `user` and `agency`, plus the optional fields `description` and `coverImage`. Both relations can be sent as objects containing their IDs.

```bash
curl -X POST http://localhost:8080/guides \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Como denunciar buracos na rua",
    "description": "Passo a passo para registrar a reclamacao",
    "content": "Conteudo completo do guia aqui.",
    "coverImage": "https://example.com/images/capa.jpg",
    "user": { "id": 1 },
    "agency": { "id": 1 }
  }'
```

### Guide steps

| Method | Route | Description |
|---|---|---|
| `GET` | `/guides/{guideId}/steps` | Lists all steps for a guide |
| `GET` | `/guides/{guideId}/steps/{id}` | Gets one guide step by ID |
| `POST` | `/guides/{guideId}/steps` | Creates a guide step |
| `DELETE` | `/guides/{guideId}/steps/{id}` | Deletes a guide step by ID |

Guide step creation accepts `position`, `content`, and optional `image`. The `guideId` is provided in the path and the guide itself is resolved internally.

```bash
curl -X POST http://localhost:8080/guides/1/steps \
  -H "Content-Type: application/json" \
  -d '{
    "position": 1,
    "content": "Primeiro passo para resolver a pendencia.",
    "image": "https://example.com/images/step-1.jpg"
  }'
```

For the `GET /{id}` and `DELETE /{id}` endpoints, a missing resource returns `404 Not Found`. Successful deletion returns `204 No Content`.

## Testing with Bruno

The Bruno collection is available in `http-requests/`:

Users:
- `http-requests/users/get_users.yml` - `GET /users`
- `http-requests/users/add_user.yml` - `POST /users`
- `http-requests/users/add_user_by_id.yml` - `GET /users/{id}`
- `http-requests/users/delete_user_by_id.yml` - `DELETE /users/{id}`
- `http-requests/users/update_user_by_id.yml` - `PATCH /users/{id}`

Login:
- `http-requests/login/login.yml` - `POST /users/auth`

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

## Project Structure

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
│   │   ├── folder.yml
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
└── README.md
```