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

The implementation follows a layered architecture. The UML artifacts are included for OOAD evaluation; the shipped code uses a role-based `AppUser` model (single user entity + `UserRole`) rather than Java inheritance for users.

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
    Service -->|Validates via gateway| PaymentGateway[PaymentGateway]
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

### 2. Payment Gateway Abstraction (Port/Adapter style)
**Location:** `com.gym.service.payment.PaymentGateway`
Payments are processed via a small gateway interface (`PaymentGateway`) that validates a payment request. This keeps the core `PaymentService` decoupled from any real payment provider.

### 3. Decorator / Wrapper Concept (Structural)
**Location:** `com.gym.dto.ApiResponse<T>`
Every RestController method uniformly routes its response through the `ApiResponse<T>` generic layer, applying a standard data envelope.

---

## Technology Stack
- Java 21 & Spring Boot 3.4.0
- Spring Web, Spring Security, Spring Data JPA
- MySQL DB + `mysql-connector-j`
- Swagger UI (OpenAPI 3)
- JUnit 5 (via Spring Boot Starter Test)

---

## Domain Model
- `AppUser`: authentication identity with `role` (`ADMIN` / `TRAINER` / `MEMBER`)
- `Member`: member profile/lifecycle data (linked from `AppUser.memberId`)
- `WorkoutPlan`: created by TRAINER for a MEMBER; contains many `Exercise`
- `ProgressRecord`: progress entries for a MEMBER and a WorkoutPlan
- `Attendance`: check-in/check-out records for a MEMBER
- `Payment` + `GymPackage` + `Membership`: subscription/payment flow

---

## 🔀 API Flow Example (Subscription)

Consider the critical **Subscription Flow**:

1. **User `POST /login`**: Client swaps credentials for a JWT token.
2. **Client injects JWT in Authorization Header `Bearer {token}`**.
3. **Admin `POST /api/packages`**: Provisions a new gym package.
4. **Member `POST /api/payments/process`**: Submits a payload to trigger payment validation.
5. **System Response `200 OK`**: Returns wrapped uniform JSON confirming `SUCCESS`.

*(Take a look at `VIVA_DEMO_SCRIPT.md` in this repository for a comprehensive end-to-end presentation script.)*

---

## 🔑 Sample JWT Usage

Every API endpoint (except `/register` and `/login`) is secured by stateless JWT tokens.

**1. Claiming your Token**
Send a POST request to `/login`:
```json
{
  "email": "user@gym.com",
  "password": "your-password"
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

### Auth APIs
- `POST /register` - Public registration (MEMBER only)
- `POST /login` - Login user (JWT)

### Admin APIs
- `POST /api/admin/users` - Create MEMBER or TRAINER (ADMIN only)
- `GET  /api/admin/trainers` - List trainers (ADMIN only)

### Trainer Assignment (Admin Only)
- **Feature**: Admin can assign trainers to members during registration
- **Usage**: When creating a MEMBER user, optionally provide `trainerUserId`
- **Result**: Links the member profile (`Member.trainerUserId`) to a TRAINER user

### Workout & Progress APIs
- `POST /api/workouts/create` - Trainer creates workout plan
- `GET  /api/workouts/me` - Member views own workout plans
- `GET  /api/workouts/trainer/me` - Trainer views plans they created
- `GET  /api/workouts/{planId}/exercises` - Member views exercises for own plan
- `POST /api/progress/update` - Member updates progress
- `GET  /api/progress/me` - Member views own progress
- `GET  /api/progress/trainer/member/{memberId}` - Trainer views assigned member progress

### Attendance APIs
- `POST /api/attendance/checkin` - Member check-in
- `POST /api/attendance/checkout/{attendanceId}` - Member check-out

### Recommendations & Reports (Power Features)
- `GET /api/recommendation/me` (Strategy Pattern Generation)
- `GET /api/report/dashboard` (Admin Analytics Dashboard)

---

## 🗄️ Database Polish & Logging

This project uses Spring Data JPA repositories over a MySQL schema (auto-managed by Hibernate with `ddl-auto=update`).

---

## 🚀 Setup and Run Guide

1. Clone the repository.
2. **Database Setup**: You must first create the `gymdb` database and assign a local user before booting. Open your local MySQL instance (`mysql -u root -p`) and run:
   ```sql
   CREATE DATABASE IF NOT EXISTS gymdb;
   CREATE USER IF NOT EXISTS 'gymuser'@'localhost' IDENTIFIED BY 'change-me';
   ALTER USER 'gymuser'@'%' IDENTIFIED BY 'change-me';
   GRANT ALL PRIVILEGES ON gymdb.* TO 'gymuser'@'localhost';
   GRANT ALL PRIVILEGES ON gymdb.* TO 'gymuser'@'%';
   FLUSH PRIVILEGES;
   ```
  *Important:* In the SQL above, `change-me` is the MySQL password for user `gymuser`.
  It is used by the backend datasource config in `gym-management-system-backend/src/main/resources/application.properties`:
  - `spring.datasource.username=${DB_USERNAME:gymuser}`
  - `spring.datasource.password=${DB_PASSWORD:change-me}`
  If you change the password in MySQL, update `DB_PASSWORD` (or the default value) to the same password.
  *Note: The application uses `spring.jpa.hibernate.ddl-auto=update`, so tables are automatically created on the first run.*
3. **Free required ports** before starting (if previous runs are still active):
```bash
# Show processes using backend/frontend ports
lsof -nP -iTCP -sTCP:LISTEN | grep -E ":(8080|5173)\\b"

# Optional: force kill processes on those ports
kill -9 $(lsof -t -i:8080) 2>/dev/null || true
kill -9 $(lsof -t -i:5173) 2>/dev/null || true
```

4. **Start Backend** (Terminal 1):
```bash
cd gym-management-system-backend

# macOS / Linux
DB_URL="jdbc:mysql://localhost:3306/gymdb" DB_USERNAME="gymuser" DB_PASSWORD="change-me" ./mvnw spring-boot:run

# Windows (PowerShell)
$env:DB_URL="jdbc:mysql://localhost:3306/gymdb"; $env:DB_USERNAME="gymuser"; $env:DB_PASSWORD="change-me"; .\mvnw.cmd spring-boot:run
```

5. **Start Frontend** (Terminal 2):
```bash
cd gym-management-system-frontend
npm install
npm run dev
```

6. **Open the app**:
- Frontend: `http://localhost:5173`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

7. **Quick health check** (optional):
```bash
curl -I http://localhost:8080/swagger-ui.html
curl -I http://localhost:5173
```

### Common Startup Issues
- **Port 8080 already in use**: stop the existing Java process and restart backend.
- **Port 5173 already in use**: stop existing Vite/Node process and restart frontend.
- **Blank frontend page**: open browser devtools console, fix import/runtime errors, then hard refresh (`Cmd+Shift+R`).
- **DB auth error**: verify MySQL credentials and ensure `gymuser` has access to `gymdb`.

---

## 🔬 Testing
Run backend tests with:
```bash
./mvnw clean test
```
