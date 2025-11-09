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
- [x] Dependency Management (Maven + BOMs)

Application configuration:

- [x] Dependency Injection and Application Mgmt (Spring Boot & Starters)
- [x] Unit Testing (JUnit)
- [x] Logging Config (Slf4j & Logback)
- [x] Tracing configuration (OpenTelemetry Java Agent for zero-code instrumentation, OpenTelemetry annotations for
  custom instrumentation)
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

- [x] Application Structure Example (`app.structure.account`)
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
	- [ ] Server Sent Events
- Business Logic
	- [ ] Modelling
	- [ ] Service Layer pattern
	- [ ] Transactions
	- [ ] Concurrency / Parallelisation of doing work / collecting data (Virtual Threads)
- Repository pattern
	- [ ] JDBC
- External communication components
	- [ ] Event Publisher (simple & with Outbox pattern, with CloudEvents (inc. idempotency))
	- [ ] Event Subscriber with resubscribe (simple & with Inbox pattern, with CloudEvents (inc. idempotency))
	- [ ] HTTP Client (simple & resilient & streamed-response)
- Resiliency patterns (with Resilience4J and/or alternatives)
	- [ ] Bulkhead
	- [ ] Cache (Simple)
	- [ ] Cache (Offline API)
	- [ ] Circuit Breaker
	- [ ] Rate Limiter
	- [ ] Retry
	- [ ] Timeout / Time Limiter
- Telemetry
	- [ ] Tracing instrumentation (OpenTelemetry Annotations)
	- [ ] Metrics instrumentation (Micrometer or OpenTelemetry)
	- [ ] Logging instrumentation (SLF4J and Logback)

Reference implementations (testing):

- [x] Provider Contract Testing the Controller (PACT)
- [x] Consumer Contract Testing the HTTP Client (PACT)
- [x] Integration Testing the Repository (in-memory DB)
- [ ] Integration Testing the Event Publisher (Testcontainers)
- [ ] Integration Testing the Event Subscriber (Testcontainers)

## Documentation

Docs are found in `/docs`.

Uses `mkdocs` to handle documentation, which requires Python (hence the `requirements.txt`).

Run docs locally by doing:

```bash
# Before running the following, I recommend being in a Python Virtual Environment.
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

## Template Usage

When being used for templating, remember to set the following in your new repository:

- Rename all instances of `init-microservice` with your application name.
- Refactor the package structure of `init.microservice` to be what you need.
- For GitHub Actions, use the Workflow Permissions: "Read and write permissions".
- Remove/Refactor the infrastructure to ensure it is scoped to the new project (i.e. backend config bucket with a
  prefix).
- Remove/Refactor the PACT tests to ensure they don't interfere with the `init-microservice` PACT tests results (or any
  others).
