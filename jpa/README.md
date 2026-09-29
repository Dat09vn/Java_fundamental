# JPA Oracle Demo

This is a simple Spring Boot + JPA application using Oracle database connectivity.

## Features
- Spring Data JPA
- Oracle JDBC driver
- In-memory H2 for local tests
- CRUD REST endpoints for users

## Run locally

1. Start Oracle XE on localhost:1521.
2. Make sure the database user and password are available.
3. Run:

```bash
./gradlew bootRun
```

## Environment variables
The app uses these values by default if not set:

```bash
DB_URL=jdbc:oracle:thin:@localhost:1521:XE
DB_USER=system
DB_PASSWORD=Oracle123
```

You can override them in the shell before running the app:

```bash
export DB_URL=jdbc:oracle:thin:@localhost:1521:XE
export DB_USER=system
export DB_PASSWORD=Oracle123
./gradlew bootRun
```

## API endpoints

### Get all users
```bash
curl http://localhost:8080/api/users
```

### Create user
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice"}'
```

### Update user
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob"}'
```

### Delete user
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Oracle schema

If you want to create the table manually in Oracle, run:

```sql
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    id NUMBER(19) DEFAULT user_seq.NEXTVAL NOT NULL,
    name VARCHAR2(255),
    CONSTRAINT users_pk PRIMARY KEY (id)
);
```
