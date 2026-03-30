# Gym Management System

A Java/Spring Boot based Gym Management System designed using object-oriented principles and UML-driven analysis.

This repository contains:
- OOAD and UML design artifacts
- Backend implementation (Spring Boot + JPA + MySQL)
- Unit tests for service-level business logic

## Table of Contents
- [Project Overview](#project-overview)
- [Project Structure](#project-structure)
- [UML Diagrams and Design Documentation](#uml-diagrams-and-design-documentation)
- [Technology Stack](#technology-stack)
- [Architecture and Technical Design](#architecture-and-technical-design)
- [Domain Model](#domain-model)
- [REST API Reference](#rest-api-reference)
- [Database and Persistence](#database-and-persistence)
- [Setup and Run Guide](#setup-and-run-guide)
- [Testing](#testing)
- [Current Scope and Roadmap](#current-scope-and-roadmap)
- [Contributing Workflow](#contributing-workflow)

## Project Overview
This project models and implements core gym operations such as:
- User registration and login (Admin, Trainer, Member)
- Workout plan creation and exercise assignment
- Progress tracking
- Attendance check-in and check-out

The implementation follows a layered architecture and aligns with the UML analysis work available in the repository.

## Project Structure

```text
GYM-MANAGEMENT-SYSTEM/
|-- UML DIAGRAMS/
|   |-- ACTIVITY DIAGRAM/
|   |   |-- Gym_System_Activity_Diagram.drawio
|   |   |-- Gym_System_Activity_Diagram.jpg
|   |-- CLASS DIAGRAM/
|   |   |-- GymManagementSystem_UML_Professional.drawio
|   |   |-- GymManagementSystem_UML_Professional.jpg
|   |   |-- README.md
|   |-- STATE DIAGRAM/
|   |   |-- Member_Lifecycle_State_Diagram.drawio
|   |   |-- Member_Lifecycle_State_Diagram.jpg
|   |   |-- Member_Lifecycle_State_Diagram.md
|   |-- USE CASE DIAGRAM/
|   |   |-- Gym_Management_System_Use_Case_Diagram.drawio
|   |   |-- Gym_Management_System_Use_Case_Diagram.jpg
|   |   |-- Gym_Management_System_Use_Case_Diagram.md
|-- gym-management-system-backend/
|   |-- pom.xml
|   |-- src/
|   |   |-- main/java/com/gym/
|   |   |   |-- backend/
|   |   |   |-- config/
|   |   |   |-- controller/
|   |   |   |-- dto/
|   |   |   |-- model/
|   |   |   |-- repository/
|   |   |   |-- service/
|   |   |-- main/resources/
|   |   |   |-- application.properties
|   |   |-- test/java/com/gym/
|   |       |-- backend/
|   |       |-- service/
|-- Gym_OOAD_Project_Documentation.pdf
|-- Mini Project Guidelines.pdf
```

## UML Diagrams and Design Documentation

### 1) Use Case Diagram
Path: `UML DIAGRAMS/USE CASE DIAGRAM/`
- `Gym_Management_System_Use_Case_Diagram.drawio` (editable)
- `Gym_Management_System_Use_Case_Diagram.jpg` (image export)
- `Gym_Management_System_Use_Case_Diagram.md` (explanation)

Highlights:
- Actor hierarchy with `User` as generalized actor and specialized `Admin`, `Trainer`, `Member`
- External actor: `Payment Gateway`
- Use cases include login/register, workout management, progress tracking, attendance, payment flow
- Includes UML `<<include>>` and `<<extend>>` relationships for mandatory/conditional behavior

### 2) Class Diagram
Path: `UML DIAGRAMS/CLASS DIAGRAM/`
- `GymManagementSystem_UML_Professional.drawio` (editable)
- `GymManagementSystem_UML_Professional.jpg` (image export)
- `README.md` (class-level explanation)

Highlights:
- Core classes: `User`, `Member`, `Trainer`, `Admin`, `WorkoutPlan`, `Exercise`, `Progress`, `Attendance`, `Payment`, `Package`
- Inheritance: `Member`, `Trainer`, `Admin` extend abstract `User`
- Composition: `WorkoutPlan` contains `Exercise`
- Enum modeling: `PaymentStatus` (`PENDING`, `SUCCESS`, `FAILED`)

### 3) State Diagram
Path: `UML DIAGRAMS/STATE DIAGRAM/`
- `Member_Lifecycle_State_Diagram.drawio` (editable)
- `Member_Lifecycle_State_Diagram.jpg` (image export)
- `Member_Lifecycle_State_Diagram.md` (explanation)

Highlights:
- Models member lifecycle from registration to active/inactive/end states
- Captures renewal and payment transitions
- Includes optional suspension/reactivation flows

### 4) Activity Diagram
Path: `UML DIAGRAMS/ACTIVITY DIAGRAM/`
- `Gym_System_Activity_Diagram.drawio` (editable)
- `Gym_System_Activity_Diagram.jpg` (image export)

Highlights:
- Activity-level flow of major user/system interactions
- Useful for process understanding and scenario walkthroughs

## Technology Stack

### Core Backend
- Java 25
- Spring Boot 3.4.0
- Spring Web (REST APIs)
- Spring Data JPA (ORM and repositories)
- Hibernate (JPA provider)
- Maven (build and dependency management)

### Database
- MySQL (configured for `gymdb`)
- JDBC via `mysql-connector-j`

### Code Quality and Productivity
- Lombok (boilerplate reduction using annotations)
- JUnit 5 + Mockito (unit testing)

## Architecture and Technical Design

### Layered Architecture
The backend follows a standard layered design:
1. Controller layer
   - Exposes REST endpoints
   - Handles HTTP request/response mapping
2. Service layer
   - Contains business rules and orchestration
   - Performs validation and domain operations
3. Repository layer
   - JPA repositories for persistence
   - Includes derived queries and custom JPQL queries
4. Model layer
   - JPA entities and relationships
5. DTO layer
   - Request/response contracts to separate API payloads from entities

### Package Structure

```text
com.gym
|-- backend      (Spring Boot application entrypoint)
|-- config       (configuration classes)
|-- controller   (REST controllers)
|-- dto          (request/response objects)
|-- model        (JPA entities)
|-- repository   (JPA repositories)
|-- service      (business services)
```

### Key Technical Decisions
- `User` is an abstract JPA entity with `JOINED` inheritance strategy
- UUID-style string IDs are generated in `@PrePersist` hooks
- Relationships use explicit `@ManyToOne`/`@OneToMany` mappings
- `CascadeType.ALL` and `orphanRemoval = true` are used where composition-style ownership exists
- Services throw `ResponseStatusException` for clean API error signaling

## Domain Model

### User and Roles
- `User` (abstract): `userId`, `name`, `email`, `phone`
- `Admin`, `Trainer`, `Member` inherit from `User`

### Workout and Tracking Entities
- `WorkoutPlan`
  - Linked to one `Member` and one `Trainer`
  - Contains multiple `Exercise` records
- `Exercise`
  - Belongs to one `WorkoutPlan`
- `Progress`
  - Belongs to one `Member`
  - Belongs to one `WorkoutPlan`
- `Attendance`
  - Belongs to one `Member`
  - Stores check-in/check-out timestamps and status

### Relationship Summary
- Member -> WorkoutPlan: one-to-many
- WorkoutPlan -> Exercise: one-to-many (composition-like ownership)
- Member -> Progress: one-to-many
- Member -> Attendance: one-to-many
- Trainer -> WorkoutPlan: one-to-many

## REST API Reference

### API Documentation (Swagger UI)
Interactive OpenAPI documentation is automatically generated. Once the application is running, navigate your browser to:
**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

*Use the "Authorize" button at the top to inject your JWT token and interact with protected endpoints.*

### Standard API Response Wrapper
Every endpoint uniformly returns an `ApiResponse<T>` JSON envelope to standardize error handling and success messages:
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": { },
  "timestamp": "2026-03-30T10:00:00.000"
}
```

Base URL (default local): `http://localhost:8080`

### Authentication/User APIs

#### Register User
- Method: `POST`
- Endpoint: `/register`
- Request body:

```json
{
  "name": "Alex",
  "email": "alex@gym.com",
  "phone": "9876543210",
  "userType": "MEMBER"
}
```

#### Login User
- Method: `POST`
- Endpoint: `/login`
- Request body:

```json
{
  "email": "alex@gym.com",
  "phone": "9876543210"
}
```

### Workout APIs

#### Create Workout Plan
- Method: `POST`
- Endpoint: `/workout/create`

#### Get Workout Plan by ID
- Method: `GET`
- Endpoint: `/workout/{planId}`

#### Get Member Workout Plans
- Method: `GET`
- Endpoint: `/workout/member/{memberId}`

#### Assign Exercise to Workout Plan
- Method: `POST`
- Endpoint: `/workout/exercise/assign`

#### Get Exercises of Plan
- Method: `GET`
- Endpoint: `/workout/{planId}/exercises`

### Progress APIs

#### Update Progress
- Method: `POST`
- Endpoint: `/progress/update`

#### Get Member Progress
- Method: `GET`
- Endpoint: `/progress/member/{memberId}`

#### Get Member Progress for Specific Plan
- Method: `GET`
- Endpoint: `/progress/member/{memberId}/plan/{planId}`

#### Get Member Progress in Date Range
- Method: `GET`
- Endpoint: `/progress/member/{memberId}/range?startDate=...&endDate=...`

### Attendance APIs

#### Check In
- Method: `POST`
- Endpoint: `/attendance/checkin`

#### Check Out
- Method: `POST`
- Endpoint: `/attendance/checkout/{attendanceId}`

#### Get Member Attendance
- Method: `GET`
- Endpoint: `/attendance/member/{memberId}`

#### Get Attendance in Date Range
- Method: `GET`
- Endpoint: `/attendance/member/{memberId}/range?startDate=...&endDate=...`

#### Get Attendance Count in Date Range
- Method: `GET`
- Endpoint: `/attendance/member/{memberId}/count?startDate=...&endDate=...`

## Database and Persistence

Current configuration in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gymdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Notes:
- `ddl-auto=update` updates schema automatically during development.
- For production, use managed migrations (Flyway/Liquibase) and secure secrets.

## Setup and Run Guide

## Prerequisites
- JDK 25 installed
- Maven 3.9+ (or use Maven Wrapper)
- MySQL Server running
- A database named `gymdb`

## Steps
1. Clone the repository.
2. Open a terminal in `gym-management-system-backend`.
3. Update `application.properties` with valid DB credentials.
4. Run the application:

```bash
./mvnw spring-boot:run
```

For Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts on the default Spring Boot port: `8080`.

## Testing
Run all tests from `gym-management-system-backend`:

```bash
./mvnw test
```

Test suite currently includes:
- `UserServiceTest`
- `WorkoutServiceTest`
- `ProgressServiceTest`
- `AttendanceServiceTest`
- `GymManagementSystemBackendApplicationTests`

## Current Scope and Roadmap

### Implemented
- User registration/login
- Role-based user types (`MEMBER`, `TRAINER`, `ADMIN`)
- Workout plan CRUD-related flows (create/retrieve/assign exercises)
- Progress update and retrieval
- Attendance check-in/check-out and reporting endpoints
- **Security hardening (Stateless JWT Authentication & Global Exception Handling)**
- **API Response Wrappers & Swagger OpenAPI 3.0 Documentation**
- **Full payment workflow, Package subscriptions, and receipt generation**

### Planned/Design-Level (from UML)
- Report generation module
- Advanced recommendation strategy implementation

## Contributing Workflow
1. Create a feature branch from `main`.
2. Implement changes with focused commits.
3. Run tests locally.
4. Push branch and open a Pull Request.
5. Merge after review approval.

## License
This project is developed for academic/project purposes. Add an explicit license if required for distribution.
