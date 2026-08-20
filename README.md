# Polikt REST API

REST API for **Polikt** — a platform for news, guides and courses — built with **Spring Boot**.

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

SQL scripts are available in `api/sql/`:

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

#### Example — Create user

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

#### Example — List users

```bash
curl http://localhost:8080/users
```

## Testing with Bruno

Test requests are in `api/PoliktRequisitions/` (Bruno collection):

- `Users/Get users.yml` — `GET /users`
- `Users/Add user.yml` — `POST /users`

## Project Structure

```
api/
├── PoliktRequisitions/     # Bruno request collection
├── sql/                    # SQL scripts
├── src/
│   ├── main/
│   │   ├── java/com/polikt/api/
│   │   │   ├── ApiApplication.java
│   │   │   └── user/
│   │   │       ├── UserControllers.java
│   │   │       ├── UserEntity.java
│   │   │       └── UserRepository.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml