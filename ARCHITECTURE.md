# Gym Management System - OOAD Architecture & Implementation

This document outlines the **Object-Oriented Analysis and Design (OOAD)** concepts modeled in our UML diagrams and successfully implemented natively in this Spring Boot 3 backend codebase. 

## 🏗️ 1. Core Architecture Map
The application is structured using the **MVC (Model-View-Controller) / N-Tier** architectural pattern backed by a relational MySQL database and strictly protected by an intercepting **JWT (JSON Web Token) Security** filter chain.

*   **API Layer (`/controller`)**: Designed as RESTful endpoints adhering strictly to HTTP verb contracts (e.g., Member marking attendance, Admin generating aggregated reports). 
*   **Service Layer (`/service`)**: Encapsulates the core business logic ensuring controllers remain thin and strictly act as routing boundaries.
*   **Data Access Layer (`/repository`)**: Pure `JpaRepository` interface extensions mapping underlying relational MySQL schemas natively to OOP models via Hibernate ORM automatically.
*   **Data Transfer Objects (`/dto`)**: Protects the database layer by ensuring that Entity representations are completely isolated from internal schemas avoiding mass assignment security defects.

---

## 🧩 2. Gang of Four (GoF) Design Patterns Implemented

We heavily prioritized decoupling and class abstraction by strictly implementing specific GoF Object-Oriented design patterns across major modules.

### A. The Strategy Pattern (Behavioral)
**Location:** `com.gym.service.recommendation`
**Use Case Context:** Overhauling the `Recommend Workout Plan` workflow. Instead of polluting the `RecommendationService` class with complex or rigid if/else tree configurations for different BMI types, we implemented a generic interface `WorkoutRecommendationStrategy`.
*   We created 3 polymorphic classes implementing this exact signature: `WeightLossStrategy`, `MuscleGainStrategy`, and `GeneralFitnessStrategy`. 
*   **OOAD Benefit:** Applies the **Open/Closed Principle**. If this Gym introduces a customized diet-centric routing algorithm tomorrow, our engineers only need to add one new Strategy payload implementation class to the system without mutating (or risking regressions within) the core `RecommendationService`.

### B. The Factory Method Pattern (Creational)
**Location:** `com.gym.service.payment.PaymentGateway`
**Use Case Context:** Payment validation is abstracted behind a small gateway interface (`PaymentGateway`).
*   The core `PaymentService` calls the gateway to validate amounts and method strings without hard-wiring any third-party provider.
*   **OOAD Benefit:** Loose coupling: swapping a fake/demo gateway for a real integration is isolated to one module.

### C. Decorator/Standardized Wrapper Pattern (Structural Concept)
**Location:** `com.gym.dto.ApiResponse<T>`
**Use Case Context:** Ensuring predictable API payload returns globally across every single controller module. Every response leaving this API environment to external clients is dynamically "wrapped" in a standard identical `ApiResponse` object. 

---

## 🛡️ 3. Safety, Standardization, & Error Handling
We've utilized native Spring Boot mechanisms to catch Edge Case behavior automatically.

### Global Exception Handling (`@ControllerAdvice`)
*   Instead of returning obscure stack traces when a NullReference or Validation violation happens in the deep Java application layers, we built the `GlobalExceptionHandler`. This cleanly intercepts 400s (Bad Requests) and 500s (Internal Errors), gracefully returning the standardized API structural envelope instead of crashing our application instance.

### Jakarta Validation Checkers
*   Inputs into the `/dto/` Request schemas strictly adhere to validation markers (`@NotNull`, `@NotBlank`, `@Positive`). This acts as the frontline defensive shield—requests will automatically be blocked gracefully by Spring Validation libraries before ever successfully reaching internal Domain entities!

---

## 🗄️ 4. Scalability Polish (Logs & Database)
To prove production readiness and system observability beyond just code compilation.

### Professional SLF4J Logic Tracing
*   For viva/demo purposes, key flows are kept simple and rely on Spring’s standard request/response behavior. (If you want production-grade observability, add structured logging + request correlation IDs.)

### JPA Database Indexed Annotations (`@Index`)
*   The application uses Spring Data JPA + Hibernate; database performance tuning (indexes, constraints) can be added at the schema level as needed.

---

## 📖 5. API Documentation Delivery (OpenAPI/Swagger)
We fully integrated `springdoc-openapi`. When running the server, evaluators and UI/UX frontend developers simply navigate directly to:
**`http://localhost:8080/swagger-ui.html`**

*   This graphical interface builds itself automatically based strictly on the methods embedded throughout all RestControllers.
*   Includes built-in test-execution platforms and JWT Authentication header injections seamlessly.
