# 🎯 OOAD Viva - System Integration Demo Flow

This script is your golden ticket for your OOAD Viva presentation. Follow this **exact sequence** from top to bottom. It proves every single UML relationship and design pattern is fully functional in real-time.

---

### Step 1: Register Core Actors
*Prove inheritance (`Member`, `Trainer`, `Admin` extending `User`).*
```http
POST /api/users/register
{
  "name": "Jane Doe",
  "email": "jane@gym.com",
  "phone": "555-0101",
  "userType": "MEMBER"
}
```
*(Repeat for a TRAINER and an ADMIN if needed)*

---

### Step 2: Login & Obtain JWT Token
*Prove stateless security and get the token required for all subsequent calls.*
```http
POST /api/users/login
{
  "email": "jane@gym.com",
  "phone": "555-0101"
}
```
**[ACTION]** Copy the `token` from the response and inject it into the Swagger UI "Authorize" modal.

---

### Step 3: Admin Creates a Package
*Prove administrative provisioning.*
```http
POST /api/packages/create
{
  "packageName": "Premium Bulk",
  "price": 5000.0,
  "durationMonths": 6,
  "description": "Unlimited access with personal training."
}
```
**[ACTION]** Save the returned `packageId`.

---

### Step 4: Member Subscribes & Pays
*Prove the `PaymentFactory` pattern and third-party delegation.*
```http
POST /api/payments/process
{
  "memberId": "jane-id",
  "packageId": "package-id",
  "amount": 5000.0,
  "paymentMethod": "UPI"
}
```
*Note the console logs showing SLF4J tracking the payment initialization and receipt generation.*

---

### Step 5: Trainer Creates Workout Plan
*Prove object composition (`WorkoutPlan` contains `Exercises`).*
```http
POST /api/workouts/create
{
  "trainerId": "trainer-id",
  "memberId": "jane-id",
  "planName": "Hypertrophy Phase 1",
  "description": "Heavy lifting focus"
}
```

---

### Step 6: Member Marks Attendance
*Prove real-time operational flows.*
```http
POST /api/attendance/checkin
{
  "memberId": "jane-id"
}
```

---

### Step 7: Member Updates Progress
*Feed data into the Strategy engine.*
```http
POST /api/progress/update
{
  "memberId": "jane-id",
  "planId": "plan-id",
  "weight": 85.5,
  "bmi": 26.2,
  "notes": "Feeling strong, gaining weight."
}
```

---

### 🔥 Step 8: POWER FEATURE A - Recommendations (Strategy Pattern)
*Showcase polymoprhic Object-Oriented behavior dynamically deciding business logic based on BMI.*
```http
GET /api/recommendation/{jane-id}
```
*Because the BMI is 26.2, the engine will automatically select `WeightLossStrategy`. Mention this explicitly to the evaluator!*

---

### 🔥 Step 9: POWER FEATURE B - Admin Analytics Dashboard
*Showcase the system's ability to aggregate statistics accurately.*
```http
GET /api/report/dashboard
```
*Shows `totalMembers: 1`, `activeTrainers: ...`, `totalRevenue: 5000.0`, `todayAttendanceCount: 1`.*
