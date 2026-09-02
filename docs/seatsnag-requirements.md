# SeatSnag — Requirements & Acceptance Criteria

A concurrency-safe event ticket reservation system, built to demonstrate optimistic
locking, hold expiry, idempotent payment confirmation, and a real AWS deployment.

---

## 0. Locked Tech Decisions

Don't relitigate these mid-build — they're chosen on purpose:

- **Language/Framework:** Java + Spring Boot (Web, Data JPA, Validation)
- **Database:** PostgreSQL (local via Docker Compose, RDS in AWS)
- **Cache/Locks:** Redis (introduced in Phase 4)
- **Architecture:** Modular monolith — packages `booking`, `payment`,
  `notification`, `event`. No microservices split.
- **Migrations:** Flyway
- **Testing:** JUnit 5 + Testcontainers (real Postgres in tests, not H2)
- **Load testing:** k6 or Gatling (Phase 6)
- **Deployment:** Docker → ECR → ECS Fargate → RDS (+ ElastiCache from Phase 4)

---

## 1. Domain Model

| Entity | Key fields |
|---|---|
| `Event` | id, name, venue, startTime |
| `Seat` | id, eventId, section, row, seatNumber, status (`AVAILABLE`/`HELD`/`BOOKED`), version |
| `Reservation` | id, seatId, userId, status (`PENDING`/`CONFIRMED`/`EXPIRED`/`CANCELLED`), createdAt, expiresAt |
| `Payment` | id, reservationId, idempotencyKey, status (`PENDING`/`SUCCEEDED`/`FAILED`), amount |

---

## Phase 1 — Core CRUD & Domain Setup

**Goal:** boring, correct baseline. No concurrency handling yet — that's intentional.

**Requirements**
- Endpoints: `POST /events`, `GET /events/{id}`, `GET /events/{id}/seats`,
  `POST /reservations`, `GET /reservations/{id}`, `DELETE /reservations/{id}`
- Seats are seeded for an event (e.g. via a migration or admin endpoint)
- A reservation on an `AVAILABLE` seat sets it to `HELD` and creates a `PENDING` reservation

**Acceptance Criteria**
- [x] Given an event with seats, `GET /events/{id}/seats` returns all seats with current status
- [x] Given an `AVAILABLE` seat, `POST /reservations` succeeds and the seat becomes `HELD`
- [x] Given a seat that is already `HELD` or `BOOKED`, `POST /reservations` returns 409 Conflict
- [x] Given a `PENDING` reservation, `DELETE /reservations/{id}` releases the seat back to `AVAILABLE`
- [ ] All entities and relationships are covered by Flyway migrations, not `ddl-auto`

**Definition of Done:** you can walk through the full happy path via Postman/curl and the DB state is always consistent — but you have *not yet* tested concurrent requests.

---

## Phase 2 — Prove the Race Condition

**Goal:** demonstrate the bug before fixing it. This phase produces no new features — just a failing test.

**Requirements**
- A test that fires N concurrent `POST /reservations` requests at the *same* seat
  (e.g. 20 threads, one seat)

**Acceptance Criteria**
- [ ] The test fires all requests near-simultaneously (use a `CountDownLatch` or
  `ExecutorService` + barrier — not a sequential loop)
- [ ] Without any locking, the test demonstrates **more than one** reservation
  succeeding for the same seat (i.e. it currently fails/oversells)
- [ ] The failure is captured in a comment or test report — this is your baseline evidence

**Definition of Done:** you have a red test proving the oversell, committed to the repo before the fix.

---

## Phase 3 — Fix with Optimistic Locking

**Goal:** the same test from Phase 2 now passes.

**Requirements**
- Add `@Version` to `Seat`
- Reservation logic must catch `OptimisticLockException` and translate it to a 409 response
- Losing requests should NOT retry automatically server-side — return 409 and let the client decide

**Acceptance Criteria**
- [ ] Given the Phase 2 concurrency test, exactly **one** request succeeds and all others receive 409
- [ ] A losing request's seat status is left untouched (still `AVAILABLE`, not corrupted)
- [ ] Response body on 409 clearly indicates "seat no longer available" (not a generic error)
- [ ] Test suite includes both a single-threaded happy path AND the concurrent test

**Definition of Done:** the race condition test from Phase 2 is now green, and you can explain *why* in one sentence to someone else.

---

## Phase 4 — Hold Expiry

**Goal:** a `HELD` seat with no payment within N minutes returns to `AVAILABLE`.

**Requirements**
- Configurable hold duration (e.g. 10 minutes), stored as `expiresAt` on `Reservation`
- A mechanism to expire holds — pick ONE and justify the choice in the README:
  - Scheduled sweep (`@Scheduled` job querying for expired `PENDING` reservations), or
  - Redis key with TTL + keyspace notification listener
- Introduce Redis into the stack (via Docker Compose locally)

**Acceptance Criteria**
- [ ] A reservation created now has `expiresAt` set correctly based on config
- [ ] After the hold window passes, the reservation's status becomes `EXPIRED` and the
  seat's status returns to `AVAILABLE`, without any manual trigger
- [ ] A `POST /payments` against an already-`EXPIRED` reservation returns 409, not a success
- [ ] Expiry mechanism is tested with a shortened TTL in the test profile (e.g. 2 seconds), not by waiting 10 real minutes

**Definition of Done:** you can hold a seat, wait past expiry (in test time), and confirm it's bookable by someone else again.

---

## Phase 5 — Idempotent Payment Confirmation

**Goal:** the same payment request, sent twice, only ever confirms once.

**Requirements**
- `POST /payments` requires an `Idempotency-Key` header
- Store idempotency keys with their result so repeated requests return the original response, not reprocess
- Successful payment transitions `Reservation` to `CONFIRMED` and `Seat` to `BOOKED`, atomically

**Acceptance Criteria**
- [ ] Given a fresh idempotency key, `POST /payments` confirms the reservation and books the seat
- [ ] Given the SAME idempotency key sent again (simulating a retried webhook), the response
  is identical to the first call, and the reservation is NOT double-processed
- [ ] Given a DIFFERENT idempotency key against an already-`CONFIRMED` reservation, the request
  is rejected (can't pay for something already paid)
- [ ] Seat status and reservation status update in a single transaction — no window where one
  is updated and the other isn't

**Definition of Done:** a test that fires the same payment request twice concurrently results in exactly one confirmation, verified by asserting on both `Payment` and `Reservation` state.

---

## Phase 6 — Load Testing & Rate Limiting

**Goal:** know your system's actual limits, not guessed ones.

**Requirements**
- A k6 or Gatling script simulating a "flash sale": many virtual users hitting
  `GET /events/{id}/seats` and `POST /reservations` against the same event
- Rate limiting on `POST /reservations` per user (e.g. token bucket, via Bucket4j or
  API Gateway if you front it with one)

**Acceptance Criteria**
- [ ] Load test report captures p50/p95/p99 latency and error rate under a defined
  concurrent user count (pick a number and document it, e.g. 200 VUs)
- [ ] Given a user exceeding the rate limit, subsequent requests return 429, not 500 or a timeout
- [ ] System correctly handles the load test with **zero** overselling (cross-check with Phase 2/3 test)
- [ ] Results and config (VU count, ramp-up, duration) are documented in the README

**Definition of Done:** you have a load test report you could show someone and explain what it proves.

---

## Phase 7 — AWS Deployment

**Goal:** the whole thing runs in AWS, deployed via CI/CD, not clicked together manually.

**Requirements**
- Dockerfile (multi-stage build)
- Infrastructure defined as code (Terraform or CDK) — not manual console setup
- ECS Fargate service behind an Application Load Balancer
- RDS Postgres in a private subnet, reachable only from the ECS task's security group
- ElastiCache Redis for hold/lock state
- GitHub Actions: run tests → build & push image to ECR → deploy to ECS on merge to main
- Basic CloudWatch dashboard: request latency, error rate, oversell-attempts-blocked (custom metric)

**Acceptance Criteria**
- [ ] `terraform apply` (or `cdk deploy`) provisions the full stack from scratch with no manual console steps
- [ ] A push to `main` results in an automatic deployment, visible in the ECS service's running task
- [ ] The deployed API is reachable via the ALB's DNS name and passes a smoke test
  (create event → reserve seat → confirm payment)
- [ ] RDS is NOT publicly accessible (verify security group rules)
- [ ] CloudWatch shows at least one custom metric beyond default ECS metrics
- [ ] README documents how to tear the whole stack down (`terraform destroy`) — cost hygiene matters

**Definition of Done:** you can point someone at a live URL, and the deploy pipeline is the only way changes reach it.

---

## Non-Functional Requirements (apply throughout)

- No `System.out.println` — use structured logging (SLF4J)
- No secrets committed to the repo — use environment variables / AWS Secrets Manager
- Every phase's new behavior has a corresponding automated test — no manual-only verification
- README stays current: setup instructions, architectural decisions, and *why* (not just *what*)
