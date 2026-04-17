# Gym Management System - Demo Data Guide

Since it is difficult to show the full functionality of the system with an empty database during evaluations, the backend is configured with a **`DemoDataSeeder`**. 

## How It Works
When you start the Spring Boot Application via `./mvnw spring-boot:run` or your IDE, the system automatically detects if the database is empty. If it is, it automatically populates the database with:
- **2 Gym Packages** (Basic & Premium)
- **3 Trainers**
- **3 Members** (With active memberships and logged payments)
- **3 Dynamic Workout Plans** (containing 4 core exercises each, assigned directly by the trainers)
- **15 Attendance Records** (simulating 5 recent check-ins for each member)
- **9 Progress Records** (simulating month-over-month weight loss)

<br>

## Credentials To Use When Demoing
Use these credentials in your `/login` Postman/Swagger requests to get JWT tokens and explore the system immediately. The password for **all** generated demo accounts is **`password123`**.

### 🛠️ Administrators (From AdminSeeder)
*   **Email:** `admin@gym.com`
*   **Password:** `admin123` *(Note the different password here!)*

### 🏋️ Trainers
*   **Trainer 1 Email:** `trainer1@gym.com` | **Password:** `password123`
*   **Trainer 2 Email:** `trainer2@gym.com` | **Password:** `password123`
*   **Trainer 3 Email:** `trainer3@gym.com` | **Password:** `password123`

### 🏃‍♂️ Members
*   **Member 1 Email:** `member1@gym.com` | **Password:** `password123` *(Assigned to Trainer 1, Weight Loss Plan)*
*   **Member 2 Email:** `member2@gym.com` | **Password:** `password123` *(Assigned to Trainer 2, Hypertrophy Plan)*
*   **Member 3 Email:** `member3@gym.com` | **Password:** `password123` *(Assigned to Trainer 3, Powerbuilding Plan)*

<br>

## What else do I need to do?
**Nothing.** 
You do not need to hit the "Register" endpoint before showing off the features. Pick an email from above, generate a JWT token via `/api/auth/login`, and use it in your Authorization header to demo Attendance, Progress, and Workout Plan fetching immediately.
