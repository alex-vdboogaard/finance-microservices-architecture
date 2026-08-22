# ==============================================================================
# Finance Microservices Architecture - Master Makefile
# ==============================================================================
# Compatible with GNU Make 3.81 (macOS) and GNU Make 4.x+
# Provides industry-standard targets for build, test, docker orchestration,
# database management, and visualization dashboards.
# ==============================================================================

# Shell configuration
SHELL := /bin/bash

# Auto-detect CLI commands (overrideable via environment or command line)
MAVEN          ?= mvn
DOCKER_COMPOSE ?= $(shell if command -v docker-compose >/dev/null 2>&1; then echo "docker-compose"; else echo "docker compose"; fi)
NPM            ?= npm
K6             ?= k6
OPEN_SCRIPT    ?= ./scripts/open-dashboards.sh

# Default target when invoking `make` without arguments
.DEFAULT_GOAL := help

# ==============================================================================
# 1. HELP & SYSTEM INFORMATION
# ==============================================================================

.PHONY: help
help: ## Display this help message showing all available commands
	@echo ""
	@echo "======================================================================"
	@echo "  Finance Microservices Architecture - Command Center"
	@echo "======================================================================"
	@echo ""
	@echo "Usage: make [target] [SERVICE=service_name]"
	@echo ""
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'
	@echo ""

.PHONY: info
info: ## Display system environment information and detected tools
	@echo "=== System Environment & Tool Diagnostics ==="
	@echo "Operating System : $(shell uname -s) $(shell uname -m)"
	@echo "Docker Compose   : $(shell $(DOCKER_COMPOSE) version 2>/dev/null || echo 'Not Found')"
	@echo "Maven            : $(shell $(MAVEN) --version 2>/dev/null | head -n 1 || echo 'Not Found')"
	@echo "Node             : $(shell node -v 2>/dev/null || echo 'Not Found')"
	@echo "k6               : $(shell $(K6) version 2>/dev/null || echo 'Not Found')"

# ==============================================================================
# 2. DOCKER ORCHESTRATION & SERVICE MANAGEMENT
# ==============================================================================

.PHONY: up
up: ## Start all microservices and infrastructure containers in detached mode
	@echo "Starting full finance microservices architecture..."
	$(DOCKER_COMPOSE) up -d

.PHONY: start
start: up ## Alias for 'up'

.PHONY: down
down: ## Stop and remove all running containers and networks
	@echo "Stopping all microservices containers..."
	$(DOCKER_COMPOSE) down

.PHONY: stop
stop: down ## Alias for 'down'

.PHONY: restart
restart: ## Restart all docker containers
	@echo "Restarting microservices stack..."
	$(DOCKER_COMPOSE) restart

.PHONY: rebuild
rebuild: ## Rebuild Docker images and restart containers
	@echo "Rebuilding and restarting all services..."
	$(DOCKER_COMPOSE) up -d --build

.PHONY: ps
ps: ## Display status of all docker containers
	$(DOCKER_COMPOSE) ps

.PHONY: status
status: ps ## Alias for 'ps'

.PHONY: logs
logs: ## Tail log output from all docker containers
	$(DOCKER_COMPOSE) logs -f

.PHONY: logs-service
logs-service: ## Tail logs for a specific service (Usage: make logs-service SERVICE=account-service)
	@if [ -z "$(SERVICE)" ]; then \
		echo "Error: SERVICE parameter is required. Example: make logs-service SERVICE=transaction-service"; \
		exit 1; \
	fi
	$(DOCKER_COMPOSE) logs -f $(SERVICE)

.PHONY: up-infra
up-infra: ## Start only infrastructure dependencies (PostgreSQL, MySQL, Kafka, Redis, Eureka, Tracing, Monitoring)
	@echo "Starting infrastructure & monitoring services only..."
	$(DOCKER_COMPOSE) up -d postgres mysql-audit-log mysql-notification redis zookeeper kafka eureka-server zipkin prometheus loki promtail kafka-ui grafana

.PHONY: up-apps
up-apps: ## Start microservices and frontend application only
	@echo "Starting application microservices and frontend..."
	$(DOCKER_COMPOSE) up -d account-service audit-log-service transaction-service notification-service api-gateway frontend

# ==============================================================================
# 3. BUILD & COMPILATION
# ==============================================================================

.PHONY: build
build: build-maven build-frontend ## Build Java Maven services and Frontend application

.PHONY: build-maven
build-maven: ## Clean and compile all Maven modules (skipping tests)
	@echo "Building Maven backend modules..."
	$(MAVEN) clean package -DskipTests

.PHONY: build-frontend
build-frontend: ## Install dependencies and build Frontend production bundle
	@echo "Building Frontend application..."
	cd frontend && $(NPM) install && $(NPM) run build

.PHONY: clean
clean: ## Clean build target directories and node_modules
	@echo "Cleaning Maven target directories..."
	$(MAVEN) clean
	@echo "Cleaning Frontend build artifacts..."
	rm -rf frontend/dist

# ==============================================================================
# 4. TESTING & QUALITY
# ==============================================================================

.PHONY: test
test: test-maven ## Run all test suites

.PHONY: test-maven
test-maven: ## Execute unit and integration tests across all Java microservices
	@echo "Running Maven tests..."
	$(MAVEN) test

.PHONY: test-k6
test-k6: ## Run k6 load test suite against active services
	@echo "Executing k6 performance load tests..."
	$(K6) run k6/transactions-load-test.js

# ==============================================================================
# 5. DASHBOARDS & VISUALIZATION
# ==============================================================================

.PHONY: dashboards
dashboards: open-dashboards ## Alias for open-dashboards

.PHONY: open-dashboards
open-dashboards: ## Launch all visualization web UIs in default browser (Eureka, Kafka UI, Grafana, Zipkin, etc.)
	@chmod +x $(OPEN_SCRIPT)
	@$(OPEN_SCRIPT)

.PHONY: dashboards-list
dashboards-list: ## Display all dashboard URLs and ports without launching browser
	@chmod +x $(OPEN_SCRIPT)
	@$(OPEN_SCRIPT) --list --actuators

.PHONY: dashboards-check
dashboards-check: ## Check HTTP health status of dashboards before opening
	@chmod +x $(OPEN_SCRIPT)
	@$(OPEN_SCRIPT) --check

# ==============================================================================
# 6. DATABASE & UTILITIES
# ==============================================================================

.PHONY: db-reset
db-reset: ## Stop containers and remove all persistent database volume data (CAUTION: Resets DB state)
	@echo "WARNING: Resetting database volumes..."
	$(DOCKER_COMPOSE) down -v

.PHONY: redis-cli
redis-cli: ## Open an interactive Redis CLI inside the running redis container
	$(DOCKER_COMPOSE) exec redis redis-cli
