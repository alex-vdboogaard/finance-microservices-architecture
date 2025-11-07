## Finance Microservices — Personal Project Case Study

This is a personal portfolio project where I designed and built an event‑driven microservices system that simulates inter‑account transfers, end‑to‑end audit logging, and API gateway routing with rate limiting. It runs locally with Docker Compose and showcases how I think about service boundaries, asynchronous workflows, observability, and developer ergonomics.


**Demo Video**
- Add your demo link here and a brief description (what the viewer will see in ~2–4 minutes: a transfer, Kafka events, audit entries, and dashboards).


**Diagrams**
- Architecture: add an image at `docs/architecture/architecture-diagram.png`.
- Flow: add an image at `docs/architecture/flow-diagram.png`.


**Quick Links**
- Demo Video (2–4 min): add your link here
- Architecture Diagram: docs/architecture/architecture-diagram.png (placeholder)
- Flow Diagram: docs/architecture/flow-diagram.png (placeholder)


**What I Built**
- Event‑driven transfers using Kafka topics (`transaction.initiated`, `transaction.completed`, `transaction.failed`).
- Service trio: Transaction Service (creates and updates transactions), Account Service (applies debits/credits), Audit Log Service (persists audit trail).
- API Gateway (Spring Cloud Gateway) with Redis‑backed global rate limiting and CORS.
- Service discovery with Eureka to decouple routing from static hostnames.
- Polyglot persistence to demonstrate trade‑offs: PostgreSQL (accounts, transactions) and MySQL (audit logs).
- Observability with Prometheus + Grafana; Kafka UI for topic/message inspection.
- Load testing via k6 to stress the workflow through the gateway or directly to services.


**Architecture Overview**
- `api-gateway` (8080): central ingress, CORS, rate limiting, discovery routing.
- `eureka-server` (8761): service registry for gateway and services.
- `services/transaction-service` (8003): receives transfer requests, saves as PENDING, emits `transaction.initiated`, consumes outcomes to update status.
- `services/accountservice` (8005): consumes initiated events, applies debit/credit logic, emits `transaction.completed|failed`.
- `services/audit-log-service` (8001): consumes all events and records an audit trail.
- `kafka`/`zookeeper` + Kafka UI (8081): messaging backbone and visualization.
- `postgres` + `mysql`: data stores; `redis`: rate limiter storage; `prometheus` (9090) and `grafana` (3000) for metrics.


**End‑to‑End Flow**
- Client calls `POST /api/v1/transactions` (via gateway).
- Transaction Service stores PENDING and publishes `transaction.initiated`.
- Account Service consumes, performs balance updates, and publishes `completed` or `failed`.
- Transaction Service consumes the outcome and updates status accordingly.
- Audit Log Service logs each stage for traceability.


**Design Decisions & Trade‑offs**
- Choreography via Kafka topics instead of orchestration to keep services decoupled and easy to extend.
- DB per service and polyglot persistence to reflect real‑world boundaries and operational trade‑offs.
- Global rate limiting at the gateway to protect downstream services; this intentionally impacts load testing and demonstrates back‑pressure behavior (HTTP 429).
- Kept the MVP simple: no distributed transactions, no outbox pattern or idempotency yet. This highlights eventual consistency and the importance of retry/idempotent design for production (see “Next Steps”).
- Discovery‑based routing (Eureka) to decouple the gateway from static hosts, while still providing explicit routes for key APIs.


**Tech Stack**
- Java 17, Spring Boot 3.3, Spring Cloud Gateway, Eureka
- Kafka, Spring for Apache Kafka
- PostgreSQL, MySQL, Spring Data JPA
- Redis (rate limiter), Docker Compose
- Prometheus, Grafana, Kafka UI
- k6 for load testing


**Load Testing (k6)**
- Script: `k6/transactions-test.js`
- Run: `k6 run k6/transactions-test.js`
- Note: gateway rate limiter (5 req/s, burst 10) can produce 429s; target `http://localhost:8003/api/v1/transactions` to avoid throttling during tests.


**What I Learned / Demonstrated**
- Designing asynchronous flows with Kafka and modeling state transitions across services.
- Applying service discovery and gateway policies to balance developer experience and resilience.
- Setting up local observability to shorten feedback loops during development and load testing.
- Handling back‑pressure and rate limits while generating load with k6.


**Next Steps (If I continue this project)**
- Add input validation and idempotent request handling.
- Introduce an outbox pattern and/or a saga orchestrator for stronger consistency and error handling.
- Add authentication/authorization on the gateway and services.
- Expand test coverage and add CI pipelines.
- Harden failure/retry policies and dead‑letter topics.


**Repository Map**
- `api-gateway/` — Spring Cloud Gateway config and filters.
- `eureka-server/` — Service registry.
- `services/transaction-service/` — Transfer APIs, Kafka producer/consumer, Postgres persistence.
- `services/accountservice/` — Account APIs, transfer application logic, Postgres persistence.
- `services/audit-log-service/` — Audit APIs, Kafka consumers, MySQL persistence.
- `k6/` — Load testing scripts.
- `monitoring/` — Prometheus config; Grafana persists in `docker-data/grafana/`.
- `docker-compose.yml` — Full local stack.

