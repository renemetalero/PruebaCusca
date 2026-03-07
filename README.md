# API de Carrito de Compras - Prueba Cuscatlán

Backend en **Java 17 + Spring Boot 3** para gestión de clientes, productos, órdenes y pagos.

## Características

- Autenticación JWT (`/api/security/register`, `/api/security/login`).
- Integración de productos con FakeStore API.
- Flujo de órdenes y pagos con endpoints de compatibilidad.
- Manejo de errores centralizado.
- Persistencia en MySQL con scripts incluidos.
- Pruebas unitarias con JUnit y Mockito.

## Endpoints principales

### Seguridad
- `POST /api/security/register`
- `POST /api/security/login`

### Clientes
- `POST /api/clients`
- `GET /api/clients`

### Productos
- `GET /api/products`
- `GET /api/products/{id}`

### OrderRegistration
- `POST /api/orderRegistration`
- `PUT /api/orderRegistration`
- `DELETE /api/orderRegistration/delete/{id}` (deshabilita, no elimina físicamente)
- `GET /api/orderRegistration/getAllOrders`
- `GET /api/orderRegistration/getAnOrder/{id}`

Request esperado:
```json
{
  "id": 0,
  "customer": "string",
  "total": 0,
  "orderStatus": "string",
  "paymentMethod": "string",
  "paymentStatus": "string",
  "details": [
    {
      "id": 0,
      "idProduct": 0,
      "quantity": 0,
      "price": 0
    }
  ]
}
```

### PaymentOrder
- `POST /api/paymentOrder`
- `PUT /api/paymentOrder`
- `DELETE /api/paymentOrder/delete/{id}` (deshabilita, no elimina físicamente)
- `GET /api/paymentOrder/getAllPayments`
- `GET /api/paymentOrder/getAPayment/{id}`

Request esperado:
```json
{
  "id": 0,
  "idOrder": 0,
  "names": "string",
  "surnames": "string",
  "email": "string",
  "phone": "string",
  "number_card": "string"
}
```

## Base de datos

Scripts en `db/mysql`:

1. `01_create_schema.sql`
2. `02_seed_data.sql`

Se agregó soporte de estado/deshabilitación lógica para órdenes y pagos mediante columna `enabled`.

## Ejecución

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

- `POST /api/orders` (accepts `clientId` or compat alias `idClient`)
- `GET /api/orders/{id}`

### Order Details

- `POST /api/details/order/{orderId}`
- `POST /api/details` (compat body with `idOrder` + `idProduct`)
- `GET /api/details/order/{orderId}`

### Payments

- `POST /api/payments` (accepts `orderId`/`cardNumber`/`cardHolder` and compat aliases `idOrder`/`number_card`/`names`)

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
