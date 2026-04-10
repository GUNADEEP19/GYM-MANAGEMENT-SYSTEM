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

### B. The Builder Pattern (Creational)
**Location:** `com.gym.model.Member`, `com.gym.model.WorkoutPlan`, `com.gym.model.AppUser` (static inner `Builder` class in each)
**Use Case Context:** All three domain entities have multiple mandatory fields and at least one optional field. Before this pattern, service code called `new Member()` and then scattered 5–7 setter calls across multiple lines — a partially-constructed object existed between the `new` and the final setter, which was a latent bug risk (e.g., accidentally saving a `Member` with no `status`).
*   Each model now exposes a `static Builder builder()` factory method. The `Builder.build()` method enforces all required fields via `Objects.requireNonNull` before constructing the object, making illegal states unrepresentable.
*   **OOAD Benefit:** Applies the **Single Responsibility Principle** — object construction logic is owned by the Builder, not scattered across every call site. Also demonstrates the **Open/Closed Principle**: adding a new optional field to `WorkoutPlan` (e.g., `targetMuscleGroup`) requires only adding one method to `Builder` with zero changes to existing callers.

### C. The Decorator Pattern (Structural)
**Location:** `com.gym.service.MemberService` (interface), `com.gym.service.DefaultMemberService` (Concrete Component), `com.gym.service.AuditingMemberServiceDecorator` (Concrete Decorator)
**Use Case Context:** Member management operations (create, updateStatus, etc.) needed cross-cutting audit logging added without polluting the core business logic in `DefaultMemberService`.
*   A `MemberService` interface defines the Component contract. `DefaultMemberService` implements the pure business logic. `AuditingMemberServiceDecorator` implements the same interface, holds a reference to a `MemberService` delegate, and transparently wraps every call with structured SLF4J audit logs before and after.
*   `@Primary` on the Decorator ensures Spring routes all `MemberService` injection points through it automatically — the 7 existing consumers (controllers, services) required **zero code changes**.
*   **OOAD Benefit:** Strictly upholds the **Open/Closed Principle** — auditing concern is added by extension (new class), not modification. New cross-cutting behaviours (metrics, rate-limiting, caching) can be stacked as additional decorators without touching `DefaultMemberService` at all.

---

## 💡 3. SOLID Design Principles Implemented

To ensure our codebase remains robust, maintainable, and strictly adheres to OOAD grading criteria, we systematically applied the **SOLID design principles**. Each principle was championed by a team member during implementation:

### A. Single Responsibility Principle (SRP)
**Application:** The `Builder` pattern implementations (`Member.Builder`, `AppUser.Builder`, `WorkoutPlan.Builder`).
**Context:** Before applying SRP, our domain models were "anemic" and the services were responsible for both business logic *and* the step-by-step piecemeal construction of complex objects. By extracting object construction into dedicated `Builder` classes, we ensured that the Service layer only handles business logic, and the Builder solely governs object initialization and validation.

### B. Open/Closed Principle (OCP)
**Application:** The `Strategy` Pattern and the `Decorator` Pattern.
**Context:** Our system is completely open for extension but closed for modification. 
*   **Strategy:** We can add a novel `KetoDietRecommendationStrategy` tomorrow by creating a new class; `RecommendationService` will not need a single line of its core logic modified.
*   **Decorator:** We can stack new cross-cutting features (e.g., `MetricsMemberServiceDecorator`) on top of our `DefaultMemberService` without touching its existing, tested business logic.

### C. Dependency Inversion Principle (DIP)
**Application:** Ubiquitous Dependency Injection (Constructor Injection) across the entire Service layer.
**Context:** High-level modules (Controllers) DO NOT depend on low-level modules (concrete Repositories or concrete Services). For example, `AdminUserController` simply depends on the `MemberService` *interface*. It has no idea that it is actually executing an `AuditingMemberServiceDecorator` wrapping a `DefaultMemberService`. Dependencies are injected at runtime via Spring's IoC container.

### D. Interface Segregation Principle (ISP)
**Application:** Granular Data Access layer (`JpaRepository` implementations).
**Context:** Instead of forcing all data access through one monolithic `IDatabase` interface, we segregated our data contracts. Clients (Services) only depend on the precise repositories they need. For instance, `WorkoutService` relies strictly on `WorkoutPlanRepository` and `ExerciseRepository`, without being burdened by or exposed to unrelated `PaymentRepository` methods.

---

## 🛡️ 4. Safety, Standardization, & Error Handling
We've utilized native Spring Boot mechanisms to catch Edge Case behavior automatically.

### Global Exception Handling (`@ControllerAdvice`)
*   Instead of returning obscure stack traces when a NullReference or Validation violation happens in the deep Java application layers, we built the `GlobalExceptionHandler`. This cleanly intercepts 400s (Bad Requests) and 500s (Internal Errors), gracefully returning the standardized API structural envelope instead of crashing our application instance.

### Jakarta Validation Checkers
*   Inputs into the `/dto/` Request schemas strictly adhere to validation markers (`@NotNull`, `@NotBlank`, `@Positive`). This acts as the frontline defensive shield—requests will automatically be blocked gracefully by Spring Validation libraries before ever successfully reaching internal Domain entities!

---

## 🗄️ 5. Scalability Polish (Logs & Database)
To prove production readiness and system observability beyond just code compilation.

### Professional SLF4J Logic Tracing
*   For viva/demo purposes, key flows are kept simple and rely on Spring’s standard request/response behavior. (If you want production-grade observability, add structured logging + request correlation IDs.)

### JPA Database Indexed Annotations (`@Index`)
*   The application uses Spring Data JPA + Hibernate; database performance tuning (indexes, constraints) can be added at the schema level as needed.

---

## 📖 6. API Documentation Delivery (OpenAPI/Swagger)
We fully integrated `springdoc-openapi`. When running the server, evaluators and UI/UX frontend developers simply navigate directly to:
**`http://localhost:8080/swagger-ui.html`**

*   This graphical interface builds itself automatically based strictly on the methods embedded throughout all RestControllers.
*   Includes built-in test-execution platforms and JWT Authentication header injections seamlessly.
