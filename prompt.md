# Finance Microservices Prompt

## Purpose
This repository hosts a teaching-friendly finance platform that mixes Spring Boot microservices with a modern Vite + React (TypeScript) frontend. Use this prompt as the source of truth for future enhancements so we preserve a production-grade architecture, clean code, and a cohesive Notion-inspired user experience.

## High-Level Architecture
- **Service registry & routing**
  - `eureka-server` exposes service discovery on port 8761. 【F:eureka-server/src/main/resources/application.yml†L1-L11】
  - `api-gateway` runs Spring Cloud Gateway on port 8080 and relies on Eureka for dynamic service routing. 【F:api-gateway/src/main/resources/application.yml†L1-L24】
- **Domain services**
  - `audit-log-service` is a Spring Boot REST API backed by MySQL with JPA + HikariCP connection pooling. 【F:services/audit-log-service/pom.xml†L27-L48】【F:services/audit-log-service/src/main/resources/application.yml†L1-L31】
  - `transaction-service` is provisioned as a Spring Boot service targeting PostgreSQL; reconcile its 8003 server port with Docker (currently mapped as 8002). 【F:services/transaction-service/src/main/resources/application.properties†L1-L17】【F:docker-compose.yml†L32-L58】
- **Frontend**
  - `frontend` is a Vite workspace with React 18, Tailwind, Radix UI, shadcn/ui primitives, and TypeScript domain models. 【F:frontend/package.json†L1-L47】【F:frontend/src/components/ui/accordion.tsx†L1-L14】【F:frontend/src/types/transaction.ts†L1-L25】
- **Data layer**
  - MySQL runs in Docker for audit logging. Seed data lives in `docs/db/audit-log.sql`. 【F:docker-compose.yml†L12-L57】【F:docs/db/audit-log.sql†L1-L20】

## Repository Layout
- `/api-gateway` – Spring Cloud Gateway entry point.
- `/eureka-server` – Netflix Eureka registry.
- `/services/audit-log-service` – REST service for audit entries.
- `/services/transaction-service` – Transaction domain service (verify database credentials before use).
- `/frontend` – Vite + React client resembling Notion.
- `/docs` – Additional assets such as database seed scripts and architecture diagrams.
- `/docker-compose.yml` – Spins up services, MySQL, and the frontend dev server.

## Backend Implementation Guidelines
1. **Spring Boot conventions**
   - Target Java 17 (per Maven properties). 【F:api-gateway/pom.xml†L26-L31】
   - Organize packages by bounded context: `controller`, `service`, `repository`, `config`, `model`.
   - Prefer constructor injection, keep controllers thin, encapsulate business logic in services.
2. **Persistence**
   - For MySQL (audit logs), use Spring Data JPA with explicit entities and DTOs. Ensure schema aligns with `docs/db/audit-log.sql` when generating migrations.
   - For PostgreSQL (transactions), introduce Flyway or Liquibase migrations before expanding domain logic.
3. **Messaging and integration**
   - Future Kafka integration should live behind ports/config toggles; design event payloads with versioning.
4. **Observability**
   - Expose Spring Actuator endpoints (already enabled). Add metrics/tracing via Micrometer if you introduce new services.
5. **Testing**
   - Use `@SpringBootTest` or slice tests (`@WebMvcTest`, `@DataJpaTest`) for isolated coverage. Mock external dependencies with Testcontainers or embedded DBs.
6. **Service contracts**
   - Document REST APIs with SpringDoc OpenAPI or similar. Keep DTOs separate from JPA entities for forward compatibility.

## Frontend Architecture Guidelines
1. **Tech stack**
   - React 18 with functional components, hooks, and TypeScript types defined under `src/types`. 【F:frontend/src/types/transaction.ts†L1-L25】
   - TailwindCSS powers layout tokens, extended in `src/styles/globals.css`. Keep styling declarative via utility classes.
   - Component primitives live under `src/components/ui` (shadcn/Radix wrappers). Favor composition over new bespoke components.
2. **State & data flow**
   - Use React Query or SWR for server state when APIs are ready; continue using `useState`/`useMemo` for local UI state as in `App.tsx`. 【F:frontend/src/App.tsx†L1-L74】
   - Encapsulate domain-specific filtering/sorting logic in dedicated hooks or utilities for reusability.
3. **File organization**
   - Place screens/pages in `src/pages` (create if needed) and co-locate feature-specific components within folders.
   - Share constants/mock data from `src/data` until real APIs replace them. 【F:frontend/src/data/mockTransactions.ts†L1-L200】

## Notion-Inspired Design System
1. **Core principles**
   - Embrace spacious layouts with subtle dividers, light-neutral backgrounds, and rounded corners reminiscent of Notion.
   - Typography should prioritize hierarchy through weight and size, using Tailwind’s `font-semibold` and `text-muted-foreground` tokens for contrast.
2. **Component usage**
   - Reuse existing shadcn/ui primitives (Buttons, Cards, Table, etc.) before creating anything new. Only craft a bespoke component when the layout cannot be expressed via existing primitives or simple composition.
   - When introducing new components, document purpose, props, and responsive behavior in a colocated README or story.
3. **Layout patterns**
   - Favor split panes, sidebars, and minimal chrome. Use `flex` and `grid` to achieve Notion-like column layouts. Maintain generous padding (`p-6`, `gap-6`) similar to `App.tsx`. 【F:frontend/src/App.tsx†L45-L74】
4. **Interactions**
   - Animations should be subtle (`transition-colors`, `hover:bg-accent/20`). Provide keyboard accessibility via Radix components.
5. **Theming**
   - Keep dark mode parity when altering colors. Align with `next-themes` conventions already installed in the project dependencies. 【F:frontend/package.json†L28-L37】

## Development Workflow
1. **Local services**
   - Use `docker-compose up --build` to orchestrate Eureka, gateway, MySQL, backend services, and the frontend dev server. 【F:docker-compose.yml†L1-L58】
   - Override database credentials/ports via environment variables instead of editing committed configs where possible.
2. **Backend**
   - Run `./mvnw spring-boot:run` per service for targeted debugging. Leverage Spring profiles to differentiate local vs. container settings.
3. **Frontend**
   - Start the UI with `npm install` followed by `npm run dev -- --host 0.0.0.0 --port 5173` (already configured in `package.json`). 【F:frontend/package.json†L39-L44】
   - For production builds, run `npm run build` and host the output through the gateway or a CDN.
4. **Testing & linting**
   - Execute `./mvnw test` for backend units/integration tests and `npm run build` or add `npm run test` for frontend when testing infrastructure exists.
   - Formatting is managed by Prettier (`.prettierrc`). Ensure editors honor LF endings, 2-space indentation, and trailing commas. 【F:.prettierrc†L1-L9】

## Data & Environment Management
- Keep secrets out of the repo—use `.env` files (already gitignored) or Docker secrets. 【F:.gitignore†L1-L120】
- Align database schemas with the seed SQL and document migrations.
- When introducing new persistence layers (Redis, Kafka, etc.), expand `docker-compose.yml` thoughtfully and note required ports.

## Documentation & Collaboration
- Update `README.md` or create docs under `docs/` whenever you add features or architectural decisions.
- Provide ADRs (Architecture Decision Records) for major changes (e.g., introducing CQRS, switching databases).
- Ensure pull requests explain trade-offs, testing evidence, and any manual steps required.

## Quality Gates
- Demand >80% coverage for new backend modules; use JaCoCo reports to verify.
- For frontend changes, include visual regression evidence or manual screenshots when modifying UI-heavy components.
- Add CI checks for formatting (`prettier --check`), type safety (`tsc --noEmit`), and backend tests.

## Future Enhancements (Guidance)
- **API contracts**: Introduce shared DTO packages or OpenAPI-generated clients for typed frontend consumption.
- **Security**: Plan for OAuth2/OpenID Connect on the gateway; secure inter-service calls with service accounts or mTLS.
- **Resilience**: Add circuit breakers (Resilience4j) and centralized logging (ELK stack) as services evolve.

Keep these guidelines in mind to maintain a clean, scalable, and educational codebase that still mirrors real-world enterprise practices.
