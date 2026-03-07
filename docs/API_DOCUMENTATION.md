# Documentacion Tecnica - Shopping Cart API

## 1) Resumen funcional

Este proyecto implementa una API REST en **Java 17 + Spring Boot 3** para un flujo de carrito de compras con seguridad JWT, clientes, productos, ordenes y pagos.

Despues de los ultimos cambios, la API soporta dos estilos de consumo:

1. **Endpoints principales** (`/api/...`) para el flujo actual de registro de ordenes y pagos.
2. **Endpoints de compatibilidad** (`/api-prueba-cuscatlan/...`) para clientes legacy.

Tambien se agrego manejo de **deshabilitacion logica** (soft delete) en ordenes y pagos con el campo `enabled`, de forma que no se borren fisicamente los datos.

---

## 2) Arquitectura

La solucion mantiene una arquitectura por capas:

- `controller`: recibe y responde HTTP.
- `service` (interfaces): contratos de negocio.
- `serviceImpl`: implementaciones del negocio.
- `repository`: acceso a datos (Spring Data JPA).
- `entity`: modelo persistente MySQL.
- `dto`: contratos de request/response.
- `exception`: manejo centralizado de errores.
- `security`: JWT y reglas de autorizacion.

---

## 3) Funcionalidad principal de la API

### 3.1 Seguridad

- `POST /api/security/register`
- `POST /api/security/login`

Devuelve JWT para consumir endpoints protegidos con:

```http
Authorization: Bearer <TOKEN>
```

### 3.2 Clientes

- `POST /api/clients`
- `GET /api/clients`

### 3.3 Productos

- `GET /api/products`
- `GET /api/products/{id}`

### 3.4 Registro de ordenes (OrderRegistration)

- `POST /api/orderRegistration`
- `PUT /api/orderRegistration`
- `DELETE /api/orderRegistration/delete/{id}` (deshabilita la orden, no la elimina)
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

### 3.5 Registro de pagos (PaymentOrder)

- `POST /api/paymentOrder`
- `PUT /api/paymentOrder`
- `DELETE /api/paymentOrder/delete/{id}` (deshabilita el pago, no lo elimina)
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

---

## 4) Reglas de negocio vigentes

1. **Soft delete**:
   - Ordenes y pagos se deshabilitan (`enabled = false`) en lugar de eliminarse.
2. **Listados filtrados por habilitados**:
   - Solo se muestran transacciones con `enabled = true`.
3. **Pago aprobado**:
   - Si el numero de tarjeta cumple la validacion interna, el pago pasa a `APPROVED`.
4. **Trazabilidad**:
   - Se agrego logging con SLF4J en controllers, servicios y excepciones.

---

## 5) Endpoints de compatibilidad

Base: `/api-prueba-cuscatlan`

Incluye rutas equivalentes para autenticacion, productos, ordenes y pagos para integraciones legacy.

---

## 6) Base de datos

Scripts MySQL:

- `db/mysql/01_create_schema.sql`
- `db/mysql/02_seed_data.sql`

Cambios importantes de esquema:

- `orders.enabled`
- `payments.enabled`
- campos de pagador en `payments` (`payer_names`, `payer_surnames`, `payer_email`, `payer_phone`)

---

## 7) Manejo de errores

La API responde errores en formato estandar:

```json
{
  "timestamp": "2026-03-07T14:47:37",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected internal error",
  "path": "/api/orderRegistration/getAllOrders"
}
```

---

## 8) Pruebas y cobertura

Se incluye en este PDF una seccion de referencia de cobertura/pruebas basada en la captura compartida del reporte JaCoCo.

Resumen visual reportado:

- Cobertura de instrucciones total: **82%**
- Cobertura de ramas total: **58%**
- Lineas cubiertas: **670**

---

## 9) Ejecucion

```bash
mvn spring-boot:run
```

Pruebas:

```bash
mvn test
```
