## Finance Microservices Architecture

**Demo Video**

**Overview**
This production‑ready microservices demonstrates event‑driven inter‑account transfers, audit logging, API gateway, service discovery, observability, and rate limiting using a modern Spring Boot stack and Docker Compose.

Key capabilities:
- Inter‑account transfers with transactional outbox pattern via Kafka topics
- API Gateway with rate limiting (Redis) and service discovery (Eureka)
- Account and Transaction services backed by PostgreSQL
- Audit Log service backed by MySQL
- Observability with Prometheus and Grafana
- Developer UX with Kafka UI and a sample k6 load test


**Table of Contents**
- Overview
- Architecture
- Services
- Tech Stack
- Prerequisites
- Quick Start (Docker Compose)
- Local Development
- Configuration
- API Endpoints
- Eventing (Kafka Topics)
- Observability
- Load Testing (k6)
- Troubleshooting
- Demo Video
- Diagrams
- Contributing
- License


**Overview**
- Goal: showcase an event‑driven microservices architecture for financial transfers and auditing.
- Flow: client hits the API Gateway → Transaction Service creates a PENDING transaction and emits `transaction.initiated` → Account Service consumes and attempts the transfer → emits `transaction.completed` or `transaction.failed` → Transaction Service updates status → Audit Log Service records each stage.


**Architecture**
- Core components
  - `api-gateway` (Spring Cloud Gateway) for routing, CORS, and rate limiting.
  - `eureka-server` for service discovery.
  - `services/transaction-service` manages transfer requests and status.
  - `services/accountservice` manages accounts and balances.
  - `services/audit-log-service` records audit logs for all events.
  - Messaging: Kafka + Zookeeper; UI: Kafka UI.
  - Data: PostgreSQL (transactions, accounts), MySQL (audit logs).
  - Observability: Prometheus + Grafana.
  - Caching/rate limiting: Redis.

- Ports (host)
  - API Gateway: 8080
  - Eureka: 8761
  - Transaction Service: 8003
  - Account Service: 8005
  - Audit Log Service: 8001
  - Kafka: 9092 (host), 29092 (container)
  - Kafka UI: 8081
  - Redis: 6379
  - PostgreSQL: 5432
  - MySQL: 3307 → 3306 (container)
  - Prometheus: 9090
  - Grafana: 3000


**Services**
- `api-gateway`: Routes `/api/v1/*` and discovery‑based routes (e.g., `/transaction-service/**`). Rate limits global traffic to 5 req/s, burst 10.
- `eureka-server`: Service registry used by all services and the gateway.
- `transaction-service`:
  - REST: `POST /api/v1/transactions`, `GET /api/v1/transactions`
  - DB: PostgreSQL (`transaction_db`)
  - Emits `transaction.initiated`, consumes `transaction.completed|failed`.
- `accountservice`:
  - REST: `GET/POST /api/v1/accounts`, `POST /api/v1/accounts/{id}/deposit`
  - DB: PostgreSQL (`account_db`)
  - Consumes `transaction.initiated`, emits `transaction.completed|failed`.
- `audit-log-service`:
  - REST: `GET /api/v1/logs`, `GET /api/v1/logs/action?action=...`, `POST /api/v1/logs`
  - DB: MySQL (`audit_db`)
  - Consumes initiated/completed/failed and stores audit entries.


**Tech Stack**
- Java 17, Spring Boot 3.3
- Spring Cloud Gateway, Eureka
- Kafka, Spring for Apache Kafka
- PostgreSQL, MySQL, Spring Data JPA
- Redis (rate limiter storage)
- Docker Compose
- Prometheus, Grafana
- k6 for load testing


**Prerequisites**
- Docker and Docker Compose
- Optional (local dev without Docker): Java 17, Maven, Postgres, MySQL, Redis, Kafka


**Quick Start (Docker Compose)**
1) Build and start the full stack:
   - `docker compose up -d --build`

2) Create the `account_db` database in Postgres (once):
   - `docker exec -it postgres psql -U postgres -c "CREATE DATABASE account_db;"`

3) Verify key UIs:
   - Eureka: http://localhost:8761
   - API Gateway: http://localhost:8080
   - Kafka UI: http://localhost:8081
   - Prometheus: http://localhost:9090
   - Grafana: http://localhost:3000 (admin/admin default; password set by compose)

4) Seed sample accounts (optional):
   - Create accounts via `POST http://localhost:8080/api/v1/accounts` with `{ "userId": 1, "accountNumber": "1001" }` etc.
   - Deposit funds: `POST http://localhost:8080/api/v1/accounts/{id}/deposit` with `{ "amount": 1000.00 }`.


**Local Development**
- Build all modules: `mvn clean package -DskipTests`
- Run a service:
  - Transaction Service: `mvn -pl services/transaction-service -am spring-boot:run`
  - Account Service: `mvn -pl services/accountservice -am spring-boot:run`
  - Audit Log Service: `mvn -pl services/audit-log-service -am spring-boot:run`
  - API Gateway: `mvn -pl api-gateway -am spring-boot:run`
  - Eureka Server: `mvn -pl eureka-server -am spring-boot:run`


**Configuration**
- All services pick up settings from their `application.yml` and environment variables.
- Kafka bootstrap servers are set via `SPRING_KAFKA_BOOTSTRAP_SERVERS` (compose points to `kafka:29092`).
- Gateway global rate limiter: 5 req/s, burst 10 (Redis backed).
- DBs:
  - Postgres (container `postgres`): `transaction_db` and `account_db`
  - MySQL (container `mysql`): `audit_db`


**API Endpoints**
- Through API Gateway (explicit routes):
  - Transactions: `POST /api/v1/transactions`, `GET /api/v1/transactions`
  - Accounts: `GET/POST /api/v1/accounts`, `POST /api/v1/accounts/{id}/deposit`
  - Audit Logs: `GET /api/v1/logs`, `GET /api/v1/logs/action?action=...`, `POST /api/v1/logs`
- Through discovery‑based routes (enabled):
  - `/{service-id}/**`, e.g., `/transaction-service/api/v1/transactions`

Example create transfer:
```
curl -X POST http://localhost:8080/api/v1/transactions \
  -H 'Content-Type: application/json' \
  -d '{"fromAccountId":1001, "toAccountId":1002, "amount": 50.0}'
```


**Eventing (Kafka Topics)**
- `transaction.initiated` (produced by Transaction Service)
- `transaction.completed` (produced by Account Service)
- `transaction.failed` (produced by Account Service)

Event flow:
- Transaction Service saves PENDING and emits `transaction.initiated`.
- Account Service consumes, applies debit/credit, emits `completed` or `failed`.
- Transaction Service consumes outcome and updates status.
- Audit Log Service records all stages to MySQL.


**Observability**
- Prometheus scrapes service metrics (where enabled in `management.endpoints`).
- Grafana dashboards can be configured to visualize service and JVM metrics.
- Kafka UI to inspect topics, messages, partitions.


**Load Testing (k6)**
- A ready script is available: `k6/transactions-test.js`.
- Run: `k6 run k6/transactions-test.js`
- By default it targets the gateway discovery route `http://localhost:8080/transaction-service/api/v1/transactions`.
  - Note: the gateway rate limiter (5 req/s, burst 10) may throttle with HTTP 429. You can target the Transaction Service directly at `http://localhost:8003/api/v1/transactions` to avoid throttling during tests.
- Each transfer triggers Kafka events (initiated → completed/failed) and corresponding audit logs.


**Troubleshooting**
- Account DB missing: create `account_db` inside the Postgres container:
  - `docker exec -it postgres psql -U postgres -c "CREATE DATABASE account_db;"`
- Rate limiting responses (429): lower k6 concurrency or target the service directly.
- Kafka connectivity: ensure Kafka and Zookeeper are healthy; verify topics via Kafka UI.
- CORS: the gateway allows `http://localhost:5173` by default and can be extended in `api-gateway/src/main/resources/application.yml`.

**Diagrams**
- Architecture Diagram: `docs/architecture/architecture-diagram.png` (placeholder)
- Flow Diagram: `docs/architecture/flow-diagram.png` (placeholder)
