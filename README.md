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
- [x] Unit Testing for Services
- [x] Provider Contract Testing the Controller (PACT)
- [x] Consumer Contract Testing the HTTP Client (PACT)
- [x] Integration Testing the Repository (in-memory DB)
- [ ] Integration Testing the Event Publisher (Testcontainers)
- [ ] Integration Testing the Event Subscriber (Testcontainers)
- [x] Logback Config
- [x] HTTP Client Config
- [x] Test & Package Automation (Maven, GitHub Actions)
- [x] Packaging into a Container (Jib, Maven)
- [ ] Pushing Container to Registry (Jib, Maven, GitHub Actions)
- [x] Kubernetes resources
- [ ] Deployment to Kubernetes (GitHub Actions)
- [x] Terraform resources (Google)
- [ ] Deployment of Terraform (GitHub Actions)

## Pact

Consumer run:

```shell
mvn clean test pact:publish -Dpact.broker.url=<broker>
```

Provider run:

```shell
mvn clean test -Dpact.verifier.publishResults=true -Dpactbroker.url=<broker>
```

Run all:

```shell
mvn clean test pact:publish -Dpact.broker.url=<broker> -Dpact.verifier.publishResults=true -Dpactbroker.url=<broker>
```
