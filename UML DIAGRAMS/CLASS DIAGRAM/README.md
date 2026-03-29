# Gym Management System - Class Diagram

This README describes the current class diagram for the Gym Management System.

## Diagram Files

1. Editable source: `GymManagementSystem_UML_Professional.drawio`
2. Exported image: `GymManagementSystem_UML_Professional.jpg`
3. This documentation: `README.md`

## Core Classes

### `User` (Abstract)

Base class for all system users.

Attributes:
1. `userId: String`
2. `name: String`
3. `email: String`
4. `phone: String`

Operations:
1. `login(): void`
2. `logout(): void`

### `Member`

Represents gym members.

Attributes:
1. `dob: Date`
2. `gender: String`
3. `joinDate: Date`
4. `status: String`

Operations:
1. `register(): void`
2. `updateProfile(): void`
3. `viewWorkoutPlan(): void`
4. `markAttendance(): void`
5. `makePayment(): void`
6. `updateProgress(): void`
7. `viewProgress(): void`

### `Trainer`

Represents fitness trainers.

Attributes:
1. `specialization: String`

Operations:
1. `createWorkoutPlan(): void`
2. `assignExercises(): void`
3. `viewAssignedMembers(): void`
4. `viewMemberProgress(): void`
5. `updateProgress(): void`

### `Admin`

Represents administrator responsibilities.

Operations:
1. `manageMembers(): void`
2. `assignTrainer(): void`
3. `generateReports(): void`

### `WorkoutPlan`

Stores structured workout plans.

Attributes:
1. `planId: String`
2. `goal: String`
3. `durationWeeks: int`

Operations:
1. `addExercise(): void`
2. `removeExercise(): void`

### `Exercise`

Represents individual exercises in a plan.

Attributes:
1. `exerciseId: String`
2. `name: String`
3. `muscleGroup: String`
4. `sets: int`
5. `reps: int`

### `Attendance`

Tracks member check-in and check-out data.

Attributes:
1. `attendanceId: String`
2. `date: Date`
3. `checkIn: Time`
4. `checkOut: Time`

Operations:
1. `markCheckIn(): void`
2. `markCheckOut(): void`

### `Progress`

Stores fitness progress metrics.

Attributes:
1. `progressId: String`
2. `date: Date`
3. `weight: double`
4. `bmi: double`

Operations:
1. `updateMetrics(): void`

### `Payment`

Handles membership/payment transactions.

Attributes:
1. `paymentId: String`
2. `amount: double`
3. `status: PaymentStatus`
4. `date: Date`

Operations:
1. `processPayment(): void`
2. `generateReceipt(): void`

### `Package`

Defines available membership packages.

Attributes:
1. `packageId: String`
2. `name: String`
3. `price: double`
4. `duration: int`

### `PaymentService` (Interface)

Defines a contract for payment processing.

Operation:
1. `processPayment(): void`

### `PaymentStatus` (Enumeration)

Defines valid payment outcomes and prevents free-text status errors.

Values:
1. `PENDING`
2. `SUCCESS`
3. `FAILED`

## Major Relationships

1. Inheritance:
   - `Member`, `Trainer`, and `Admin` inherit from abstract `User`.

2. Member-Trainer:
   - Members are assigned to a trainer.

3. Trainer-Workout/Attendance:
   - Trainer creates and manages workout/attendance-related records.

4. Member-WorkoutPlan:
   - Member follows/subscribes to workout plans.

5. WorkoutPlan-Exercise:
   - A workout plan is composed of one or more exercises.

6. Member-Progress:
   - Member progress is tracked through progress records.

7. Member-Payment-Package:
   - Members make payments and subscribe to packages.

8. Payment-PaymentService:
   - `Payment` behavior aligns with `PaymentService` processing contract.

9. Payment-PaymentStatus:
   - `Payment` uses `PaymentStatus` as an enum type for the `status` attribute.
   - Optional in UML: a dashed dependency from `Payment` to `PaymentStatus` (line label is not required).

## Notes

1. Multiplicities in implementation should follow the `.drawio` model exactly.
2. This README is aligned with the current professional class diagram image/source in this folder.
