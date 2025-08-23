# init-microservice

A repo that contains bootstrapping code and reference implementations for developing a microservice.

Built in Java.

## Showcases

Project configuration:

- [x] README
- [x] .gitignore
- [x] Docs + ADRs (MkDocs + Markdown)
- [x] Auto-updates (Renovate)
- [x] HTTP API Specification (OpenAPI)
- [x] Async API Specification (AsyncAPI)
- [x] Dependency Management (Maven)

Application configuration:

- [x] Dependency Injection and Application Mgmt (Spring Boot & Starters)
- [x] Unit Testing (JUnit)
- [x] Logging Config (Slf4j & Logback)
- [x] Tracing configuration (Micrometer + OpenTelemetry) // TODO: Replace!
- [ ] Tracing configuration (OpenTelemetry Java agent for Zero code instrumentation + OpenTelemetry annotations)
- [x] Metrics configuration (Micrometer + Prometheus Registry/Endpoint)

Build / CI:

- [x] Test & Build automation (Maven, GitHub Actions)
- [x] Uses reusable [workflows](https://github.com/Harmelodic/workflows) for ease of CI maintenance.
- [x] Java Project + Container image specific build process:
    - [x] Lint/Scan Java code (PMD, Modernizer, Spotbugs)
    - [x] Testing (Maven Surefire plugin)
    - [x] Automated publishing of Contract Testing Contracts and Results (PACT Broker, GitHub Actions)
    - [x] Packaging JAR (Maven Compiler plugin, Spring Boot Maven plugin)
    - [x] Packaging and pushing a container image (Jib Maven plugin)

Deployment / CD:

- [x] Kubernetes resources (Kustomize)
- [ ] Spread pods (Pod Anti-affinity)
- [ ] Auto-scaling (HPA)
- [x] Pod security (SecurityContext)
- Expected that an external CD system would deploy to Kubernetes (e.g. Argo CD)

Infrastructure as Code:

- [ ] Terraform Database (Google CloudSQL / Redis / BigTable / Firestore)
- [ ] Terraform Message Bus Topics/Subscriptions (Google Pub/Sub)
- [x] Rudimentary applying of Terraform (GitHub Actions)
- Expected that an external CD system would apply Terraform (e.g. Atlantis)

Reference implementation examples (production):

- [x] Application Structure Example (account)
    - Reasonably decoupled layers/components
    - Domain-driven
    - Scoped explicit exception handling
    - Simple reusable model, mapping done in layers (if needed)
    - Dependency Injection used
    - No implementation details (as implementations covered in other reference implementations)
- HTTP Endpoint
    - [ ] Front Controller
    - [ ] Authorization checks
    - [ ] Versioning
    - [ ] HTTP POST Idempotency
    - [ ] Offset Pagination
    - [ ] Cursor Pagination
    - [ ] Caching (where appropriate)
- Business Logic / Service Layer pattern
    - [ ] Modelling
    - [ ] Transactions
    - [ ] Caching
- Repository pattern
    - [ ] Jdbc
    - [ ] Retries
- External communication components
    - [ ] Event Publisher (with & without Outbox pattern)
    - [ ] Event Subscriber with resubscribe (with & without Inbox pattern)
    - [ ] HTTP Client (with & with retries)
    - [ ] Offline API
    - [ ] Message Bus idempotency
- Telemetry
    - [ ] Tracing instrumentation (Micrometer)
    - [ ] Metrics instrumentation (Micrometer)
- [ ] State machine example

Reference implementations (testing):

- [x] Provider Contract Testing the Controller (PACT)
- [x] Consumer Contract Testing the HTTP Client (PACT)
- [x] Integration Testing the Repository (in-memory DB)
- [ ] Integration Testing the Event Publisher (Testcontainers)
- [ ] Integration Testing the Event Subscriber (Testcontainers)

## Documentation

Uses `mkdocs` to handle documentation, which requires Python (hence the `requirements.txt`).

Run docs locally by doing:

```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
mkdocs serve
```

Then open at http://localhost:8000

## Running the app locally

```bash
mvn spring-boot:run
```

- Endpoints accessible on http://localhost:8080
- Management endpoints on: https://localhost:8081
