# Polikt REST API

REST API for **Polikt** - a platform for news, guides and courses - built with **Spring Boot**.

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

## Database

SQL scripts are available in `SQL/`:

| File | Description |
|---|---|
| `create_tables.sql` | Creates tables (users, tag, agency, news, guide, course, etc.) |
| `inserts.sql` | Sample data |
| `select.sql` | Sample queries |
| `delete_tables.sql` | Drops tables |
| `drop_database.sql` | Drops the database |

## Running

```bash
cd api
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Endpoints

### Root

| Method | Route | Description |
|---|---|---|
| `GET` | `/` | Welcome message |

### Users

| Method | Route | Description |
|---|---|---|
| `GET` | `/users` | Lists all users |
| `POST` | `/users` | Creates a new user |

#### Example - Create user

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

#### Example - List users

```bash
curl http://localhost:8080/users
```

## Testing with Bruno

Test requests are in `HTTPRequisitions/` (Bruno collection):

- `Users/Get users.yml` - `GET /users`
- `Users/Add user.yml` - `POST /users`

## Project Structure

```
.
├── api
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── polikt
│   │   │   │           └── api
│   │   │   │               ├── ApiApplication.java
│   │   │   │               ├── guide
│   │   │   │               │   ├── GuideController.java
│   │   │   │               │   ├── Guide.java
│   │   │   │               │   └── GuideRepository.java
│   │   │   │               ├── news
│   │   │   │               │   ├── NewsController.java
│   │   │   │               │   ├── News.java
│   │   │   │               │   └── NewsRepository.java
│   │   │   │               └── user
│   │   │   │                   ├── UserController.java
│   │   │   │                   ├── User.java
│   │   │   │                   └── UserRepository.java
│   │   │   └── resources
│   │   │       └── application.properties
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── polikt
│   │                   └── api
│   │                       └── ApiApplicationTests.java
│   └── target
│       ├── classes
│       │   ├── application.properties
│       │   └── com
│       │       └── polikt
│       │           └── api
│       │               ├── ApiApplication.class
│       │               ├── guide
│       │               │   ├── Guide.class
│       │               │   ├── GuideController.class
│       │               │   └── GuideRepository.class
│       │               ├── news
│       │               │   ├── News.class
│       │               │   ├── NewsController.class
│       │               │   └── NewsRepository.class
│       │               └── user
│       │                   ├── User.class
│       │                   ├── UserController.class
│       │                   └── UserRepository.class
│       ├── generated-sources
│       │   └── annotations
│       ├── generated-test-sources
│       │   └── test-annotations
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       │       ├── createdFiles.lst
│       │       │       └── inputFiles.lst
│       │       └── testCompile
│       │           └── default-testCompile
│       │               ├── createdFiles.lst
│       │               └── inputFiles.lst
│       └── test-classes
│           └── com
│               └── polikt
│                   └── api
│                       └── ApiApplicationTests.class
├── http-requests
│   ├── Guides
│   │   ├── add_guide.yml
│   │   ├── delete_guide_by_id.yml
│   │   ├── folder.yml
│   │   ├── get_guide_by_id.yml
│   │   └── get_guides.yml
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
│       └── get_users.yml
├── LICENSE
├── README.md
└── sql-schemes
    ├── create_tables.sql
    ├── delete_tables.sql
    ├── drop_database.sql
    ├── inserts.sql
    └── select.sql
```