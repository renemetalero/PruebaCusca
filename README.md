# Shopping Cart API - Spring Boot Technical Test

Backend implementation for a shopping cart platform using Java + Spring Boot.

## Features

- JWT authentication and authorization (`/api/security/register`, `/api/security/login`).
- REST APIs for:
  - Clients
  - Products (proxy to `https://fakestoreapi.com`)
  - Orders
  - Order details
  - Payments (simulated process)
- OpenAPI/Swagger documentation.
- Validation and centralized error handling.
- Dedicated DTO + Mapper layer to avoid exposing entities from controllers.
- Unit tests with JUnit + Mockito.
- MySQL-ready schema scripts with indexes and constraints for good query performance.

## Tech Stack

- Java 17
- Spring Boot 3.3
- Spring Web, Spring Security, Spring Data JPA, Validation
- JWT (`io.jsonwebtoken`)
- MySQL 8
- springdoc-openapi
- JUnit 5 + Mockito

## Database setup (MySQL)

Scripts are located in:

- `db/mysql/01_create_schema.sql`
- `db/mysql/02_seed_data.sql`

Run them in order:

```bash
mysql -u root -p < db/mysql/01_create_schema.sql
mysql -u root -p < db/mysql/02_seed_data.sql
```

Default local connection used by `application.yml`:

- Database: `shopping_cart_db`
- User: `root`
- Password: `root`
- URL: `jdbc:mysql://localhost:3306/shopping_cart_db`

## Run locally

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8+

### Steps

1. Clone repository.
2. Create database schema with scripts in `db/mysql`.
3. Adjust credentials in `src/main/resources/application.yml` if needed.
4. Run:

```bash
mvn spring-boot:run
```

5. Access:
   - API base: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - OpenAPI docs: `http://localhost:8080/api-docs`

## API Endpoints

### Security

- `POST /api/security/register`
- `POST /api/security/login`

### Clients

- `POST /api/clients`
- `GET /api/clients`

### Products (FakeStore proxy)

- `GET /api/products`
- `GET /api/products/{id}`

### Orders

- `POST /api/orders`
- `GET /api/orders/{id}`

### Order Details

- `POST /api/details/order/{orderId}`
- `GET /api/details/order/{orderId}`

### Payments

- `POST /api/payments`

## Authentication flow

1. Register or login.
2. Get JWT token.
3. Add header on protected routes:

```http
Authorization: Bearer <token>
```

### How to generate a JWT (step by step)

1. Register a user:

```bash
curl -X POST http://localhost:8080/api/security/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"12345678"}'
```

2. Or login with an existing user:

```bash
curl -X POST http://localhost:8080/api/security/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"12345678"}'
```

3. The API returns:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresInSeconds": 7200
}
```

4. Use the token in protected endpoints:

```bash
curl http://localhost:8080/api/clients \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Tests

Run tests with:

```bash
mvn test
```

> `src/test/resources/application.yml` uses H2 in-memory so unit tests can run without MySQL.

## Error handling

The API returns a standardized error payload:

```json
{
  "timestamp": "2026-03-06T10:22:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error",
  "details": ["email: must be a well-formed email address"],
  "path": "/api/clients"
}
```

## Postman collection

A Postman collection with all endpoints is included at:

- `postman/ShoppingCartApi.postman_collection.json`
