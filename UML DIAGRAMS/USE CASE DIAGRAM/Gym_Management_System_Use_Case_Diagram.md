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
