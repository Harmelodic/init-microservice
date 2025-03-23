# init-microservice

A basic template repo that contains starter code for developing a microservice.

Built in Java.

## Showcases

- [x] README
- [x] .gitignore
- [x] Dependency Management (Maven)
- [x] Dependency Injection and Application Mgmt (Spring Boot & Starters)
- [x] Front Controller pattern
- [x] Service Layer pattern
- [x] Repository pattern
- [ ] Event Publisher
- [ ] Event Subscriber
- [x] HTTP Client
- [x] Unit Testing for Services (JUnit)
- [x] Provider Contract Testing the Controller (PACT)
- [x] Consumer Contract Testing the HTTP Client (PACT)
- [x] Automated publishing of Contract Testing Contracts and Results (PACT Broker, GitHub Actions)
- [x] Integration Testing the Repository (in-memory DB)
- [ ] Integration Testing the Event Publisher (Testcontainers)
- [ ] Integration Testing the Event Subscriber (Testcontainers)
- [x] Logging Config (Slf4j & Logback)
- [x] HTTP Client Config
- [x] Test & Package Automation (Maven, GitHub Actions)
- [x] Packaging into a Container (Jib, Maven)
- [x] Pushing Container to Registry (Jib, Maven, GitHub Actions)
- [x] Kubernetes resources (Kustomize)
- [ ] Terraform Database (Google)
- [ ] Terraform Pub/Sub (Google)
- [x] Deployment of Terraform (GitHub Actions)
- [ ] Create Spans for Tracing (OpenTelemetry)
- [ ] Create Domain-logic Metrics (Micrometer)
- [x] Auto-updates (Renovate)
- [x] Docs with MkDocs
- [x] ADRs
- [ ] API Specifications

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
