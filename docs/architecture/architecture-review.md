# Architecture Review — Week 5 Sprint Review

## Status

- Branch: `dev`
- Sprint: Week 5 — Transaction & Fraud Foundations
- Review focus: Architecture Review and stabilization
- Architecture: Modular Monolith evolving toward Microservices

## 1. Current Architecture

The application is currently a modular monolith. Business capabilities are separated by domain/module while sharing the Spring Boot runtime and transactional PostgreSQL persistence model.

```text
Client
  |
  v
Spring Web Controllers
  |
  +-------------------+-------------------+-------------------+
  |                   |                   |                   |
  v                   v                   v                   v
Account             Payment             Fraud           Notification
Module              Module              Module              Module
  |                   |                   |                   ^
  |                   |                   |                   |
  +-------------------+-------------------+                   |
                      |                                       |
                      v                                       |
                PostgreSQL                         Spring Application Events

Auth/Security and Common cross-cutting concerns support the modules.
```

## 2. Module Responsibilities

### Account

Owns account state and account lifecycle operations:

- account creation
- account number generation
- balance management
- freeze/unfreeze
- account status validation

### Payment

Owns payment orchestration and transfer workflow:

- transfer request validation
- source/destination account coordination
- payment persistence
- transaction orchestration
- invocation of fraud evaluation

### Fraud

Owns fraud/risk decisions.

The current design uses a `FraudRule` abstraction with independent rules evaluated by `FraudEngine`.

### Transaction / Audit

Owns financial history and audit information produced by business operations.

### Notification

Consumes application/domain events and performs notification-side processing. It must not own payment or account business state.

### Auth / Security

Owns authentication, authorization, JWT handling and role-based access decisions.

## 3. Dependency Rules

The following rules apply going forward:

1. Controllers handle transport concerns only.
2. Services own business orchestration and transaction boundaries.
3. Repositories own persistence access.
4. Account owns account state.
5. Payment owns payment orchestration.
6. Fraud owns fraud decisions.
7. Notification reacts to events and must not manipulate payment/account repositories directly.
8. Cross-module repository access should be minimized and reviewed explicitly.
9. Domain/application events should represent business facts, not notification implementation details.
10. Infrastructure technologies are introduced only when a real requirement justifies them.

## 4. Financial Transaction Boundary

The critical transfer flow is designed as a single database transaction:

```text
Transfer request
     |
     v
Validate
     |
     v
Load accounts
     |
     v
Fraud evaluation
     |
     v
Debit source
     |
     v
Credit destination
     |
     v
Persist payment / transaction / audit state
     |
     v
COMMIT
```

If any critical operation fails, the transaction must roll back.

External network calls must not be performed while holding critical database row locks.

## 5. Concurrency Strategy

Account state changes use both optimistic and pessimistic concurrency techniques where appropriate.

- Optimistic locking is useful for lower-contention state updates.
- Pessimistic write locking is appropriate for sensitive balance operations where concurrent mutations must be serialized.

The transaction scope must remain short to avoid unnecessary lock contention.

## 6. Fraud Flow

```text
Payment transfer
      |
      v
FraudEngine
      |
      +----> LargeAmountRule
      |
      +----> BlockedAccountRule
      |
      +----> Future rules
      |
      v
PASS / BLOCK
```

A blocked fraud decision must prevent financial mutation from continuing.

## 7. Event Flow

The current modular-monolith design uses Spring application events for in-process event communication.

```text
Payment transaction
      |
      v
ApplicationEventPublisher
      |
      +------------------------------+
                                     |
                                     v
                         TransactionalEventListener
                              AFTER_COMMIT
                                     |
                                     v
                              Notification
```

This is intentionally kept as an in-process mechanism at the current project stage.

Kafka is a future option when durable messaging, replay, independent deployment, cross-service communication or consumer scaling becomes a real requirement.

## 8. Why Kafka Is Deferred

Adding Kafka now would increase operational complexity without providing a necessary capability for the current single-process modular-monolith workflow.

When Kafka is introduced, the design must also address:

- event schemas
- idempotent consumers
- retries
- dead-letter topics
- partitioning
- ordering
- producer reliability
- observability
- replay

The current application-event boundary keeps the codebase simpler while preserving the domain-event direction.

## 9. Security Boundaries

Authentication and authorization are separate concerns.

A valid JWT must not automatically imply permission to perform a financial operation.

Payment authorization should verify both:

- authenticated identity/role
- authorization to operate on the source account

Sensitive credentials, tokens and unnecessary personal data must never be logged.

## 10. Persistence Rules

PostgreSQL remains the source of truth for financial state.

Flyway remains the source of truth for schema evolution.

Indexes must be based on actual query patterns, especially for:

- account number
- customer ID
- payment reference
- transaction history access

Financial balances must never depend on cache consistency.

## 11. Observability

Current and near-term operational signals should include:

- HTTP request count and latency
- error rate
- database connection pool usage
- payment success/failure count
- fraud rejection count
- notification processing count
- correlation ID / trace correlation

Actuator can provide the initial application metrics foundation.

## 12. Testing Strategy

### Unit tests

- Fraud rules
- Validators
- Factories
- Pure business logic

### Integration tests

- Repository behavior
- PostgreSQL schema
- transaction boundaries
- rollback behavior

### Business-flow tests

- successful transfer
- insufficient balance
- fraud rejection
- frozen account
- concurrent balance mutation
- transaction rollback
- notification event behavior

## 13. Architecture Decisions

### ADR-001 — Modular Monolith First

**Decision:** Keep the application as a modular monolith for the current stage.

**Reason:** Financial operations benefit from shared transactional boundaries while the domain model is still evolving. Domain modules remain explicit so they can be extracted later.

**Status:** Accepted

### ADR-002 — In-Process Events First

**Decision:** Use Spring application events for current in-process event communication.

**Reason:** The current deployment is a single application and does not yet require durable cross-service messaging.

**Status:** Accepted

### ADR-003 — Defer Kafka

**Decision:** Introduce Kafka only when durable/event-driven distributed communication is required.

**Reason:** Kafka adds operational and distributed-systems complexity that is not yet required by the current deployment model.

**Status:** Accepted / Revisit when services are extracted

## 14. Technical Debt / Improvement Areas

- Strengthen idempotency for payment operations.
- Formalize payment state transitions.
- Strengthen transaction/audit consistency.
- Add more concurrency-focused tests.
- Improve event contract documentation.
- Add notification delivery state and retry semantics later.
- Establish stronger architecture tests if module coupling grows.
- Evaluate an outbox pattern before introducing Kafka for critical events.

## 15. Microservice Extraction Candidates

Likely early candidates:

1. Notification — event consumer with relatively loose coupling.
2. Fraud — independent decision capability with a clear interface.

Payment and Account should be extracted later because financial consistency and transactional coupling require more careful decomposition.

## 16. Review Conclusion

The current implementation has a solid modular-monolith foundation. The immediate engineering priority is to protect the existing domain boundaries, financial transaction correctness and event semantics rather than adding infrastructure prematurely.

The next reliability milestone should focus on:

```text
Idempotency
   -> Payment state machine
   -> Retry semantics
   -> Transaction/audit consistency
   -> Outbox evaluation
   -> Kafka when justified
```
