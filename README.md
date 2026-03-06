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
- In-memory H2 database for quick local execution.

## Tech Stack

- Java 17
- Spring Boot 3.3
- Spring Web, Spring Security, Spring Data JPA, Validation
- JWT (`io.jsonwebtoken`)
- H2 Database
- springdoc-openapi
- JUnit 5 + Mockito

## Run locally

### Prerequisites

- Java 17+
- Maven 3.9+

### Steps

1. Clone repository.
2. Run:

```bash
mvn spring-boot:run
```

3. Access:
   - API base: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - OpenAPI docs: `http://localhost:8080/api-docs`
   - H2 console: `http://localhost:8080/h2-console`

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

## Tests

Run tests with:

```bash
mvn test
```

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
