# Gym Management System - OOAD Mini-Project

A comprehensive, Java/Spring Boot based Gym Management System engineered strictly using Object-Oriented Analysis and Design (OOAD) principles and UML-driven architecture.

## Table of Contents
- [Project Overview](#project-overview)
- [Project Structure](#project-structure)
- [UML Diagrams and Design Documentation](#uml-diagrams-and-design-documentation)
- [Architecture Diagram (Mermaid)](#architecture-diagram-mermaid)
- [OOAD Design Patterns Used (Bonus Marks)](#ooad-design-patterns-used-bonus-marks)
- [Technology Stack](#technology-stack)
- [Domain Model](#domain-model)
- [REST API Reference](#rest-api-reference)
- [API Flow Example (Subscription)](#api-flow-example-subscription)
- [Sample JWT Usage](#sample-jwt-usage)
- [Database Polish & Logging](#database-polish--logging)
- [Setup and Run Guide](#setup-and-run-guide)
- [Testing](#testing)

---

## Project Overview
This project models and implements core gym operations such as:
- User registration and login (Admin, Trainer, Member)
- Workout plan creation and exercise assignment
- Progress tracking
- Attendance check-in and check-out
- Dynamic reporting and strategy-based recommendations.

The implementation follows a layered architecture and aligns strictly with the UML analysis artifacts available in the repository.

---

## Project Structure

```text
GYM-MANAGEMENT-SYSTEM/
|-- UML DIAGRAMS/
|   |-- ACTIVITY DIAGRAM/
|   |-- CLASS DIAGRAM/
|   |-- STATE DIAGRAM/
|   |-- USE CASE DIAGRAM/
|-- gym-management-system-backend/
|   |-- pom.xml
|   |-- src/main/java/com/gym/
|   |   |-- config/
|   |   |-- controller/
|   |   |-- dto/
|   |   |-- model/
|   |   |-- repository/
|   |   |-- service/
|   |-- src/main/resources/
|   |-- src/test/java/com/gym/
|-- Gym_OOAD_Project_Documentation.pdf
|-- Mini Project Guidelines.pdf
|-- ARCHITECTURE.md
|-- VIVA_DEMO_SCRIPT.md
```

---

## UML Diagrams and Design Documentation

### 1) Use Case Diagram
Path: `UML DIAGRAMS/USE CASE DIAGRAM/`
- Actor hierarchy with `User` as generalized actor and specialized `Admin`, `Trainer`, `Member`
- External actor: `Payment Gateway`

### 2) Class Diagram
Path: `UML DIAGRAMS/CLASS DIAGRAM/`
- Core classes: `User`, `Member`, `Trainer`, `Admin`, `WorkoutPlan`, `Exercise`, `Progress`, `Attendance`, `Payment`, `Package`
- Inheritance: `Member`, `Trainer`, `Admin` extend abstract `User`
- Composition: `WorkoutPlan` contains `Exercise`

### 3) State Diagram
Path: `UML DIAGRAMS/STATE DIAGRAM/`
- Models member lifecycle from registration to active/inactive/end states

### 4) Activity Diagram
Path: `UML DIAGRAMS/ACTIVITY DIAGRAM/`
- Activity-level flow of major user/system interactions

---

## 🏗️ Architecture Diagram (Mermaid)

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
Based on a `Progress` entity's `BMI`, the `RecommendationService` polymorphically injects `WeightLossStrategy`, `MuscleGainStrategy`, or `GeneralFitnessStrategy` at runtime.

### 2. The Factory Method Pattern (Creational)
**Location:** `com.gym.service.payment.PaymentFactory`
The `PaymentFactory` abstracts the initialization of decoupled, distinct transaction environments (`UpiPaymentService`, `CreditCardPaymentService`).

### 3. Decorator / Wrapper Concept (Structural)
**Location:** `com.gym.dto.ApiResponse<T>`
Every RestController method uniformly routes its response through the `ApiResponse<T>` generic layer, applying a standard data envelope.

---

## Technology Stack
- Java 25 & Spring Boot 3.4.0
- Spring Web, Spring Security, Spring Data JPA
- MySQL DB + `mysql-connector-j`
- Swagger UI (OpenAPI 3)
- Lombok, JUnit 5, Mockito

---

## Domain Model
- `User` (abstract): `userId`, `name`, `email`, `phone` -> (Inherited by `Admin`, `Trainer`, `Member`)
- `WorkoutPlan` (Member, Trainer) -> Many `Exercise`
- `Progress` (Member)
- `Attendance` (Member)
- `Payment` & `Package` (Member)

---

## 🔀 API Flow Example (Subscription)

Consider the critical **Subscription Flow**:

1. **User `POST /api/users/login`**: Client swaps credentials for a JWT token.
2. **Client injects JWT in Authorization Header `Bearer {token}`**.
3. **Admin `POST /api/packages/create`**: Provisions a new gym package.
4. **Member `POST /api/payments/process`**: Submits a payload to trigger the `PaymentFactory`.
5. **System Response `200 OK`**: Returns wrapped uniform JSON confirming `SUCCESS`.

*(Take a look at `VIVA_DEMO_SCRIPT.md` in this repository for a comprehensive end-to-end presentation script.)*

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

## REST API Reference

### User APIs
- `POST /api/users/register`
- `POST /api/users/login`

### Workout & Progress APIs
- `POST /api/workout/create`
- `GET  /api/workout/member/{memberId}`
- `POST /api/progress/update`
- `GET  /api/progress/member/{memberId}`

### Attendance APIs
- `POST /api/attendance/checkin`
- `POST /api/attendance/checkout/{attendanceId}`

### Recommendations & Reports (Power Features)
- `GET /api/recommendation/{memberId}` (Strategy Pattern Generation)
- `GET /api/report/dashboard` (Admin Analytics Dashboard)

---

## 🗄️ Database Polish & Logging

- **JPA Context Indexes**: Heavily queried parameters (`user.email`, `attendance.date`, `payment.status`) are directly annotated with `@Index`. This forces optimized B-Tree lookups on the MySQL database, guaranteeing enterprise scaling.
- **SLF4J Console Logging**: All core `.service` files utilize Lombok `@Slf4j` annotations. Operations emit diagnostic console audit trails (INFO/WARN/ERROR) instead of rudimentary `System.out.println` traces.

---

## 🚀 Setup and Run Guide

1. Clone the repository.
2. Update `src/main/resources/application.properties` with valid standard MySQL credentials (`gymdb`).
3. Boot the application:
```bash
# MacOS / Linux
./mvnw clean spring-boot:run

# Windows
.\mvnw.cmd clean spring-boot:run
```
4. Access the **Interactive Swagger Documentation** at: `http://localhost:8080/swagger-ui.html`

---

## 🔬 Testing
There are **64 native JUnit/Mockito unit tests** verifying edge-case conditions ranging from `PaymentServices` routing to `AttendanceCheckIns`. Run them natively:
```bash
./mvnw clean test
```
