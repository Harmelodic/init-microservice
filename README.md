# init-microservice

A basic template repo that contains starter code for developing a microservice.

Built in Java.

## Showcases

Project configuration:

- [x] README
- [x] .gitignore
- [x] Docs + ADRs (MkDocs + Markdown)
- [x] Auto-updates (Renovate)
- [ ] HTTP API Specification (OpenAPI)
- [ ] Async API Specification (AsyncAPI) 
- [x] Dependency Management (Maven)

Application configuration:

- [x] Dependency Injection and Application Mgmt (Spring Boot & Starters)
- [x] Unit Testing (JUnit)
- [x] Logging Config (Slf4j & Logback)
- [ ] Tracing configuration (Micrometer + OpenTelemetry)
- [ ] Metrics configuration (Micrometer + Prometheus Registry/Endpoint)

Build / CI:

- [x] Test & Build automation (Maven, GitHub Actions)
- [x] Packaging and pushing a container image (Jib, Maven, GitHub Actions)
- [x] Automated publishing of Contract Testing Contracts and Results (PACT Broker, GitHub Actions)

Deployment / CD:

- [x] Kubernetes resources (Kustomize)
- Expected that an external CD system would deploy to Kubernetes (e.g. Argo CD)

Infrastructure as Code:

- [ ] Terraform Database (Google CloudSQL / Redis / BigTable / Firestore)
- [ ] Terraform Message Bus Topics/Subscriptions (Google Pub/Sub)
- [x] Rudimentary applying of Terraform (GitHub Actions)
- Expected that an external CD system would apply Terraform (e.g. Atlantis) 

Reference implementations (production):

- [x] Front Controller pattern
- [x] Service Layer pattern
- [x] Repository pattern
- [ ] Event Publisher
- [ ] Event Subscriber
- [x] HTTP Client
- [ ] Tracing instrumentation (Micrometer)
- [ ] Metrics instrumentation (Micrometer)

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
