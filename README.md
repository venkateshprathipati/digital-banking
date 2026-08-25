# 🏦 Digital Banking & Payments Platform

> Enterprise-grade Digital Banking & Payments Platform built with Java, Spring Boot, PostgreSQL, and modern backend engineering practices.

A production-oriented backend project designed to demonstrate how a real financial platform can evolve from a **Modular Monolith** into a **Microservices-based distributed system**.

The project focuses on:

- Financial transaction correctness
- Account and balance management
- Payment processing
- Fraud detection
- Transaction and audit management
- Event-driven architecture
- Authentication and authorization
- Concurrency control
- Database consistency
- Observability
- Reliability
- Scalability
- Microservice evolution

---

# 📌 Project Status

**Current Stage: Week 5 — Transaction & Fraud Foundations**

The project is currently implemented as a **Modular Monolith**.

The architecture is intentionally designed so that individual business modules can later be extracted into independent microservices.

### Currently implemented

- Account Management
- Account Balance Management
- Account Freeze / Unfreeze
- Payment Transfer
- Payment Validation
- Transaction Management
- Audit Logging
- Fraud Detection
- Rule-based Fraud Engine
- Payment Success Events
- Payment Failure Events
- Payment Rejection Events
- Fraud Detection Events
- Notification Event Handling
- Transaction Rollback Handling
- Optimistic Locking
- Pessimistic Locking
- Spring Security
- **JWT** Authentication
- PostgreSQL
- **JPA** / Hibernate
- Flyway Database Migration
- Bean Validation
- Global Exception Handling
- **API** Response Standardization
- Integration Testing
- Testcontainers
- Spring Boot Actuator

---

# 🎯 Project Objective

The goal of this project is not simply to build **CRUD** APIs.

The objective is to design a realistic enterprise financial backend and demonstrate the engineering decisions required to build systems that handle:

High-value transactions Concurrent requests Financial consistency Fraud detection Security Failure handling Auditability Event-driven processing Scalability Production operations

The project is being developed incrementally, following the evolution of a real enterprise backend.

🏗️ Architecture Evolution

The system follows this evolution:

    **CURRENT**
    │
    ▼
    ┌─────────────────┐
    │ Modular Monolith │
    └────────┬────────┘
    │
    ▼
    Clear Domain Boundaries
    │
    ▼
    Stable Business APIs
    │
    ▼
    Domain Events
    │
    ▼
    Durable Event Infrastructure
    │
    ▼
    Selective Microservice
    Extraction
    │
    ▼
    Independent Microservices

The project intentionally avoids prematurely introducing distributed-system complexity.

🏛️ Current Architecture
┌─────────────────────┐
│       Clients       │
│                     │
│ Web / Mobile / **API**  │
└──────────┬──────────┘
│
▼
┌─────────────────────┐
│    Spring Boot      │
│   **REST** Controllers  │
└──────────┬──────────┘
│
┌─────────────────────┼─────────────────────┐
│                     │                     │
▼                     ▼                     ▼
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   Account   │       │   Payment   │       │    Auth     │
│   Module    │       │   Module    │       │  /Security  │
└──────┬──────┘       └──────┬──────┘       └─────────────┘
│                     │
│                     ▼
│              ┌─────────────┐
│              │ FraudEngine │
│              └──────┬──────┘
│                     │
│                     ▼
│              ┌─────────────┐
│              │ Fraud Rules │
│              └─────────────┘
│
▼
┌─────────────────────────────────────┐
│             PostgreSQL              │
│      Financial Source of Truth      │
└─────────────────────────────────────┘

Payment / Fraud Business Events
│
▼
Spring Application Events
│
▼
@TransactionalEventListener
AFTER_COMMIT
│
▼
Notification Module
🧩 Domain Modules

The application is organized around business capabilities rather than technical layers.

com.novalabs.digitalbanking │ ├── auth ├── account ├── payment ├── transaction ├── audit ├── fraud ├── notification └── common 🔐 Auth / Security Module

Responsible for:

Authentication **JWT** generation **JWT** validation Authorization Role-based access control Securing **REST** endpoints User identity

Security is treated as a separate concern from business logic.

🏦 Account Module

The Account module owns account state.

Responsibilities Open account Generate account number Maintain account balance Deposit Withdraw Freeze account Unfreeze account Validate account status Retrieve account information ### Account State

Example:

**ACTIVE** **FROZEN** **CLOSED** 💳 Payment Module

The Payment module owns payment orchestration.

Responsibilities Initiate transfer Validate transfer request Validate source account Validate destination account Check account status Invoke fraud detection Debit source account Credit destination account Persist payment Publish payment events Handle payment failure Handle payment rejection 🛡️ Fraud Detection Module

Fraud detection is implemented using a rule-based architecture.

    ┌──────────────┐
    │ FraudEngine  │
    └───────┬──────┘
    │
    ┌───────────────┼────────────────┐
    │               │                │
    ▼               ▼                ▼
┌────────────────┐ ┌────────────────┐ ┌───────────────┐
│ LargeAmountRule│ │BlockedAccount  │ │ Future Rules  │
│                │ │Rule            │ │               │
└────────────────┘ └────────────────┘ └───────────────┘
│               │                │
└───────────────┼────────────────┘
│
▼
Fraud Decision
│
┌───────┴───────┐
▼               ▼
**PASS**            **BLOCK**

The system uses a FraudRule abstraction.

This follows the Strategy Pattern and allows new rules to be introduced without modifying a large centralized conditional block.

💰 Transaction Module

The Transaction module provides financial transaction history and transaction tracking.

A payment operation can produce transaction information such as:

Transaction ID ### Payment Reference ### Source Account ### Destination Account Amount ### Transaction Type ### Transaction Status Timestamp

The transaction history is important for:

Customer history Reconciliation Audit Fraud investigation Operational troubleshooting 📝 Audit Module

Audit information provides traceability for important financial operations.

The audit layer helps answer:

Who performed the operation? What operation was performed? When did it happen? What was the result? What business reference was involved?

Audit data should be treated differently from normal application logs.

📨 Notification Module

The Notification module reacts to business events.

Current events include:

PaymentCompletedEvent PaymentFailedEvent PaymentRejectedEvent FraudDetectedEvent

The notification module does not own financial state.

It reacts to events produced by business modules.

🔄 Payment Transfer Flow

A typical transfer request follows this flow:

**POST** /api/v1/transfers
│
▼
Payment Controller
│
▼
Transfer Service
│
├── Validate request
│
├── Load source account
│
├── Load destination account
│
├── Validate account state
│
▼
FraudEngine
│
├── Large Amount Rule
├── Blocked Account Rule
└── Future Rules
│
▼
Fraud Decision
│
┌────┴────┐
│         │
**BLOCK**     **PASS**
│         │
▼         ▼
Reject     Debit Source
│
▼
Credit Destination
│
▼
Save Payment
│
▼
Save Transaction
│
▼
Save Audit
│
▼
Publish Event
│
▼
**COMMIT**
│
▼
Notification
💵 Financial Transaction Boundary

Financial operations must be executed within a controlled database transaction.

Conceptually:

@Transactional
│
▼
Validate
│
▼
### Fraud Check
    │
    ▼
Debit
│
▼
Credit
│
▼
### Persist Payment
    │
    ▼
### Persist Transaction
    │
    ▼
### Persist Audit
    │
    ▼
**COMMIT**

If a critical operation fails:

Exception
│
▼
**ROLLBACK**

This prevents partially completed financial operations.

🔒 Concurrency Control

Financial systems must correctly handle concurrent requests.

Example:

Initial Balance = ₹10,**000**

Request A → Withdraw ₹7,**000** Request B → Withdraw ₹6,**000**

Both requests must not independently read:

₹10,**000**

and then successfully withdraw.

The project uses database concurrency mechanisms including:

### Optimistic Locking

Used when conflicts are possible but relatively infrequent.

@Version

Hibernate uses the version value to detect concurrent modifications.

### Pessimistic Locking

Used for critical balance operations where concurrent modifications must be serialized.

Conceptually:

**SELECT** * **FROM** accounts **WHERE** id = ? **FOR** **UPDATE**;

This prevents another transaction from modifying the locked row until the current transaction completes.

📨 Event-Driven Architecture

The current modular monolith uses Spring Application Events for in-process communication.

### Business Operation

    │
    ▼
ApplicationEventPublisher
│
▼
### Spring Application Event
    │
    ▼
@TransactionalEventListener
│
▼
AFTER_COMMIT
│
▼
Notification
Why AFTER_COMMIT?

Consider:

Payment
│
▼
### Send Notification
    │
    ▼
### Database Rollback

The customer may receive:

### Payment Successful

even though the payment was rolled back.

This is incorrect.

With:

Payment
│
▼
### Database Commit
    │
    ▼
AFTER_COMMIT Event
│
▼
Notification

the notification represents committed business state.

🧠 Domain Events

Events should represent business facts.

Good:

PaymentCompleted PaymentFailed PaymentRejected FraudDetected

Avoid tightly coupling event names to infrastructure.

For example, prefer:

PaymentCompleted

over:

SendPaymentCompletedNotification

The first describes what happened.

The second describes what a consumer wants to do.

This separation becomes important when the application eventually moves to Kafka.

📨 Kafka Strategy

Kafka is part of the future distributed architecture.

It is intentionally not introduced simply because Kafka is popular.

Kafka becomes valuable when we require:

Durable events Cross-service communication Independent consumers Event replay Consumer scaling Independent deployment High-volume asynchronous processing

Before introducing Kafka for financial events, the project should evaluate:

### Outbox Pattern

    │
    ▼
Kafka
│
├── Retry
├── Dead Letter Topic
├── Idempotent Consumer
├── Partitioning
├── Ordering
└── Schema Management
📦 Outbox Pattern — Future

For critical business events, direct event publication can eventually create a reliability gap:

Database **COMMIT**
│
├── Success
│
└── Event publishing fails

The Outbox Pattern can solve this by storing the business event in the same database transaction.

### Business Transaction

    │
    ├── Update Financial Data
    │
    └── Insert Outbox Event
    │
    ▼
    **COMMIT**
    │
    ▼
    Outbox Publisher
    │
    ▼
    Kafka

This will be evaluated before introducing Kafka for critical financial events.

🗄️ Database Architecture

PostgreSQL is currently the primary database.

### Spring Boot

    │
    ▼
Spring Data **JPA**
│
▼
Hibernate
│
▼
PostgreSQL

PostgreSQL is the source of truth for financial state.

🛠️ Database Migrations

Flyway manages database schema evolution.

Application
│
▼
Flyway
│
▼
Versioned **SQL** Migration
│
▼
PostgreSQL

Schema changes should be committed as versioned migrations.

Avoid manually modifying production database schemas.

🔐 Security Architecture

Authentication and authorization are different responsibilities.

Request
│
▼
**JWT** Authentication
│
▼
### User Identity
    │
    ▼
Role / Permission
│
▼
### Account Ownership
    │
    ▼
### Business Operation

A valid **JWT** does not automatically mean that the user is authorized to transfer money from an account.

🔒 Security Principles

Never log:

Passwords **JWT** Tokens ### Authorization Headers ### Refresh Tokens ### Sensitive Credentials ### Unnecessary Personal Information

Prefer structured logging:

correlationId paymentReference accountId eventType status duration 📊 **API** Response Strategy

The **API** follows a standardized response/error model.

Successful responses should provide consistent structure.

Errors should provide useful information such as:

timestamp status error errorCode message path correlationId

Example:

{
*timestamp*: ***2026**-08-**20T11**:32:51*,
*status*: **422**,
*error*: *Unprocessable Entity*,
*errorCode*: *BUS_001*,
*message*: "LARGE_AMOUNT : Transaction amount exceeds fraud threshold*,
*path*: */api/v1/transfers*,
*correlationId*: *..."
}
🧪 Testing Strategy

The project follows a layered testing strategy.

### Unit Tests

Used for isolated business logic.

Examples:

### Fraud Rules

Validators Factories ### Utility Logic ### Repository Tests

Validate:

Database queries **JPA** mappings Indexes Locking behavior ### Integration Tests

Validate:

### Spring Context

PostgreSQL Transactions Rollback Persistence Security

Testcontainers is used to provide realistic infrastructure during integration tests.

### Business Flow Tests

Important scenarios include:

### Successful Transfer

### Insufficient Balance ### Frozen Account ### Fraud Rejection ### Payment Failure ### Transaction Rollback ### Concurrent Withdrawal ### Concurrent Transfer ### Notification Event

The goal is to test business behavior rather than only code coverage.

📈 Observability

Spring Boot Actuator provides the foundation for application monitoring.

Important metrics include:

**HTTP** Request Count **HTTP** Request Latency **HTTP** Error Rate

### Database Connection Pool

### Database Query Performance

### Payment Success Count

### Payment Failure Count

### Fraud Rejection Count

### Notification Processing Count

Future distributed architecture will introduce:

OpenTelemetry Prometheus Grafana ### Distributed Tracing ### Centralized Logging 🧱 Architecture Principles

The project follows these architectural rules.

## Controllers

Controllers handle transport concerns only.

**HTTP** Validation **DTO** Mapping Response

Business logic belongs in services/domain components.

## Services

Services own business orchestration and transaction boundaries.

## Repositories

Repositories own persistence access.

Repositories should not contain business workflows.

## Module Ownership

Account
↓
### Account State

Payment
↓
### Payment Workflow

Fraud
↓
### Fraud Decision

Transaction
↓
### Financial Transaction History

Audit
↓
### Audit Trail

Notification
↓
### Notification Processing
## Cross-Module Communication

Avoid unnecessary direct access to another module's internal repositories or entities.

Prefer:

### Application Service

        ↓
Domain/Application Contract
↓
### Other Module

and eventually:

Service ↓ **API** / Event ↓ ### Other Microservice 🏛️ Architecture Decisions **ADR**-**001** — Modular Monolith First Decision

Start with a Modular Monolith.

Reason

The business domains and transaction boundaries are still evolving.

A modular monolith provides:

Strong consistency Simple deployment Simple debugging Lower operational overhead Shared database transactions

while still allowing clear business boundaries.

Status

Accepted.

**ADR**-**002** — Package by Business Domain Decision

Organize code by business capability.

account payment fraud notification transaction audit auth Reason

This makes ownership and future service extraction clearer.

Status

Accepted.

**ADR**-**003** — Spring Events Before Kafka Decision

Use Spring Application Events for current in-process event communication.

Reason

The application is currently a single deployable unit.

Kafka would add unnecessary operational complexity at this stage.

Status

Accepted.

**ADR**-**004** — Kafka When Justified Decision

Introduce Kafka when there is a concrete requirement for durable distributed messaging.

Requirements

Examples:

Independent service deployment Durable events Event replay Multiple consumers High-volume asynchronous processing Cross-service communication Status

Deferred.

**ADR**-**005** — PostgreSQL as Financial Source of Truth Decision

PostgreSQL remains the authoritative source for financial balances and transactional state.

Reason

Financial correctness requires strong consistency and transactional guarantees.

Caches such as Redis must never become the authoritative balance source.

Status

Accepted.

🚀 Future Microservice Evolution

The project will not be split into microservices all at once.

The expected evolution is:

    Modular Monolith
    │
    ▼
    Clear Module Boundaries
    │
    ▼
    Stable Business Contracts
    │
    ▼
    Domain Events
    │
    ▼
    Durable Messaging
    │
    ▼
    Selective Module Extraction
    │
    ┌─────────────┴─────────────┐
    ▼                           ▼
    Notification Service        Fraud Service
    │                           │
    └─────────────┬─────────────┘
    ▼
    Independent Services
🎯 Potential Service Extraction Order

A possible extraction strategy:

## Notification

2. Fraud
## Payment
## Account

This is not a fixed rule.

Actual extraction order will depend on:

Business boundaries Operational requirements Scaling requirements Data ownership Deployment requirements Transactional coupling 📋 Current Technical Debt

The next reliability improvements include:

Payment idempotency
Payment state machine
Retry semantics
Stronger transaction/audit consistency
Concurrent transaction testing
Formal event contracts
Notification retry
Notification delivery status
Better authorization
Better observability
Architecture dependency checks
🗓️ August Development Backlog
Phase 1 — Payment Reliability
Idempotency
↓
### Payment State Machine
     ↓
### Retry Semantics
     ↓
### Failure Handling
Phase 2 — Transaction Reliability
Transaction / Audit Consistency
↓
### Rollback Verification
            ↓
### Concurrency Testing
            ↓
### Consistency Guarantees
Phase 3 — Fraud Improvements
### Velocity Checks
      ↓
### Configurable Rules
      ↓
### Fraud Decision Persistence
      ↓
### Fraud Audit Trail
Phase 4 — Event Reliability
### Stable Event Contracts
        ↓
### Outbox Pattern Evaluation
        ↓
### Retry Handling
        ↓
### Dead Letter Handling
Phase 5 — Distributed Architecture

Only when justified:

Kafka ↓ ### Notification Service ↓ ### Fraud Service ↓ Service-to-Service Authentication ↓ Service-Owned Databases ↓ ### Distributed Tracing 🚫 Technologies Intentionally Deferred

The following technologies are not introduced simply for technology adoption:

Kafka Redis Kubernetes ### Service Mesh **CQRS** ### Event Sourcing Saga ### Distributed Transactions ### Multiple Databases

They will be introduced only when there is a concrete architectural or business requirement.

🧰 Technology Stack Backend Java 21+ ### Spring Boot ### Spring Framework ### Spring Web Spring Data **JPA** Hibernate ### Spring Security ### Bean Validation MapStruct Lombok Database PostgreSQL Flyway **SQL** **JPA** / Hibernate Messaging Current ### Spring Application Events Planned ### Apache Kafka Caching Planned Redis Testing JUnit 5 Mockito ### Spring Boot Test ### Spring Security Test Testcontainers Build Maven ### Development Tools IntelliJ **IDEA** Git GitHub Postman Docker ### Docker Compose Observability ### Spring Boot Actuator ### Structured Logging Micrometer OpenTelemetry Prometheus Grafana 📂 Project Structure digital-banking/ │ ├── src/ │   │ │   ├── main/ │   │   │ │   │   ├── java/ │   │   │   │ │   │   │   └── com/ │   │   │       └── novalabs/ │   │   │           └── digitalbanking/ │   │   │               │ │   │   │               ├── auth/ │   │   │               │ │   │   │               ├── account/ │   │   │               │   ├── controller/ │   │   │               │   ├── dto/ │   │   │               │   ├── entity/ │   │   │               │   ├── enums/ │   │   │               │   ├── generator/ │   │   │               │   ├── mapper/ │   │   │               │   ├── repository/ │   │   │               │   └── service/ │   │   │               │ │   │   │               ├── payment/ │   │   │               │ │   │   │               ├── transaction/ │   │   │               │ │   │   │               ├── audit/ │   │   │               │ │   │   │               ├── fraud/ │   │   │               │   ├── engine/ │   │   │               │   ├── model/ │   │   │               │   └── rule/ │   │   │               │ │   │   │               ├── notification/ │   │   │               │   ├── event/ │   │   │               │   ├── listener/ │   │   │               │   └── service/ │   │   │               │ │   │   │               └── common/ │   │   │                   ├── exception/ │   │   │                   ├── response/ │   │   │                   ├── validation/ │   │   │                   └── util/ │   │   │ │   │   └── resources/ │   │       │ │   │       ├── application.yml │   │       ├── application-dev.yml │   │       ├── application-test.yml │   │       └── db/ │   │           └── migration/ │   │ │   └── test/ │ ├── docs/ │   ├── architecture/ │   │   └── architecture-review.md │   │ │   └── backlog/ │       └── august-backlog.md │ ├── pom.xml ├── docker-compose.yml ├── .gitignore └── **README**.md 📖 Documentation

Detailed architecture and planning documents are maintained under:

docs/
│
├── architecture/
│   └── architecture-review.md
│
└── backlog/
└── august-backlog.md
### Architecture Review

Contains:

Current architecture Module responsibilities Dependency rules Transaction boundaries Concurrency strategy Fraud architecture Event architecture Security boundaries Testing strategy Architecture decisions Technical debt Microservice extraction strategy ### August Backlog

Contains prioritized engineering work for:

Payment reliability Fraud improvements Event reliability Platform capabilities Distributed architecture 📌 Current Development Focus

The project is currently focused on reliability and architecture maturity rather than adding infrastructure.

Architecture
↓
### Financial Correctness
     ↓
### Payment Reliability
     ↓
### Fraud Reliability
     ↓
### Event Reliability
     ↓
Observability
↓
### Microservice Evolution
🎯 Next Milestone
### Payment Reliability

The next major engineering milestone focuses on:

    Payment Reliability
    │
    ┌───────────┼───────────┐
    ▼           ▼           ▼
    Idempotency   State       Retry
    Machine     Semantics
    │           │           │
    └───────────┼───────────┘
    ▼
    Transaction Safety
    │
    ▼
    Outbox Evaluation
    │
    ▼
    Kafka
💡 Engineering Philosophy

This project follows a simple principle:

Do not introduce technology because it is popular. Introduce technology because the system has a real problem that the technology solves.

For example:

Need caching?
↓
### Evaluate Redis

Need durable messaging?
↓
### Evaluate Kafka

Need independent deployment?
↓
### Evaluate Microservices

Need distributed tracing?
↓
Evaluate OpenTelemetry

Need distributed transactions?
↓
Evaluate Saga / Outbox / other patterns

The architecture should always be driven by:

### Business Requirements

        +
### Consistency Requirements
        +
### Scalability Requirements
        +
### Reliability Requirements
        +
### Operational Constraints
📈 Long-Term Architecture Vision

The final target architecture will look conceptually like:

    ┌──────────────────────┐
    │      Client Apps     │
    └──────────┬───────────┘
    │
    ▼
    ┌──────────────────────┐
    │     **API** Gateway      │
    └──────────┬───────────┘
    │
    ┌──────────────────────┼────────────────────────┐
    │                      │                        │
    ▼                      ▼                        ▼
    ┌─────────────┐       ┌─────────────┐          ┌─────────────┐
    │    Auth     │       │   Account   │          │   Payment   │
    │   Service   │       │   Service   │          │   Service   │
    └─────────────┘       └─────────────┘          └──────┬──────┘
    │
    ▼
    ┌─────────────┐
    │    Fraud    │
    │   Service   │
    └──────┬──────┘
    │
    ▼
    ┌─────────┐
    │  Kafka  │
    └────┬────┘
    │
    ┌──────────────────────────┼──────────────────────┐
    │                          │                      │
    ▼                          ▼                      ▼
    Notification                Transaction              Analytics
    Service                    Service                  Service

This architecture is a future target, not the current implementation.

🛡️ Production Engineering Principles

The project prioritizes:

Reliability Idempotency Transactions Rollback Retry ### Failure Handling Security Authentication Authorization **JWT** ### Account Ownership ### Secure Logging Performance ### Database Indexing ### Connection Pooling Pagination ### Efficient Queries ### Lock Management Scalability Stateless APIs ### Domain Boundaries Event-Driven Processing ### Horizontal Scaling ### Selective Microservice Extraction Observability ### Structured Logging Metrics Correlation IDs Tracing ### Health Checks 👨‍💻 Author ### Venkatesh Prathipati

### Senior Software Engineer

### Technical Focus

Java Kotlin ### Spring Boot ### Spring Security Microservices ### Distributed Systems PostgreSQL MongoDB Kafka Redis Docker **AWS** ### Backend Architecture ⭐ Project Vision

The long-term goal is to evolve this project from:

### Modular Monolith

into:

Production-Grade Distributed Digital Banking & Payments Platform

while maintaining:

    Financial Correctness
    +
    Security
    +
    Reliability
    +
    Scalability
    +
    Observability
    +
    Maintainability

The system will evolve incrementally.

The goal is not to demonstrate the maximum number of technologies.

The goal is to demonstrate why each technology, architecture pattern, and engineering decision is appropriate for the problem being solved.