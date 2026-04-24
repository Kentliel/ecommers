# Proyecto: E-commerce profesional con Spring Boot

## 1. Visión del producto

El objetivo es construir una aplicación web de comercio electrónico profesional, inspirada en plataformas como Amazon o Zalando, pero con un alcance progresivo y realista para un proyecto académico y de portfolio.

La aplicación permitirá a los usuarios navegar por un catálogo de productos, filtrar por categorías, consultar detalles, añadir productos al carrito, realizar pedidos, pagar mediante pasarelas externas como Stripe o PayPal, gestionar direcciones de envío y dejar reseñas sobre productos comprados.

El proyecto debe estar estructurado como una aplicación profesional, con separación clara entre capas, entidades bien modeladas, validaciones, control de errores, seguridad, roles de usuario, documentación técnica y un flujo de trabajo basado en Scrum.

---

## 2. Objetivos del proyecto

### Objetivo principal

Desarrollar un e-commerce funcional, mantenible y presentable en GitHub como proyecto de portfolio profesional.

### Objetivos específicos

- Diseñar un modelo de dominio sólido para una tienda online.
- Implementar una arquitectura limpia basada en capas.
- Crear funcionalidades reales de catálogo, carrito, pedidos, pagos y reseñas.
- Integrar pasarelas de pago externas: Stripe y PayPal.
- Aplicar seguridad con usuarios, roles y permisos.
- Añadir validaciones, manejo de errores y buenas prácticas de backend.
- Preparar documentación profesional para GitHub.
- Trabajar con metodología Scrum: épicas, historias de usuario, sprints y criterios de aceptación.

---

## 3. Stack tecnológico propuesto

### Backend

- Java 21
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Spring Security
- Hibernate
- Maven

### Base de datos

- H2 para desarrollo inicial
- PostgreSQL para entorno profesional/final

### Frontend inicial

- Thymeleaf
- HTML5
- CSS3
- Bootstrap o Tailwind CSS

### Pagos

- Stripe Checkout
- PayPal Checkout

### Herramientas profesionales

- Git y GitHub
- GitHub Projects o Trello para Scrum
- Docker opcional
- Postman o Insomnia para pruebas de endpoints
- README profesional
- Issues y milestones en GitHub

---

## 4. Roles de usuario

### Usuario anónimo

Puede navegar por el catálogo, ver categorías, buscar productos y consultar detalles, pero no puede comprar ni escribir reseñas.

### Cliente registrado

Puede gestionar su perfil, direcciones, carrito, pedidos, pagos y reseñas.

### Administrador

Puede gestionar productos, categorías, marcas, stock, pedidos, usuarios y promociones.

---

## 5. Arquitectura recomendada

La aplicación debe seguir una arquitectura por capas:

```text
src/main/java/com/example/ecommerce
│
├── config/             # Configuración general, seguridad, datos iniciales
├── controller/         # Controladores MVC o REST
├── dto/                # Objetos de transferencia de datos
├── exception/          # Excepciones personalizadas y manejadores globales
├── mapper/             # Conversión entre entidades y DTOs
├── model/              # Entidades JPA
├── repository/         # Repositorios Spring Data JPA
├── security/           # Configuración de autenticación y autorización
├── service/            # Lógica de negocio
└── validation/         # Validaciones personalizadas
```

### Regla importante

Los controladores no deben contener lógica de negocio. Deben delegar en servicios.

```text
Controller → Service → Repository → Database
```

---

## 6. Entidades principales del dominio

El modelo inicial del proyecto tenía cuatro entidades principales: `Product`, `Category`, `Purchase` y `Review`. Para convertirlo en un e-commerce profesional, el modelo debe evolucionar hacia un sistema más realista basado en carrito, pedidos, pagos y direcciones.

---

## 7. Modelo de entidades profesional

### 7.1 User

`User` representa a cualquier persona registrada en la aplicación.

Campos recomendados:

```text
- id: UUID
- firstName: String
- lastName: String
- email: String, único
- password: String, cifrada
- role: Role
- enabled: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

Relaciones:

```text
User 1 ── N Address
User 1 ── N Order
User 1 ── N Review
User 1 ── 1 Cart
```

Roles recomendados:

```java
public enum Role {
    USER,
    ADMIN
}
```

---

### 7.2 Product

`Product` representa un artículo disponible en la tienda.

Campos recomendados:

```text
- id: UUID
- sku: String, único
- name: String
- slug: String, único
- description: String
- shortDescription: String
- price: BigDecimal
- stock: Integer
- imageUrl: String
- active: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

Relaciones:

```text
Product N ── 1 Category
Product N ── 1 Brand
Product 1 ── N Review
Product 1 ── N OrderItem
Product 1 ── N CartItem
```

Notas profesionales:

- Usar `BigDecimal`, no `Double`, para precios.
- `sku` debe ser único.
- `slug` sirve para URLs limpias como `/products/iphone-15-pro`.
- `active` permite ocultar productos sin borrarlos.

---

### 7.3 Category

`Category` agrupa productos.

Campos recomendados:

```text
- id: UUID
- name: String
- slug: String, único
- description: String
- active: Boolean
```

Relaciones:

```text
Category 1 ── N Product
Category N ── 1 Category, opcional para subcategorías
```

Ejemplo:

```text
Electrónica
 ├── Móviles
 ├── Portátiles
 └── Auriculares
```

---

### 7.4 Brand

`Brand` representa la marca de un producto.

Campos recomendados:

```text
- id: UUID
- name: String
- slug: String, único
- description: String
- website: String
- logoUrl: String
```

Relaciones:

```text
Brand 1 ── N Product
```

---

### 7.5 Cart

`Cart` representa el carrito activo de un usuario.

Campos recomendados:

```text
- id: UUID
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

Relaciones:

```text
Cart 1 ── 1 User
Cart 1 ── N CartItem
```

---

### 7.6 CartItem

`CartItem` representa un producto dentro del carrito.

Campos recomendados:

```text
- id: UUID
- quantity: Integer
- unitPrice: BigDecimal
- subtotal: BigDecimal
```

Relaciones:

```text
CartItem N ── 1 Cart
CartItem N ── 1 Product
```

Reglas de negocio:

- La cantidad debe ser mayor que cero.
- No se puede añadir más cantidad que el stock disponible.
- Si el producto ya existe en el carrito, se actualiza la cantidad.

---

### 7.7 Order

`Order` sustituye a la entidad simple `Purchase`. En un e-commerce profesional, una compra real es un pedido que contiene varias líneas.

Campos recomendados:

```text
- id: UUID
- orderNumber: String, único
- status: OrderStatus
- subtotal: BigDecimal
- shippingCost: BigDecimal
- taxAmount: BigDecimal
- total: BigDecimal
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

Relaciones:

```text
Order N ── 1 User
Order N ── 1 Address
Order 1 ── N OrderItem
Order 1 ── 1 Payment
```

Estados recomendados:

```java
public enum OrderStatus {
    PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED
}
```

---

### 7.8 OrderItem

`OrderItem` representa cada línea de un pedido.

Campos recomendados:

```text
- id: UUID
- productName: String
- productSku: String
- quantity: Integer
- unitPrice: BigDecimal
- subtotal: BigDecimal
```

Relaciones:

```text
OrderItem N ── 1 Order
OrderItem N ── 1 Product
```

Nota profesional:

Aunque se relacione con `Product`, conviene guardar también `productName`, `productSku` y `unitPrice`, porque el producto puede cambiar de nombre o precio después de la compra.

---

### 7.9 Payment

`Payment` representa el pago asociado a un pedido.

Campos recomendados:

```text
- id: UUID
- provider: PaymentProvider
- status: PaymentStatus
- amount: BigDecimal
- currency: String
- externalPaymentId: String
- checkoutUrl: String
- paidAt: LocalDateTime
- createdAt: LocalDateTime
```

Relaciones:

```text
Payment 1 ── 1 Order
```

Proveedores recomendados:

```java
public enum PaymentProvider {
    STRIPE,
    PAYPAL
}
```

Estados recomendados:

```java
public enum PaymentStatus {
    PENDING,
    AUTHORIZED,
    PAID,
    FAILED,
    CANCELLED,
    REFUNDED
}
```

---

### 7.10 Address

`Address` representa una dirección de envío del cliente.

Campos recomendados:

```text
- id: UUID
- fullName: String
- phone: String
- street: String
- city: String
- province: String
- postalCode: String
- country: String
- defaultAddress: Boolean
```

Relaciones:

```text
Address N ── 1 User
Address 1 ── N Order
```

---

### 7.11 Review

`Review` representa una reseña escrita por un cliente sobre un producto.

Campos recomendados:

```text
- id: UUID
- rating: Integer
- title: String
- comment: String
- verifiedPurchase: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

Relaciones:

```text
Review N ── 1 User
Review N ── 1 Product
```

Reglas de negocio:

- `rating` debe estar entre 1 y 5.
- Un usuario solo puede dejar una reseña por producto.
- Opcionalmente, solo puede reseñar productos que haya comprado.

---

### 7.12 Promotion

`Promotion` representa descuentos aplicables a productos o categorías.

Campos recomendados:

```text
- id: UUID
- name: String
- code: String, único
- discountType: DiscountType
- discountValue: BigDecimal
- validFrom: LocalDateTime
- validUntil: LocalDateTime
- active: Boolean
```

Tipos de descuento:

```java
public enum DiscountType {
    PERCENTAGE,
    FIXED_AMOUNT
}
```

---

## 8. Diagrama lógico de relaciones

```text
User ────────< Address
User ────────< Order ────────< OrderItem >──────── Product >──────── Category
User ────────< Review >──────────────────────────── Product >──────── Brand
User ──────── Cart ────────< CartItem >──────────── Product
Order ─────── Payment
```

---

## 9. Flujo principal de compra

```text
1. Usuario se registra o inicia sesión.
2. Usuario navega por productos y categorías.
3. Usuario añade productos al carrito.
4. Usuario revisa el carrito.
5. Usuario selecciona o crea dirección de envío.
6. Sistema crea un Order en estado PENDING.
7. Usuario elige método de pago: Stripe o PayPal.
8. Sistema crea un Payment en estado PENDING.
9. Usuario es redirigido a la pasarela de pago.
10. Pasarela confirma o rechaza el pago.
11. Sistema actualiza Payment y Order.
12. Si el pago es correcto, se descuenta stock.
13. Usuario puede consultar su historial de pedidos.
14. Usuario puede dejar reseña tras la compra.
```

---

## 10. Integración con Stripe

### Objetivo

Permitir que el usuario pague un pedido mediante Stripe Checkout.

### Flujo recomendado

```text
1. Crear Order en estado PENDING.
2. Crear sesión de Stripe Checkout desde el backend.
3. Guardar externalPaymentId en Payment.
4. Redirigir al usuario a la URL de Stripe.
5. Recibir confirmación mediante webhook.
6. Actualizar Payment a PAID.
7. Actualizar Order a PAID.
8. Reducir stock.
```

### Variables de entorno

```env
STRIPE_SECRET_KEY=sk_test_xxx
STRIPE_WEBHOOK_SECRET=whsec_xxx
APP_BASE_URL=http://localhost:8080
```

### Endpoints sugeridos

```text
POST /checkout/stripe/{orderId}
POST /webhooks/stripe
GET  /checkout/success
GET  /checkout/cancel
```

---

## 11. Integración con PayPal

### Objetivo

Permitir pagos mediante PayPal Checkout.

### Flujo recomendado

```text
1. Crear Order en estado PENDING.
2. Crear orden de pago en PayPal.
3. Guardar externalPaymentId en Payment.
4. Redirigir al usuario a PayPal.
5. Usuario aprueba el pago.
6. Backend captura el pago.
7. Actualizar Payment a PAID.
8. Actualizar Order a PAID.
9. Reducir stock.
```

### Variables de entorno

```env
PAYPAL_CLIENT_ID=xxx
PAYPAL_CLIENT_SECRET=xxx
PAYPAL_MODE=sandbox
APP_BASE_URL=http://localhost:8080
```

### Endpoints sugeridos

```text
POST /checkout/paypal/{orderId}
GET  /checkout/paypal/success
GET  /checkout/paypal/cancel
```

---

## 12. Seguridad

### Funcionalidades mínimas

- Registro de usuarios.
- Login y logout.
- Contraseñas cifradas con BCrypt.
- Roles `USER` y `ADMIN`.
- Rutas protegidas.
- Panel de administración solo para `ADMIN`.

### Reglas de autorización sugeridas

```text
Público:
- GET /products
- GET /products/{slug}
- GET /categories
- GET /login
- GET /register

Usuario autenticado:
- /cart/**
- /orders/**
- /checkout/**
- /reviews/**
- /account/**

Administrador:
- /admin/**
```

---

## 13. Servicios recomendados

```text
ProductService
CategoryService
BrandService
CartService
OrderService
PaymentService
StripePaymentService
PayPalPaymentService
ReviewService
UserService
AddressService
PromotionService
```

### Principio importante

`PaymentService` puede definir una interfaz común y delegar en implementaciones concretas:

```java
public interface PaymentGateway {
    PaymentResponse createCheckout(Order order);
    void handleWebhook(String payload, String signature);
}
```

Implementaciones:

```text
StripePaymentGateway
PayPalPaymentGateway
```

---

## 14. Repositorios recomendados

```java
ProductRepository extends JpaRepository<Product, UUID>
CategoryRepository extends JpaRepository<Category, UUID>
BrandRepository extends JpaRepository<Brand, UUID>
CartRepository extends JpaRepository<Cart, UUID>
CartItemRepository extends JpaRepository<CartItem, UUID>
OrderRepository extends JpaRepository<Order, UUID>
OrderItemRepository extends JpaRepository<OrderItem, UUID>
PaymentRepository extends JpaRepository<Payment, UUID>
AddressRepository extends JpaRepository<Address, UUID>
ReviewRepository extends JpaRepository<Review, UUID>
PromotionRepository extends JpaRepository<Promotion, UUID>
```

Queries útiles:

```java
Optional<Product> findBySlug(String slug);
Optional<Product> findBySku(String sku);
List<Product> findByActiveTrue();
List<Product> findByCategorySlugAndActiveTrue(String slug);
List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);
List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);
Optional<Cart> findByUserId(UUID userId);
List<Review> findByProductIdOrderByCreatedAtDesc(UUID productId);
boolean existsByUserIdAndProductId(UUID userId, UUID productId);
```

---

## 15. DTOs recomendados

No es recomendable exponer entidades directamente en formularios o respuestas.

DTOs sugeridos:

```text
ProductRequest
ProductResponse
CategoryRequest
CategoryResponse
CartResponse
CartItemResponse
OrderResponse
OrderItemResponse
CheckoutRequest
PaymentResponse
ReviewRequest
ReviewResponse
AddressRequest
AddressResponse
```

---

## 16. Validaciones recomendadas

### Product

```text
- name: obligatorio, 3-100 caracteres
- sku: obligatorio y único
- price: mayor que 0
- stock: mayor o igual que 0
- category: obligatoria
- brand: obligatoria
```

### Review

```text
- rating: entre 1 y 5
- title: máximo 100 caracteres
- comment: máximo 1000 caracteres
```

### Address

```text
- fullName: obligatorio
- phone: obligatorio
- street: obligatorio
- city: obligatorio
- postalCode: obligatorio
- country: obligatorio
```

### Order

```text
- no puede crearse si el carrito está vacío
- no puede pagarse si no hay stock suficiente
- no puede marcarse como PAID sin Payment confirmado
```

---

## 17. Backlog del producto

### Épica 1: Catálogo

Como usuario, quiero ver productos organizados por categorías para encontrar lo que quiero comprar.

Historias:

- Como usuario, quiero ver un listado de productos activos.
- Como usuario, quiero ver el detalle de un producto.
- Como usuario, quiero filtrar productos por categoría.
- Como usuario, quiero buscar productos por nombre.
- Como administrador, quiero crear, editar y desactivar productos.

Criterios de aceptación:

- Los productos inactivos no aparecen en el catálogo público.
- Cada producto muestra nombre, precio, imagen, stock y categoría.
- El detalle del producto muestra descripción, marca y reseñas.

---

### Épica 2: Usuarios y seguridad

Como cliente, quiero registrarme e iniciar sesión para comprar productos y consultar mis pedidos.

Historias:

- Como usuario, quiero registrarme con email y contraseña.
- Como usuario, quiero iniciar sesión.
- Como usuario, quiero cerrar sesión.
- Como administrador, quiero acceder a un panel privado.

Criterios de aceptación:

- Las contraseñas se guardan cifradas.
- Un usuario no autenticado no puede acceder al carrito ni al checkout.
- Un usuario normal no puede acceder a `/admin/**`.

---

### Épica 3: Carrito

Como cliente, quiero añadir productos al carrito para preparar mi compra.

Historias:

- Como cliente, quiero añadir un producto al carrito.
- Como cliente, quiero cambiar cantidades.
- Como cliente, quiero eliminar productos del carrito.
- Como cliente, quiero ver el total del carrito.

Criterios de aceptación:

- No se puede añadir una cantidad superior al stock.
- El subtotal se calcula por línea.
- El total se recalcula al modificar cantidades.

---

### Épica 4: Pedidos

Como cliente, quiero convertir mi carrito en un pedido para poder pagarlo.

Historias:

- Como cliente, quiero crear un pedido desde mi carrito.
- Como cliente, quiero seleccionar una dirección de envío.
- Como cliente, quiero ver mi historial de pedidos.
- Como administrador, quiero ver todos los pedidos.
- Como administrador, quiero actualizar el estado de un pedido.

Criterios de aceptación:

- Un pedido contiene varias líneas de pedido.
- El pedido conserva el precio del producto en el momento de la compra.
- El pedido empieza en estado `PENDING`.

---

### Épica 5: Pagos

Como cliente, quiero pagar mi pedido con Stripe o PayPal para completar la compra.

Historias:

- Como cliente, quiero pagar con Stripe.
- Como cliente, quiero pagar con PayPal.
- Como sistema, quiero recibir confirmaciones de pago.
- Como sistema, quiero marcar pedidos como pagados.
- Como sistema, quiero registrar pagos fallidos.

Criterios de aceptación:

- Un pedido pagado pasa a estado `PAID`.
- Un pago fallido no descuenta stock.
- El stock solo se descuenta tras confirmación de pago.
- Se guarda el identificador externo de Stripe o PayPal.

---

### Épica 6: Reseñas

Como cliente, quiero dejar reseñas para valorar productos comprados.

Historias:

- Como cliente, quiero crear una reseña de un producto.
- Como cliente, quiero editar mi reseña.
- Como usuario, quiero ver las reseñas de un producto.
- Como administrador, quiero moderar reseñas.

Criterios de aceptación:

- La puntuación debe estar entre 1 y 5.
- Un usuario solo puede dejar una reseña por producto.
- Opcionalmente, solo puede reseñar productos que haya comprado.

---

## 18. Plan Scrum por sprints

### Sprint 0: Preparación profesional

Objetivo: dejar el proyecto listo para trabajar en equipo.

Tareas:

- Crear repositorio en GitHub.
- Crear README inicial.
- Configurar ramas: `main`, `develop`, `feature/*`.
- Configurar proyecto Spring Boot.
- Configurar H2 y PostgreSQL.
- Crear estructura de paquetes.
- Crear tablero Scrum en GitHub Projects.

Entregable:

- Proyecto arranca correctamente.
- README explica visión, stack y arquitectura.

---

### Sprint 1: Modelo de dominio base

Objetivo: crear entidades principales y relaciones.

Tareas:

- Crear `User`, `Product`, `Category`, `Brand`.
- Crear `Address`.
- Crear repositorios.
- Crear datos iniciales.
- Crear relaciones JPA.
- Verificar tablas en H2.

Entregable:

- Modelo base persistido en base de datos.

---

### Sprint 2: Catálogo público

Objetivo: permitir navegar productos.

Tareas:

- Crear `ProductService`.
- Crear `CategoryService`.
- Crear controladores.
- Crear vistas Thymeleaf.
- Crear página de listado de productos.
- Crear página de detalle.
- Añadir filtros por categoría.
- Añadir búsqueda por nombre.

Entregable:

- Catálogo navegable desde el navegador.

---

### Sprint 3: Seguridad y usuarios

Objetivo: permitir registro, login y roles.

Tareas:

- Configurar Spring Security.
- Crear registro de usuarios.
- Cifrar contraseñas con BCrypt.
- Crear login y logout.
- Proteger rutas.
- Crear panel admin básico.

Entregable:

- Usuarios pueden iniciar sesión y acceder según su rol.

---

### Sprint 4: Carrito

Objetivo: implementar carrito funcional.

Tareas:

- Crear `Cart` y `CartItem`.
- Crear `CartService`.
- Añadir producto al carrito.
- Actualizar cantidades.
- Eliminar productos.
- Calcular total.
- Validar stock.

Entregable:

- Usuario autenticado puede gestionar su carrito.

---

### Sprint 5: Pedidos

Objetivo: convertir carrito en pedido.

Tareas:

- Crear `Order` y `OrderItem`.
- Crear `OrderService`.
- Crear checkout interno.
- Seleccionar dirección.
- Crear pedido desde carrito.
- Ver historial de pedidos.

Entregable:

- Usuario puede generar un pedido pendiente de pago.

---

### Sprint 6: Pagos con Stripe

Objetivo: integrar Stripe Checkout.

Tareas:

- Crear cuenta de Stripe en modo test.
- Configurar claves de entorno.
- Crear `Payment`.
- Crear `StripePaymentService`.
- Crear sesión de checkout.
- Crear endpoints de success/cancel.
- Implementar webhook.
- Actualizar pedido tras pago correcto.

Entregable:

- Pedido se puede pagar con Stripe en modo test.

---

### Sprint 7: Pagos con PayPal

Objetivo: integrar PayPal Checkout.

Tareas:

- Crear cuenta sandbox de PayPal.
- Configurar credenciales.
- Crear `PayPalPaymentService`.
- Crear orden PayPal.
- Capturar pago.
- Actualizar pedido y pago.

Entregable:

- Pedido se puede pagar con PayPal en sandbox.

---

### Sprint 8: Reseñas y mejoras finales

Objetivo: completar funcionalidades de valor.

Tareas:

- Crear `ReviewService`.
- Crear reseñas por producto.
- Validar una reseña por usuario y producto.
- Mostrar promedio de valoración.
- Mejorar diseño visual.
- Añadir manejo global de errores.
- Añadir documentación final.

Entregable:

- Proyecto presentable como portfolio.

---

## 19. Definition of Done

Una tarea se considera terminada cuando:

- El código compila sin errores.
- La funcionalidad está probada manualmente.
- La lógica de negocio está en servicios.
- Las entidades tienen validaciones básicas.
- No hay credenciales reales en el repositorio.
- El código sigue nombres claros y consistentes.
- Hay commits descriptivos.
- La funcionalidad está documentada si es relevante.
- La rama se puede fusionar sin conflictos.

---

## 20. Criterios profesionales para GitHub

El repositorio debe incluir:

```text
README.md
.gitignore
.env.example
pom.xml
src/
docs/
```

### README recomendado

El README principal debe explicar:

- Nombre del proyecto.
- Descripción corta.
- Capturas de pantalla.
- Stack tecnológico.
- Funcionalidades.
- Modelo de entidades.
- Cómo ejecutar el proyecto.
- Variables de entorno.
- Estado del proyecto.
- Roadmap.
- Autor.

### Buenas prácticas de commits

Ejemplos:

```text
feat: add product entity and repository
feat: implement cart service
feat: integrate stripe checkout
fix: validate stock before adding cart item
docs: update README with payment flow
refactor: move checkout logic to service layer
```

---

## 21. Variables de entorno recomendadas

Crear un archivo `.env.example`:

```env
APP_BASE_URL=http://localhost:8080

DB_URL=jdbc:postgresql://localhost:5432/ecommerce_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

STRIPE_SECRET_KEY=sk_test_xxx
STRIPE_WEBHOOK_SECRET=whsec_xxx

PAYPAL_CLIENT_ID=xxx
PAYPAL_CLIENT_SECRET=xxx
PAYPAL_MODE=sandbox
```

No subir nunca `.env` real al repositorio.

---

## 22. Roadmap profesional

### Versión 1.0

- Catálogo público.
- Registro y login.
- Carrito.
- Pedidos.
- Pago con Stripe.
- Reseñas.

### Versión 1.1

- Pago con PayPal.
- Panel de administración.
- Gestión avanzada de stock.
- Promociones.

### Versión 1.2

- Docker.
- PostgreSQL definitivo.
- Tests unitarios.
- Tests de integración.
- API REST opcional.

### Versión 2.0

- Frontend separado con Angular, React o Vue.
- Autenticación JWT.
- Despliegue en cloud.
- Sistema de emails.
- Facturas en PDF.

---

## 23. Orden recomendado de implementación

No conviene empezar directamente por Stripe o PayPal. El orden profesional es:

```text
1. Modelo de entidades
2. Repositorios
3. Datos iniciales
4. Servicios
5. Controladores y vistas
6. Seguridad
7. Carrito
8. Pedidos
9. Pagos
10. Reseñas
11. Panel de administración
12. Documentación final
```

Los pagos dependen de que antes existan usuarios, carrito, pedidos y totales bien calculados.

---

## 24. Recomendación de alcance para portfolio

Para que el proyecto sea profesional pero viable, la primera versión debería centrarse en:

- Catálogo completo.
- Registro/login.
- Carrito funcional.
- Checkout con creación de pedido.
- Stripe en modo test.
- Historial de pedidos.
- Reseñas.
- Panel admin básico.
- README profesional con capturas.

PayPal puede añadirse como mejora posterior, porque tener Stripe bien implementado ya aporta mucho valor profesional.

---

## 25. Resumen final

Este proyecto debe evolucionar desde una tienda sencilla con productos, categorías, compras y reseñas hacia un e-commerce profesional con pedidos, carrito, pagos, direcciones, seguridad y administración.

La clave no es añadir muchas funcionalidades de golpe, sino construir una base sólida, con buen modelo de datos, arquitectura limpia y documentación clara. Si se implementa por sprints, el proyecto será más fácil de desarrollar, probar, explicar y defender en una entrevista técnica.
