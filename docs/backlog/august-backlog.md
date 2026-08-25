# August Backlog — Digital Banking & Payments Platform

## Purpose

This backlog follows the Week 5 architecture review and prioritizes reliability and domain maturity before distributed infrastructure.

## P0 — Reliability and Financial Correctness

- [ ] Add payment idempotency using a client-provided idempotency key.
- [ ] Define and enforce payment state transitions.
- [ ] Strengthen transaction and audit consistency.
- [ ] Add concurrent transfer/withdrawal integration tests.
- [ ] Verify rollback behavior for every critical transfer failure path.

## P1 — Fraud and Event Reliability

- [ ] Add transaction velocity fraud rules.
- [ ] Persist fraud decisions and rule outcomes where business requirements require auditability.
- [ ] Formalize domain event contracts.
- [ ] Add notification delivery status and retry semantics.
- [ ] Evaluate an outbox pattern for critical events before Kafka adoption.

## P2 — Platform Capabilities

- [ ] Introduce Redis only for justified caching, rate limiting or idempotency use cases.
- [ ] Improve metrics and operational dashboards.
- [ ] Add distributed tracing when the system becomes distributed.
- [ ] Add API rate limiting.
- [ ] Improve authorization checks around account ownership and payment operations.

## P3 — Distributed Architecture

- [ ] Define Kafka event schemas and compatibility rules.
- [ ] Introduce Kafka for durable cross-service events when required.
- [ ] Extract Notification as an independently deployable service.
- [ ] Evaluate Fraud service extraction.
- [ ] Introduce service-to-service authentication.
- [ ] Establish service-owned persistence boundaries.

## Deferred Until Justified

- Kubernetes
- Service mesh
- CQRS
- Event sourcing
- Distributed transactions
- Saga orchestration
- Multiple databases per service

## Priority Principle

Do not introduce infrastructure because it is listed in the technology roadmap. Introduce it when a concrete scalability, reliability, deployment or business requirement justifies the operational complexity.
