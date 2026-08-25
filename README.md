# Polikt REST API

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

Edit `api/src/main/resources/application.properties`:

```properties
spring.application.name=api

spring.datasource.url=jdbc:postgresql://localhost:5432/polikt_db
spring.datasource.username=postgres
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> `ddl-auto=update` creates/updates tables automatically from the JPA entities.

## Running

```bash
cd api
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Endpoints

All endpoints return JSON except `GET /`, which returns the welcome HTML. The API does not currently expose update endpoints.

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

- `http-requests/users/get_users.yml` - `GET /users`
- `http-requests/users/add_user.yml` - `POST /users`
- `http-requests/users/add_user_by_id.yml` - `GET /users/{id}`
- `http-requests/users/delete_user_by_id.yml` - `DELETE /users/{id}`
- `http-requests/agencies/get_agencies.yml` - `GET /agencies`
- `http-requests/agencies/get_agency_by_id.yml` - `GET /agencies/{id}`
- `http-requests/agencies/add_agency.yml` - `POST /agencies`
- `http-requests/agencies/delete_agency_by_id.yml` - `DELETE /agencies/{id}`
- `http-requests/news/get_news.yml` - `GET /news`
- `http-requests/news/get_news_by_id.yml` - `GET /news/{id}`
- `http-requests/news/add_news.yml` - `POST /news`
- `http-requests/news/delete_news_by_id.yml` - `DELETE /news/{id}`
- `http-requests/guides/get_guides.yml` - `GET /guides`
- `http-requests/guides/get_guide_by_id.yml` - `GET /guides/{id}`
- `http-requests/guides/add_guide.yml` - `POST /guides`
- `http-requests/guides/delete_guide_by_id.yml` - `DELETE /guides/{id}`
- `http-requests/guides/Steps/get_guide_steps.yml` - `GET /guides/{guideId}/steps`
- `http-requests/guides/Steps/get_guide_step_by_id.yml` - `GET /guides/{guideId}/steps/{id}`
- `http-requests/guides/Steps/add_a_guide_step.yml` - `POST /guides/{guideId}/steps`
- `http-requests/guides/Steps/delete_guide_step_by_id.yml` - `DELETE /guides/{guideId}/steps/{id}`

## Project Structure

```text
.
├── .git/
├── .gitignore
├── LICENSE
├── README.md
├── api/
│   ├── .gitattributes
│   ├── .gitignore
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── polikt/
│   │   │   │           └── api/
│   │   │   │               ├── ApiApplication.java
│   │   │   │               ├── agency/
│   │   │   │               │   ├── Agency.java
│   │   │   │               │   ├── AgencyController.java
│   │   │   │               │   └── AgencyRepository.java
│   │   │   │               ├── guide/
│   │   │   │               │   ├── Guide.java
│   │   │   │               │   ├── GuideController.java
│   │   │   │               │   ├── GuideRepository.java
│   │   │   │               │   └── step/
│   │   │   │               │       ├── GuideStep.java
│   │   │   │               │       ├── GuideStepController.java
│   │   │   │               │       └── GuideStepRepository.java
│   │   │   │               ├── news/
│   │   │   │               │   ├── News.java
│   │   │   │               │   ├── NewsController.java
│   │   │   │               │   └── NewsRepository.java
│   │   │   │               └── user/
│   │   │   │                   ├── User.java
│   │   │   │                   ├── UserController.java
│   │   │   │                   └── UserRepository.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── polikt/
│   │                   └── api/
│   │                       └── ApiApplicationTests.java
│   └── target/
│       ├── classes/
│       ├── generated-sources/
│       ├── generated-test-sources/
│       ├── maven-status/
│       └── test-classes/
├── http-requests/
│   ├── agencies/
│   │   ├── add_agency.yml
│   │   ├── delete_agency_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_agencies.yml
│   │   └── get_agency_by_id.yml
│   ├── guides/
│   │   ├── Steps/
│   │   │   ├── add_a_guide_step.yml
│   │   │   ├── delete_guide_step_by_id.yml
│   │   │   ├── folder.yml
│   │   │   ├── get_guide_step_by_id.yml
│   │   │   └── get_guide_steps.yml
│   │   ├── add_guide.yml
│   │   ├── delete_guide_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_guide_by_id.yml
│   │   └── get_guides.yml
│   ├── news/
│   │   ├── add_news.yml
│   │   ├── delete_news_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_news.yml
│   │   └── get_news_by_id.yml
│   ├── opencollection.yml
│   └── users/
│       ├── add_user.yml
│       ├── add_user_by_id.yml
│       ├── delete_user_by_id.yml
│       ├── folder.yml
│       └── get_users.yml
├── set-env.sh
├── sql-schemes/
│   ├── create_tables.sql
│   ├── delete_tables.sql
│   ├── drop_database.sql
│   ├── inserts.sql
│   └── select.sql
└── LICENSE
```
