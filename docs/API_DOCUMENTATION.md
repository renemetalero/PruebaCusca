# Shopping Cart API - Technical Documentation

## 1. Project overview

This project is a **Java 17 + Spring Boot 3** backend for a shopping cart platform.
It exposes REST endpoints for:

- Security (register/login with JWT)
- Clients
- Products (proxy to FakeStore API)
- Orders
- Order details
- Payments (simulated)

## 2. Architecture and model

The project uses a layered architecture:

1. **Controller layer** (`controller`): Receives HTTP requests and returns responses.
2. **Service layer** (`service`): Encapsulates business rules.
3. **Repository layer** (`repository`): Data access with Spring Data JPA.
4. **Domain model** (`entity`): JPA entities persisted in MySQL.
5. **DTO + Mapper layers** (`dto`, `mapper`): Decouple API contracts from persistence entities.
6. **Security layer** (`security`): JWT generation/validation, filter, and access handlers.
7. **Exception layer** (`exception`): Centralized API error handling with a standard JSON payload.

### Architectural style

- Monolithic backend with internal modular separation.
- Stateless authentication via JWT bearer tokens.
- External integration via HTTP client (`RestClient`) to FakeStore.

## 3. Main libraries

- `spring-boot-starter-web`: REST API support.
- `spring-boot-starter-security`: Authentication and authorization.
- `spring-boot-starter-data-jpa`: ORM and repository abstraction.
- `spring-boot-starter-validation`: Bean validation.
- `spring-boot-starter-actuator`: Observability endpoints.
- `mysql-connector-j`: MySQL JDBC driver.
- `springdoc-openapi-starter-webmvc-ui`: Swagger/OpenAPI docs.
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: JWT creation/validation.
- `lombok`: Boilerplate reduction.
- `spring-boot-starter-test`, `spring-security-test`, `h2`: testing stack.

## 4. Security flow (JWT)

1. User registers (`POST /api/security/register`) or logs in (`POST /api/security/login`).
2. API returns JWT (`token`) with `Bearer` type and expiration in seconds.
3. Client sends token in `Authorization` header for protected endpoints:

```http
Authorization: Bearer <JWT_TOKEN>
```

4. `JwtAuthenticationFilter` validates token and sets security context.
5. Unauthorized/forbidden responses are returned as custom JSON.

## 5. Data model

Main entities:

- `AppUser`: credentials and role.
- `Client`: customer data.
- `OrderEntity`: shopping order metadata.
- `OrderDetail`: order lines with product and quantity.
- `Payment`: payment result and amount.

Persistence:

- MySQL schema scripts in:
  - `db/mysql/01_create_schema.sql`
  - `db/mysql/02_seed_data.sql`

## 6. Endpoints documentation

Base path examples assume `http://localhost:8080`.

---

### 6.1 Security

#### `POST /api/security/register`
Registers a new user and returns JWT.

Request body:

```json
{
  "username": "john",
  "password": "12345678"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresInSeconds": 7200
}
```

#### `POST /api/security/login`
Authenticates an existing user and returns JWT.

Request body:

```json
{
  "username": "john",
  "password": "12345678"
}
```

---

### 6.2 Clients

#### `POST /api/clients`
Creates a client.

Headers:

- `Authorization: Bearer <token>`

Request body:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@doe.com"
}
```

#### `GET /api/clients`
Returns all clients.

Headers:

- `Authorization: Bearer <token>`

---

### 6.3 Products

#### `GET /api/products`
Returns all products by proxying FakeStore API.

#### `GET /api/products/{id}`
Returns a single product by ID through the external proxy.

---

### 6.4 Orders

#### `POST /api/orders`
Creates an order for a client.

Request body:

```json
{
  "clientId": 1
}
```

#### `GET /api/orders/{id}`
Returns order header + detail lines.

---

### 6.5 Order details

#### `POST /api/details/order/{orderId}`
Adds an item into an order.

Request body:

```json
{
  "productId": 1,
  "quantity": 2
}
```

#### `GET /api/details/order/{orderId}`
Returns all lines of one order.

---

### 6.6 Payments

#### `POST /api/payments`
Simulates payment process for an order.

Request body:

```json
{
  "orderId": 1,
  "cardNumber": "1234567890",
  "cardHolder": "John Doe"
}
```

Behavior:

- Approved if card number length >= 8.
- Order status changes to `PAID` when approved.

## 7. Error handling

The API returns a standardized error JSON with:

- `timestamp`
- `status`
- `error`
- `message`
- `details` (optional)
- `path`

Example:

```json
{
  "timestamp": "2026-03-06T10:22:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication is required to access this resource",
  "path": "/api/clients"
}
```

## 8. How to run

1. Create MySQL schema with provided scripts.
2. Configure DB credentials in `src/main/resources/application.yml`.
3. Start application:

```bash
mvn spring-boot:run
```

4. Open Swagger UI:

- `http://localhost:8080/swagger-ui.html`

## 9. Testing

Run tests:

```bash
mvn test
```

Test profile uses H2 in-memory database from:

- `src/test/resources/application.yml`
