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
- [ ] Pushing Container to Registry (Jib, Maven, GitHub Actions)
- [x] Kubernetes resources
- [ ] Deployment to Kubernetes (GitHub Actions)
- [x] Terraform resources (Google)
- [ ] Deployment of Terraform (GitHub Actions)
- [ ] Create Spans for Tracing (OpenTelemetry)
- [ ] Create Domain-logic Metrics (Micrometer)
- [x] Auto-updates (Renovate)

## Pact

Pact is bound to Maven's `test` and `deploy` phases:

Running the Pact tests. The `pactbroker.url` property is needed for Pact Provider tests:

```shell
mvn test -Dpactbroker.url=<broker>
```

Publish Consumer Pacts and Provider results:

```shell
mvn deploy \
  -Dpactbroker.url=<broker> \
  -Dpact.broker.url=<broker> \
  -Dpact.verifier.publishResults=true
```
