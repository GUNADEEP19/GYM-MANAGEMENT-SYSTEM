# Member Lifecycle State Diagram

This diagram models the lifecycle of one core object: **Member**.

## States Included

1. Start (initial)
2. Registered
3. Logged In
4. Active Member
5. Using Services
6. Payment Pending
7. Payment Processing
8. Membership Renewed
9. Inactive Member
10. Suspended (optional)
11. End (final)

## Reality Check (UML Semantics)

This diagram mixes **three different kinds of state**, which is usually marked down in OOAD grading:

1. **Persistent domain state** of `Member` (stored in DB)
2. **Session/authentication state** (logged-in vs logged-out)
3. **Payment workflow state** (pending/processing/success/fail)

In the current implementation:

- `Member` has a persistent `MemberStatus` = `ACTIVE | INACTIVE | SUSPENDED`.
- `Membership` has a persistent `MembershipStatus` = `ACTIVE | EXPIRED | CANCELLED`.
- `Payment` has a persistent `PaymentStatus` = `PENDING | SUCCESS | FAILED`.
- “Logged In” is **not** a stored state of `Member`; it’s derived from whether a request carries a valid JWT.

If you want this UML to be defensible, split this into 2–3 diagrams:

- **Member Status State Machine**: ACTIVE ↔ INACTIVE, ACTIVE ↔ SUSPENDED
- **Membership State Machine**: ACTIVE → EXPIRED, ACTIVE → CANCELLED
- **Payment State Machine**: PENDING → SUCCESS, PENDING → FAILED (optionally FAILED → PENDING on retry)

## Main Transition Flow

1. Start -> Registered (`account created`)
2. Registered -> Logged In (`login request`)
3. Logged In -> Active Member (`login successful`)
4. Active Member -> Using Services (`access services`)
5. Using Services -> Active Member (`continue usage (loop)`)
6. Active Member -> Payment Pending (`membership expires`)
7. Payment Pending -> Payment Processing (`renewal initiated`)
8. Payment Processing -> Membership Renewed (`payment success`)
9. Payment Processing -> Payment Pending (`payment failed`)
10. Membership Renewed -> Active Member (`renewal activated`)

## Optional/Extended Transitions

1. Payment Pending -> Inactive Member (`not renewed in time`)
2. Inactive Member -> Payment Pending (`reactivation request`)
3. Active Member -> Suspended (`policy violation / low attendance`)
4. Suspended -> Active Member (`issue resolved`)
5. Inactive Member -> End (`membership terminated`)

## Payment Status Alignment (with Class Diagram)

Use enum values from `PaymentStatus`:

1. `Payment Pending` corresponds to `PENDING`
2. `Payment Processing -> Membership Renewed` corresponds to `SUCCESS`
3. `Payment Processing -> Payment Pending` corresponds to `FAILED`

This keeps state transitions and class-level payment status consistent.

## UML Notation Used

1. Rounded rectangles for states
2. Filled circle for initial state
3. Double circle for final state
4. Directed arrows with transition labels
