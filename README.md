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

## Pruebas

```bash
mvn test
```
