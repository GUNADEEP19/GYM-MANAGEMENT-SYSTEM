# 🎯 OOAD Viva - System Integration Demo Flow

This script is your golden ticket for your OOAD Viva presentation. Follow this **exact sequence** from top to bottom. It proves every single UML relationship and design pattern is fully functional in real-time.

---

### Step 0: Login as Seeded Admin (created at startup)
*Admin is seeded via `app.admin.*` in backend `application.properties`.*
```http
POST /login
{
  "email": "admin@gym.com",
  "password": "admin123"
}
```
**[ACTION]** Copy the `token` and Authorize as **ADMIN** in Swagger.

---

### Step 1: Admin Creates a Trainer User
*Public `/register` is MEMBER-only; TRAINER users are created by ADMIN.*
```http
POST /api/admin/users
{
  "name": "Tom Trainer",
  "email": "trainer@gym.com",
  "phone": "555-0202",
  "password": "Trainer123!",
  "userType": "TRAINER"
}
```
**[ACTION]** Save the returned `id` as `trainerUserId`.

---

### Step 2: Public Register a Member (optionally assign trainer)
*This creates an `AppUser` with role MEMBER + a linked `Member` profile.*
```http
POST /register
{
  "name": "Jane Doe",
  "email": "jane@gym.com",
  "phone": "555-0101",
  "password": "Passw0rd!",
  "trainerUserId": 123
}
```
*(Replace `123` with the `trainerUserId` from Step 1.)*

---

### Step 3: Admin Lists Members (get Jane’s `memberId`)
```http
GET /api/members
```
**[ACTION]** From the response, note Jane’s `id` (this is `memberId`).

---

### Step 4: Login as Member & Obtain JWT Token
```http
POST /login
{
  "email": "jane@gym.com",
  "password": "Passw0rd!"
}
```
**[ACTION]** Copy the `token` and Authorize as **MEMBER** in Swagger.

---

### Step 5: (Switch back) Admin Creates a Package
*Authorize as ADMIN again for this call.*
```http
POST /api/packages
{
  "durationMonths": 6,
  "name": "Premium Bulk",
  "price": 5000.0
}
```
**[ACTION]** Save the returned `packageId`.

---

### Step 6: (Switch to Member) Member Subscribes & Pays
*Authorize as MEMBER for this call.*
```http
POST /api/payments/process
{
  "packageId": "package-id",
  "amount": 5000.0,
  "paymentMethod": "UPI",
  "discountCode": null
}
```
*Note the console logs showing SLF4J tracking the payment initialization and receipt generation.*

---

### Step 7: (Switch to Trainer) Trainer Creates Workout Plan
*Prove object composition (`WorkoutPlan` contains `Exercises`).*
```http
POST /login
{
  "email": "trainer@gym.com",
  "password": "Trainer123!"
}
```
**[ACTION]** Copy the `token` and Authorize as **TRAINER** in Swagger.

```http
POST /api/workouts/create
{
  "memberId": 1,
  "planName": "Hypertrophy Phase 1",
  "description": "Heavy lifting focus",
  "durationWeeks": 6,
  "difficultyLevel": "INTERMEDIATE",
  "exercises": [
    { "exerciseName": "Squat", "sets": 4, "reps": 8, "bodyPart": "LEGS", "instructions": "Controlled tempo" }
  ]
}
```
*(Replace `1` with the `memberId` from Step 3.)*
**[ACTION]** Save the returned plan `id` as `planId`.

---

### Step 8: (Switch to Member) Member Marks Attendance
*Prove real-time operational flows.*
*Authorize as MEMBER for this call.*
```http
POST /api/attendance/checkin
{
  "attendanceDate": null
}
```

---

### Step 9: Member Updates Progress
*Feed data into the Strategy engine.*
*Authorize as MEMBER for this call.*
```http
POST /api/progress/update
{
  "planId": 1,
  "weekNumber": 1,
  "exercisesDone": 4,
  "weight": 85.5,
  "bmi": 26.2,
  "progressNotes": "Feeling strong, gaining weight."
}
```
*(Replace `1` with the `planId` from Step 7.)*

---

### 🔥 Step 10: POWER FEATURE A - Recommendations (Strategy Pattern)
*Showcase polymorphic Object-Oriented behavior dynamically deciding business logic based on BMI.*
```http
GET /api/recommendation/me
```
*Because the BMI is 26.2, the engine will automatically select `WeightLossStrategy`. Mention this explicitly to the evaluator!*

---

### 🔥 Step 11: POWER FEATURE B - Admin Analytics Dashboard
*Showcase the system's ability to aggregate statistics accurately.*
*Authorize as ADMIN for this call.*
```http
GET /api/report/dashboard
```
*Shows `totalMembers: 1`, `activeTrainers: ...`, `totalRevenue: 5000.0`, `todayAttendanceCount: 1`.*
