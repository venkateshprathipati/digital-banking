# 🏦 Digital Banking & Payments Platform

> Enterprise-style digital banking backend built with Java, Spring Boot, PostgreSQL, Kafka and Redis, with a focus on correctness, reliability, security and production-oriented backend engineering.

This repository is an incremental implementation of a digital banking and payments platform. It starts with a **modular monolith** to keep financial operations transactional and domain boundaries explicit, while introducing reliable messaging and distributed-systems practices as the platform evolves.

**Repository:** [venkateshprathipati/digital-banking](https://github.com/venkateshprathipati/digital-banking)  
**Working branch:** `dev`  
**Status reviewed:** 12 November 2026

---

## Project status

The project has progressed from core account/payment foundations into **reliability and event-driven processing**. The `dev` branch contains the following implemented areas.

| Area | Status in `dev` | Notes |
|---|---|---|
| Account lifecycle and balance operations | Implemented | Account creation, account state, balance operations and freeze/unfreeze flows |
| Payment transfers | Implemented | Transfer workflow, validation and persistence |
| Transaction history and audit | Implemented | Transaction records and audit/event handling |
| Fraud detection | Implemented | Rule-based fraud decision flow |
| Authentication and authorization | Implemented | Spring Security, JWT and role-based access; public registration is restricted to the customer role |
| Database consistency | Implemented | PostgreSQL, JPA/Hibernate, Flyway and locking/versioning mechanisms |
| Payment idempotency | Implemented | Idempotency persistence and request-hash schema are present |
| Scheduled payments | Implemented in code | Scheduled-payment schema and application code are present; validate scheduler execution in the target environment |
| Transactional outbox | Implemented in code | Outbox schema and publisher/reliability changes are present; delivery/recovery behavior should be validated against a running broker |
| Kafka integration | Implemented in code | Producer/consumer work is represented in branch history; verify broker configuration and end-to-end delivery locally |
| Redis integration | Configured/in use in project | Used for supporting cache/idempotency-related capabilities; PostgreSQL remains authoritative for financial state |
| API documentation | Implemented | Springdoc/OpenAPI documentation is included |
| Environment profiles | Implemented | Base, dev, test and prod YAML configuration files are present |
| Integration testing | Implemented | Testcontainers-backed and integration test classes are present; run the suite before treating all scenarios as passing |
| Production observability | Foundation present | Spring Boot Actuator is configured; dashboards, alerting and distributed tracing remain future hardening |

> **Status wording:** “Implemented” means the relevant code/configuration is present in the branch. It does not claim that production deployment, broker operations, load testing or every test scenario has been independently verified.

---

## Architecture and evolution

The current codebase remains a **modular monolith**. Kafka and the outbox infrastructure introduce asynchronous/event-driven capabilities, but the presence of a broker does not mean the business modules have already been extracted into independently deployable microservices.

### Current high-level view

```text
                 Web / Mobile / API Clients
                           |
                           v
                    Spring Boot REST API
                           |
          +----------------+----------------+
          |                |                |
          v                v                v
      Identity           Account          Payment
      / Security          Module           Module
                           |                |
                           |          +-----+------+
                           |          |            |
                           |          v            v
                           |        Fraud      Scheduled
                           |        Rules       Payments
                           |          |
                           +----------+
                                      |
                                      v
                           Transaction + Audit
                                      |
                                      v
                                PostgreSQL
                         (financial source of truth)
                                      |
                           Transactional Outbox
                                      |
                                      v
                                    Kafka
                                      |
                          +-----------+-----------+
                          |                       |
                          v                       v
                    Event Consumers       Notification Flow

                         Redis
              (supporting cache / fast lookups)
```

### Architectural direction

1. Keep account balances and payment state transactionally consistent in PostgreSQL.
2. Persist business changes and their outbox events within the same database transaction.
3. Publish outbox records asynchronously and make consumers safe against duplicate delivery.
4. Introduce independent services only when ownership, deployment or scaling needs justify extraction.

---

## Implemented capabilities

### 1. Identity, authentication and authorization

- Spring Security integration.
- JWT-based authentication and request authorization.
- Role-based access control.
- Registration flow restricts public self-registration to the customer role.
- Protected REST endpoints and identity-aware business operations.

### 2. Account management

- Open and retrieve accounts.
- Maintain account balances.
- Account status management, including freeze and unfreeze.
- Account state validation before financial operations.
- PostgreSQL-backed persistence.

### 3. Payments and transfers

- Transfer request validation.
- Source and destination account checks.
- Balance debit and credit within a transaction boundary.
- Payment and transaction persistence.
- Failure handling and rollback-oriented tests.
- Payment idempotency support to prevent duplicate processing when a request is retried with the same idempotency identity.

### 4. Fraud detection

- Rule-based fraud evaluation.
- Fraud decision integrated into the payment workflow.
- Extensible rule-oriented design for additional checks.

### 5. Transactions and audit

- Persist transaction history for payment operations.
- Retain business references and transaction status for traceability.
- Separate audit/event records from ordinary application logging.

### 6. Scheduled payments

- Scheduled-payment persistence and migration.
- Scheduled-payment application components are present in the codebase.
- The scheduler should be verified with the active profile, database state and application logs before relying on execution in an environment.

### 7. Reliable event processing: outbox and Kafka

- Outbox database schema is present.
- Outbox reliability fields have been added.
- Outbox publisher and database-locking behavior received a code update merged on 11 November 2026.
- Kafka producer configuration and consumer implementation are represented in the branch history.
- The design supports decoupling event creation from asynchronous publication.

**Important delivery guarantee:** An outbox normally provides at-least-once publication. Consumers must be idempotent because a publisher can successfully send a message and fail before recording that it was sent. Do not describe this as end-to-end exactly-once processing unless the complete transaction, broker and consumer design proves that guarantee.

### 8. Notifications

- Notification/event-handling components are part of the application.
- Notification processing is designed to react to business events rather than own account balances or payment state.
- Verify each consumer group, retry and failure path with a running Kafka broker before claiming operational readiness.

---

## Technology stack

| Concern | Technology |
|---|---|
| Language | Java |
| Framework | Spring Boot |
| Security | Spring Security, JWT |
| Persistence | Spring Data JPA, Hibernate |
| Primary database | PostgreSQL |
| Schema migrations | Flyway |
| Messaging | Apache Kafka |
| Cache / supporting fast data access | Redis |
| API documentation | Springdoc OpenAPI |
| Validation | Jakarta Bean Validation |
| Testing | JUnit, Spring Boot Test, Testcontainers |
| Monitoring foundation | Spring Boot Actuator |
| Build | Maven |

Exact dependency versions are maintained in `pom.xml`.

---

## Repository structure

The application uses package-by-business-domain organization. The main source tree includes:

```text
src/
  main/
    java/com/novalabs/digitalbanking/
      account/
      auth/
      common/
      fraud/
      notification/
      payment/
        controller/
        dto/
        entity/
        event/
        idempotency/
        repository/
        scheduled/
        service/
        validation/
      security/
      transactions/
    resources/
      application.yaml
      application-dev.yaml
      application-test.yaml
      application-prod.yaml
      db/migration/
  test/
    java/com/novalabs/digitalbanking/
```

The exact package contents may evolve as implementation progresses.

---

## Configuration profiles

Profile files are present for:

- `application.yaml` — shared defaults.
- `application-dev.yaml` — local development configuration.
- `application-test.yaml` — test configuration.
- `application-prod.yaml` — production-oriented configuration.

Activate the desired profile explicitly.

### Maven

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### JVM option

```text
-Dspring.profiles.active=dev
```

For a production run, use the `prod` profile and supply secrets, database credentials, Kafka/Redis endpoints and signing keys through environment variables or a secret manager. Do not commit production secrets to the repository.

---

## Prerequisites

Install and configure:

- JDK version compatible with the project POM.
- Maven (or use the Maven wrapper if included).
- PostgreSQL.
- Kafka broker for producer/consumer and outbox delivery flows.
- Redis for features configured to use it.
- Docker, when using Testcontainers or local infrastructure containers.

---

## Run locally

1. Start PostgreSQL and create the application database/user expected by your active profile.
2. Start Kafka and Redis when exercising the related features.
3. Set required environment variables and review the active YAML profile.
4. Run the application:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

5. Check the application logs and health endpoint:

```text
http://localhost:8080/actuator/health
```

The Actuator endpoint is available only when the active profile exposes it and the application starts successfully.

### API documentation

When the application is running and the profile permits Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```

Refer to the `docs/` directory for API and architecture documentation.

---

## Testing

Run the full test suite:

```bash
./mvnw clean test
```

Run a focused test class:

```bash
./mvnw -Dtest=TransferServiceRollbackTest test
```

The repository includes integration-test coverage for authentication/authorization, payment transfer/history, rollback behavior and account cache consistency. Testcontainers-based tests require a working Docker environment.

**Before marking a scenario complete**, run the relevant test and record the result. Source files alone do not establish that the test currently passes.

---

## Financial correctness and reliability principles

- Keep PostgreSQL as the source of truth for financial balances and payment state.
- Use database transactions for atomic debit/credit and related persistence.
- Use optimistic or pessimistic locking according to the contention and consistency needs of the operation.
- Acquire multiple account locks in a deterministic order to reduce deadlock risk.
- Use idempotency keys for retryable payment requests.
- Persist outbox events in the same transaction as the business change.
- Expect duplicate message delivery and make consumers idempotent.
- Define bounded retries and a dead-letter/recovery strategy for failed event processing.
- Never log passwords, JWTs, signing secrets or unnecessary personal/financial data.

---

## Current engineering focus

The latest merged implementation milestone on the `dev` branch is the **outbox publisher and database-locking improvement** (11 November 2026).

Suggested next verification and hardening steps:

1. Run `./mvnw clean test` and capture the actual test result.
2. Start two application instances against the same PostgreSQL database and verify competing outbox workers using row-level locking / `SKIP LOCKED`.
3. Verify outbox retry, backoff, stale-event recovery and duplicate publication behavior.
4. Verify Kafka producer delivery and consumer-group behavior with multiple consumers/instances.
5. Verify scheduled payments across due-time, retry, failure and duplicate-execution scenarios.
6. Verify payment idempotency under concurrent duplicate requests.
7. Validate Actuator health/metrics exposure for each profile.
8. Add dashboards, alerting, distributed tracing and operational runbooks.

These are verification/hardening items, not claims that every item is already complete.

---

## Planned evolution

The following are future architecture and operational enhancements rather than completed microservice extraction:

- Extract selected domains into independently deployable services when justified.
- Introduce an API Gateway and service-to-service security for a distributed deployment.
- Add service discovery/configuration management if deployment topology requires it.
- Add distributed tracing, centralized logs, dashboards and alerting.
- Define event schema compatibility and versioning policies.
- Add load, soak, failure-injection and recovery testing.
- Establish CI/CD deployment and rollback strategies.

---

## Learning outcomes demonstrated

This project is intended to provide hands-on practice with:

- Java and Spring Boot backend engineering.
- Domain-oriented modular design.
- REST API and security implementation.
- Relational modelling and schema evolution.
- Transaction isolation, locking and rollback.
- Idempotent payment APIs.
- Transactional outbox and asynchronous messaging.
- Kafka producer/consumer fundamentals.
- Scheduled and retryable business workflows.
- Integration testing with real infrastructure.
- Observability and production-readiness practices.

---

## Documentation

See the `docs/` directory for detailed API, architecture and backlog notes. The README is the high-level entry point; detailed endpoint contracts and implementation decisions should live alongside their respective documentation.

---

## Project links

- [Repository](https://github.com/venkateshprathipati/digital-banking)
- [Development branch](https://github.com/venkateshprathipati/digital-banking/tree/dev)
- [Documentation](https://github.com/venkateshprathipati/digital-banking/tree/dev/docs)

---

## Disclaimer

This is an engineering and learning project. It is not a licensed banking system and must not be used to process real customer funds or sensitive production financial data.
