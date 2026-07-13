# 🏦 Digital Banking & Payments Platform

> **An enterprise-grade backend project built incrementally to learn production-ready Java, Spring Boot, and distributed system design.**

This project simulates a real-world digital banking platform similar to those used by fintech companies. The project starts as a **Modular Monolith** and will gradually evolve into a **Microservices Architecture** while maintaining production-quality engineering practices.

---

# 🚧 Project Status

## Current Phase

**Architecture:** Modular Monolith

**Current Sprint:** Spring Boot Foundation – Account Service

### ✅ Completed

- Spring Boot Project Setup
- Layered Architecture
- Environment Profiles
- PostgreSQL Configuration
- Flyway Database Migration
- Account Module
- CRUD REST APIs
- Request Validation
- Global Exception Handling
- Logging
- Maven Build Configuration

### 🚀 Upcoming

- Spring Security
- Authentication & Authorization
- Payment Module
- Transaction Module
- Unit Testing
- Docker
- Redis
- Kafka
- Monitoring
- Microservices Migration

---

# 🎯 Project Objectives

Build a banking platform capable of supporting:

- Customer Account Management
- Secure Money Transfers
- Transaction History
- Fraud Detection
- Notifications
- High Concurrency
- Event-Driven Communication
- Fault Tolerance
- Distributed Systems

The focus is on learning **enterprise architecture and backend engineering**, not just implementing REST APIs.

---

# 🏗️ Current Architecture

The project currently follows a **Modular Monolith** architecture.

```text
                    Client
                       │
                       ▼
             Spring Boot Application
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
   Controller      Service      Repository
        │              │              │
        └──────────────┼──────────────┘
                       ▼
                  PostgreSQL
                       │
                    Flyway
```

---

# 🎯 Target Architecture (Future)

The project will gradually evolve into the following microservices architecture.

```text
                   Client Applications
                          │
                          ▼
                    API Gateway
                          │
 ┌─────────┬─────────┬─────────┬──────────┬────────────┐
 ▼         ▼         ▼         ▼          ▼
Auth   Account   Payment   Transaction   Fraud
Service Service   Service     Service     Service
 │         │         │           │           │
PostgreSQL PostgreSQL PostgreSQL MongoDB   MongoDB
                          │
                          ▼
                 Notification Service
                          │
                       MongoDB

                Kafka • Redis • Monitoring
```

---

# 🛠 Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Validation
- Spring Security *(Upcoming)*
- Spring Cloud *(Future)*

## Database

- PostgreSQL
- Flyway

## Future Technologies

- MongoDB
- Kafka
- Redis
- Docker
- Kubernetes
- Prometheus
- Grafana
- ELK Stack

---

# 📂 Current Project Structure

```text
digital-banking/

├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── novalabs
│   │   │           └── digitalbanking
│   │   │               ├── account
│   │   │               │   ├── controller
│   │   │               │   ├── dto
│   │   │               │   ├── entity
│   │   │               │   ├── repository
│   │   │               │   └── service
│   │   │               ├── config
│   │   │               ├── exception
│   │   │               └── DigitalBankingApplication
│   │   │
│   │   └── resources
│   │       ├── db
│   │       │   └── migration
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       └── application-prod.yml
│
├── pom.xml
├── README.md
└── .gitignore
```

---

# 📦 Current Module

## Account Service

Implemented Features

- Create Account
- Get Account
- Update Account
- Delete Account
- Account Validation
- Exception Handling

### Engineering Concepts

- Layered Architecture
- DTO Pattern
- Repository Pattern
- Dependency Injection
- Bean Validation
- Flyway Migration
- ACID Transactions

---

# 🚀 Running the Project

## Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 17+

## Clone Repository

```bash
git clone <repository-url>
cd digital-banking
```

## Create Database

```sql
CREATE DATABASE digital_banking;
```

## Configure

Update:

```
application-dev.yml
```

with your PostgreSQL credentials.

## Run

```bash
mvn spring-boot:run
```

Application starts at:

```
http://localhost:8080
```

---

# 📖 Implemented REST APIs

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/accounts` | Create Account |
| GET | `/api/v1/accounts/{id}` | Get Account |
| GET | `/api/v1/accounts` | List Accounts |
| PUT | `/api/v1/accounts/{id}` | Update Account |
| DELETE | `/api/v1/accounts/{id}` | Delete Account |

---

# 🗺 Development Roadmap

```text
✅ Project Setup

        ↓

✅ Configuration

        ↓

✅ PostgreSQL

        ↓

✅ Flyway

        ↓

✅ Account Module

        ↓

⬜ Spring Security

        ↓

⬜ Payment Module

        ↓

⬜ Transaction Module

        ↓

⬜ Kafka Integration

        ↓

⬜ Redis

        ↓

⬜ Docker

        ↓

⬜ Monitoring

        ↓

⬜ Microservices Migration
```

---

# 🎓 Engineering Concepts Covered

- Spring Boot
- Dependency Injection
- Bean Lifecycle
- REST APIs
- DTO Pattern
- Repository Pattern
- Validation
- Exception Handling
- Logging
- PostgreSQL
- Flyway
- JPA & Hibernate

Upcoming:

- Spring Security
- Kafka
- Redis
- Docker
- Distributed Transactions
- Saga Pattern
- Event-Driven Architecture

---

# 📚 Learning Goals

This project is designed to gain hands-on experience with:

- Enterprise Backend Development
- Spring Boot Internals
- Production Code Quality
- REST API Design
- Database Design
- Distributed Systems
- Event-Driven Architecture
- High-Concurrency Applications
- Cloud-Native Development

---

# 👨‍💻 Git Commit Convention

Examples:

```text
feat(account): add account creation endpoint
fix(account): validate duplicate account numbers
refactor(account): simplify service layer
docs(readme): update project documentation
chore: cleanup project structure
```

---

# 📄 License

This project is intended for educational purposes and to demonstrate enterprise backend engineering practices.