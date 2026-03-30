# Gym Management System - OOAD Mini-Project

A comprehensive, pure Java/Spring Boot based Gym Management System engineered strictly using Object-Oriented Analysis and Design (OOAD) principles and UML-driven architecture.

---

## 🏗️ Architecture Diagram

```mermaid
graph TD
    Client[Client App / Swagger UI] -->|HTTP REST| Filter[JWT Authentication Filter]
    Filter -->|Valid Token| Controller[REST Controllers]
    Filter -->|Invalid Token| 401[HTTP 401 Unauthorized]
    
    Controller -->|DTO mapped| GlobalEx[Global Exception Handler]
    Controller -->|Delegates logic| Service[Service Layer]
    Controller -->|Returns envelope| Response[ApiResponse Wrapper]
    
    subgraph Core Business Engine
    Service -->|Uses Strategy| StrategyPattern[WorkoutRecommendationStrategy]
    Service -->|Uses Factory| FactoryPattern[PaymentFactory]
    end
    
    Service --> Repo[JPA Repository Layer]
    Repo -->|Hibernate ORM| MySQL[(MySQL Database)]
```

---

## 🎯 OOAD Design Patterns Used (Bonus Marks)

This application heavily utilizes classic Gang of Four (GoF) design patterns to ensure maximum cohesion and minimal coupling.

### 1. The Strategy Pattern (Behavioral)
**Location:** `com.gym.service.recommendation`
Instead of rigid if/else statements for dynamic business logic, we rely on the `WorkoutRecommendationStrategy` interface. Based on a `Progress` entity's `BMI`, the `RecommendationService` polymorphically injects `WeightLossStrategy`, `MuscleGainStrategy`, or `GeneralFitnessStrategy` at runtime.

### 2. The Factory Method Pattern (Creational)
**Location:** `com.gym.service.payment.PaymentFactory`
The API receives abstract representations of a transaction. The `PaymentFactory` abstracts the initialization of decoupled, distinct transaction environments (`UpiPaymentService`, `CreditCardPaymentService`).

### 3. Decorator / Wrapper Concept (Structural)
**Location:** `com.gym.dto.ApiResponse<T>`
Every RestController method uniformly routes its response through the `ApiResponse<T>` generic layer, applying a standard data envelope to all outward bounds payloads.

---

## 🔀 API Flow Example

To understand how the system components interact, consider the critical **Subscription Flow**:

1. **User `POST /api/users/login`**: Client swaps credentials for a JWT token.
2. **Client injects JWT in Authorization Header `Bearer {token}`**.
3. **Admin `POST /api/packages/create`**: Provisions a new gym package.
4. **Member `POST /api/payments/process`**: Submits a payload to trigger the `PaymentFactory`.
5. **System Response `200 OK`**: Returns wrapped uniform JSON confirming `SUCCESS`.

---

## 🚀 Setup and Run Guide

### 1. Prerequisites
- JDK 25 installed
- Maven 3.9+
- MySQL Server

### 2. Database Setup
Create an empty schematic inside MySQL before booting Spring Boot:
```sql
CREATE DATABASE gymdb;
```

### 3. Application Config
Edit `src/main/resources/application.properties` to map correctly to your local environment:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gymdb
spring.datasource.username=root
spring.datasource.password=root
```

### 4. Boot Application
```bash
# MacOS / Linux
./mvnw clean spring-boot:run

# Windows
.\mvnw.cmd clean spring-boot:run
```

---

## 🔑 Sample JWT Usage

Every API endpoint (except `/register` and `/login`) is secured by stateless JWT tokens.

**1. Claiming your Token**
Send a POST request to `/api/users/login`:
```json
{
  "email": "user@gym.com",
  "phone": "555-1234"
}
```
*Backend response:* `"token": "eyJhbGciOiJIUzI1NiJ9..."`

**2. Accessing Protected Routes**
Inject the returned token into your HTTP Header:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
*(If using the built-in Swagger UI at `http://localhost:8080/swagger-ui.html`, simply click the green **Authorize** padlock button at the top of the screen and paste the token).*

---

## 🗄️ Database Polish & Logging
- **JPA Context Indexes**: Heavily queried parameters (`user.email`, `attendance.date`) are directly annotated with `@Index` to force optimized B-Tree lookups on MySQL.
- **SLF4J Console Logging**: All core `.service` files leverage `@Slf4j` tags emitting diagnostic operational statuses rather than rudimentary IDE traces. 

---

## 🔬 Testing
64 native JUnit/Mockito tests verifying edge-case conditions ranging from `PaymentServices` to `AttendanceCheckIns`. Run them natively:
```bash
./mvnw clean test
```
