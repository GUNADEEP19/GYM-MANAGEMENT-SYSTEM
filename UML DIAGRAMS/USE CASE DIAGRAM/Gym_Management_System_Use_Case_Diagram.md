# Gym Management System - Use Case Diagram (Current)

This README reflects the latest version of the use-case model in the `.drawio` diagram.

## Actor Model

1. `User` is the generalized actor.
2. `Admin`, `Trainer`, and `Member` specialize `User`.
3. External actor: `Payment Gateway`.

## Core Use Cases by Role

### Common

1. `Login`
2. `Register`

### Admin

1. `Manage Members`
2. `Generate Reports`
3. `Assign Trainer to Member`

### Trainer

1. `Create Workout Plan`
2. `Assign Exercises`
3. `View Assigned Members`
4. `View Member Progress`

### Member

1. `Recommend Workout Plan`
2. `Mark Attendance`
3. `Check Membership Validity`
4. `View Workout Plan`
5. `View Assigned Trainer`
6. `Update Progress`
7. `View Own Progress`
8. `Make Payment`

### Payment and Failure Handling

1. `Validate Payment`
2. `Generate Receipt`
3. `Apply Discount`
4. `Payment Failure Handling`

### Recommendation Logic

1. `Analyze Member Data`
2. `Select Strategy`

## UML Relationship Mapping

### `<<include>>` (mandatory sub-flows)

1. `Make Payment` `<<include>>` `Validate Payment`
2. `Make Payment` `<<include>>` `Generate Receipt`
3. `Mark Attendance` `<<include>>` `Check Membership Validity`
4. `Create Workout Plan` `<<include>>` `Assign Exercises`
5. `Recommend Workout Plan` `<<include>>` `Analyze Member Data`
6. `Recommend Workout Plan` `<<include>>` `Select Strategy`

### `<<extend>>` (conditional behavior)

1. `Apply Discount` `<<extend>>` `Make Payment`
2. `Payment Failure Handling` `<<extend>>` `Validate Payment`

## Key Associations

1. `Payment Gateway` is associated with payment validation flow.
2. `Member` is associated with payment, attendance, recommendation, and progress use cases.
3. `Trainer` is associated with workout creation, assigned members, and member progress monitoring.
4. `Admin` is associated with member management, reporting, and trainer assignment.

## Diagram Files

1. Primary editable diagram: `Gym_Management_System_Use_Case_Diagram.drawio`
2. This explanation file: `Gym_Management_System_Use_Case_Diagram.md`

## Implementation Coverage (What’s Real vs What’s “Paper”)

This section is the **OOAD correctness audit**: the use-case diagram is directionally right, but several use cases are not implemented (or implemented in a different place than the diagram implies).

Legend:
- ✅ Implemented end-to-end
- 🟡 Partially implemented / simplified
- ❌ Not implemented (diagram-only)

### Common

- Login ✅ (JWT token issued)
- Register ✅ (member registration)

### Admin

- Manage Members 🟡 (admin can create users and member profiles; member/membership legacy controllers exist but are admin-only now)
- Generate Reports ✅ (dashboard metrics)
- Assign Trainer to Member ✅ (trainer can be assigned at member creation; update flow may be limited)

### Trainer

- Create Workout Plan ✅ (trainer creates a plan for a member)
- Assign Exercises ✅ (exercises can be provided as part of plan creation)
- View Assigned Members ✅
- View Member Progress ✅ (trainer can view progress for assigned members)

### Member

- Recommend Workout Plan ✅ (strategy selection exists; based on BMI)
- Mark Attendance ✅ (check-in/check-out)
- Check Membership Validity ✅ (member can query validity; attendance enforces active membership on check-in)
- View Workout Plan ✅ (member can view own plans)
- View Assigned Trainer ✅ (member can fetch assigned trainer details via API)
- Update Progress ✅
- View Own Progress ✅
- Make Payment ✅ (records payment + assigns membership)

### Payment and Failure Handling

- Validate Payment 🟡 (server validates request; payment gateway is simulated)
- Generate Receipt ✅ (receipt payload + receipt number)
- Apply Discount ✅ (discount codes supported)
- Payment Failure Handling 🟡 (FAILED status exists; no retry/compensation flow beyond “status = FAILED”)

### Recommendation Logic

- Analyze Member Data 🟡 (uses member’s stored metrics; simplified)
- Select Strategy ✅ (Strategy pattern exists)

## OOAD Notes (If Examiner Pushes)

- The diagram includes an external `Payment Gateway` actor, but the implementation is a simulated payment flow (no third-party integration). Either implement a gateway adapter, or remove that actor from the diagram.
- The `<<include>> Check Membership Validity` under attendance is a **claim**; to make it true, attendance service must reject check-in/out when membership is not ACTIVE.
