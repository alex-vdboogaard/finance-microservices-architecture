# Finance Microservices System

A microservices-based backend that simulates inter-account money transfers at scale, inspired by real-world digital banking infrastructure.  
The system focuses on scalability, fault tolerance, asynchronous communication, and cloud-native design using Java Spring Boot and supporting technologies.

---

## Demo Video

[![Demo Video](https://img.youtube.com/vi/xbn1LlVYExQ/0.jpg)](https://www.youtube.com/watch?v=xbn1LlVYExQ)

---

## Screenshots

![Architecture](assets/overall%20architecture.png)  
![Transaction flow](assets/transaction%20flow.png)  
![Metrics](assets/grafana%20cpu%20usage.png)
![k6 Test](assets/k6%20test.png)  
![Kafka UI](assets/kafka%20ui.png)  

---

## Tech Stack

- **Backend:** Java Spring Boot (with API Gateway)
- **Service Discovery:** Netflix Eureka
- **Databases:** MySQL & PostgreSQL (per-service isolation)
- **Caching / Rate Limiting:** Redis
- **Message Queue:** Apache Kafka
- **Containerisation:** Docker & Docker Compose
- **Monitoring / Observability:** Prometheus, Grafana, Loki
- **Load Testing:** k6
- **Error Standard:** RFC 9457

---

## Architecture Overview

This system is designed around independent, domain-focused microservices:

- **Account Service:** Manages accounts, balances, and validations
- **Transaction Service:** Orchestrates transfers and tracks lifecycle
- **Notification Service:** Push notifications for success/failure events
- **Audit Service:** Immutable, append-only logs for traceability

Communication between services is fully asynchronous via Kafka topics:

- `transaction.initiated`
- `transaction.completed`
- `transaction.failed`

A lightweight **SAGA pattern** ensures distributed consistency and recovery.

---

## Performance Testing

Four k6 scenarios were implemented to simulate realistic pressure:

- Load testing
- Stress testing
- Spike testing
- Scalability testing

These tests validated throughput, latency, resilience, and Kafka’s effectiveness under concurrency.

---

## Scale Inspiration

India’s UPI system handles ~3,729 transactions per second (223,740 per minute).  
This highlights the scale modern financial platforms must support.

Testing at that volume was avoided for the sake of the laptop’s survival.

---

## Lessons Learned

- Value of asynchronous communication in distributed systems
- Designing for fault tolerance and eventual consistency
- Importance of observability and pressure testing
- Awareness that some architectural decisions may need improvement—learning is ongoing

---

## Future Improvements

- Fraud-detection pipeline
- Ledger service
- Grafana alerting rules
- Full CI/CD pipeline:
  - Automated tests
  - Docker builds
  - Security scans
  - Kubernetes deployments

---

## Running the Project

```bash
docker-compose up --build
```
