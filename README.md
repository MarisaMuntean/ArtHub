# ArtHub - Enterprise Spring Boot E-Commerce Architecture

**ArtHub** is an e-commerce application architected using **Java 17** and the **Spring Boot 3** ecosystem. It implements the Model-View-Controller (MVC) design pattern to provide a robust platform for art acquisition, featuring role-based security, stateful session management, and third-party payment gateway integration via **Stripe**.

The system emphasizes data integrity, concurrency handling, and secure resource management using industry-standard protocols.

---

## Technical Stack & Architecture

### Backend Core
* **Framework:** Spring Boot 3.0 (Spring MVC, Spring Context)
* **Language:** Java 17 (utilizing Streams API, Optional, Records)
* **Security:** Spring Security 6 (FilterChain, BCrypt, CSRF Protection)
* **ORM / Data:** Spring Data JPA, Hibernate, JPQL
* **Scheduling:** Spring Task Scheduling (`@Scheduled`)
* **Validation:** Jakarta Validation API (Hibernate Validator)

### Frontend & UI
* **Engine:** Thymeleaf (Server-Side Rendering)
* **Styling:** Bootstrap 5 (Custom "Sketchy" Theme), CSS3
* **Interactive Components:** JavaScript (ES6+), noUiSlider for range filtering

### Database & Storage
* **RDBMS:** MySQL 8.0
* **Storage:** Local File System (NIO.2) for asset management
* **Transaction Management:** `@Transactional` propagation

---

## Key System Implementations

### 1. Security & Authentication Layer
The application implements a rigorous security filter chain (`SecurityFilterChain`) to manage access control.
* **Authentication:** Custom `UserDetailsService` implementation backed by MySQL.
* **Password Storage:** Using `BCryptPasswordEncoder` with strength 10.
* **Session Management:** Stateful sessions with role-based redirection (USER vs EDITOR).
* **DTO Pattern:** Registration logic uses `UserRegistrationDto` to decouple the domain entity from the view layer and enforce strict validation rules (Regex patterns for password complexity).

### 2. Concurrency & Transactional Integrity
To prevent **overselling** (Race Conditions) on unique art pieces:
* Implemented a check-then-act locking mechanism in `OrderController`.
* Utilizes database isolation to ensure that a painting cannot be purchased simultaneously by two concurrent threads.
* **Query Logic:** `existsByPaintingAndStatusNot(painting, OrderStatus.CANCELED)` ensures atomicity in stock availability checks.

### 3. Advanced Filtering & Search Algorithms
* **Dynamic Queries:** The catalog utilizes Java Stream API for in-memory filtering of complex criteria (Artist, Period, Technique, Material).
* **JPA Specifications:** Custom JPQL `@Query` methods are used for aggregate data extraction (e.g., finding Min/Max price bounds for the slider normalization).

### 4. Asynchronous Task Scheduling
* **Automated Cleanup:** An `OrderCleanupService` runs a CRON job every 60 seconds (`@Scheduled(fixedRate = 60000)`).
* **Logic:** Detects and purges "Zombie Orders" (orders initiated >10 minutes ago via Card but not paid) to release the stock back to the inventory.

### 5. Payment Gateway Integration
* **Stripe API:** Server-side integration creating secure `Session` objects.
* **Webhook/Callback Handling:** Dedicated endpoints to handle success/cancel URLs, updating the `Order` entity status state-machine from `PENDING` to `PAID` automatically.

---

## Module Visualization

### Authentication & Validation
<img width="800" height="800" alt="image" src="https://github.com/user-attachments/assets/226cde16-757f-47ba-a6cc-cf79b0abcdc5" />

<img width="800" height="800" alt="image" src="https://github.com/user-attachments/assets/0d25bc48-2a99-45f7-957b-ad03a0a5d565" />


### Inventory Management (Editor Role)
<img width="975" height="375" alt="image" src="https://github.com/user-attachments/assets/80b7ff45-f9be-4a09-9699-d4d8ee808efb" />

### Catalog & Discovery (User Role)
<img width="975" height="298" alt="image" src="https://github.com/user-attachments/assets/685a3d2c-3480-4088-b1b8-6255eff5d22c" />


### Checkout & Order Processing
<img width="975" height="415" alt="image" src="https://github.com/user-attachments/assets/cac2e1ec-141b-49ce-8928-e216c259ee77" />
<img width="975" height="475" alt="image" src="https://github.com/user-attachments/assets/759d09cf-ebc0-4b98-a896-86874dfb3eab" />


---

## Database Schema Overview

The domain model is normalized to 3NF and consists of the following core entities:

* **`User`**: Stores credentials and Roles (`ROLE_USER`, `ROLE_EDITOR`).
* **`Painting`**: Contains metadata (Artist, Technique, Price) and file paths.
* **`Order`**: The junction entity linking Users and Paintings. Includes an `OrderStatus` Enum state machine (`PENDING` -> `PAID` -> `SHIPPED`).

---

## Deployment & Setup

### Prerequisites
* JDK 17+
* Maven 3.8+
* MySQL Server 8.0+

### Environment Variables
Configure `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bd_spring_jpa
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# Stripe API Configuration
stripe.api.key=sk_test_...
stripe.public.key=pk_test_...

### Security Audit Highlights

* **CSRF Protection:** Enabled for all state-changing HTTP verbs (`POST`, `PUT`, `DELETE`) to prevent unauthorized command execution.
* **Path Traversal Prevention:** Implementation of `StringUtils.cleanPath()` during file uploads sanitizes filenames to prevent directory climbing attacks.
* **Input Sanitization:** Thymeleaf strict context escaping is utilized to prevent Cross-Site Scripting (XSS) attacks on rendered views.
