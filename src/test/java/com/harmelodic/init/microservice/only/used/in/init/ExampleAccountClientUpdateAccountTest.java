package com.harmelodic.init.microservice.only.used.in.init;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.harmelodic.init.microservice.account.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * You should NOT create this in your tests.
 * This is only here to provide PACTs for the AccountControllerTest to consume.
 * <p>
 * In the real world, the PACTs will be created by your ACTUAL Consumers.
 * Testing a Controller with your own Client not how Consumer-driven Contract Testing works.
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Account Service")
class ExampleAccountClientUpdateAccountTest {

    private final Pattern UUID_PATTERN =
            Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    private final Account ACCOUNT_EXAMPLE = new Account(
            UUID.fromString("35e30f1c-bbe3-4ee8-8d1d-d320615e554e"),
            "Matt",
            UUID.fromString("ecaa5fc1-4587-4734-adf1-40cbcbecad8a"));

    @Pact(consumer = "MyExampleService")
    public V4Pact updateAccountSuccess(PactDslWithProvider builder) {
        return builder
                .given("an account exists with ID and name", Map.of(
                        "id", ACCOUNT_EXAMPLE.id().toString(),
                        "name", "Some name before update",
                        "customerId", ACCOUNT_EXAMPLE.customerId().toString()
                ))
                .uponReceiving("a request to update account")
                .method("PATCH")
                .headers(Map.of(
                        "Content-Type", "application/json"))
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .body(new PactDslJsonBody()
                        .uuid("id", ACCOUNT_EXAMPLE.id())
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .uuid("id", ACCOUNT_EXAMPLE.id())
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "updateAccountSuccess")
    void updateAccountSuccessTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        Account receivedAccount = accountClient.updateAccount(ACCOUNT_EXAMPLE);

        assertEquals(ACCOUNT_EXAMPLE, receivedAccount);
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact updateAccountThatDoesNotExist(PactDslWithProvider builder) {
        return builder
                .given("an account with ID does not exist", Map.of(
                        "id", ACCOUNT_EXAMPLE.id().toString()
                ))
                .uponReceiving("a request to update account")
                .method("PATCH")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .body(new PactDslJsonBody()
                        .uuid("id", ACCOUNT_EXAMPLE.id())
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .willRespondWith()
                .status(404)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "updateAccountThatDoesNotExist")
    void updateAccountThatDoesNotExistTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(RuntimeException.class, () -> accountClient.updateAccount(ACCOUNT_EXAMPLE));
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact updateAccountServerError(PactDslWithProvider builder) {
        return builder
                .given("A Server Error will occur")
                .uponReceiving("a request to update account")
                .method("PATCH")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .body(new PactDslJsonBody()
                        .uuid("id", ACCOUNT_EXAMPLE.id())
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .willRespondWith()
                .status(500)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "updateAccountServerError")
    void updateAccountServerErrorTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(RuntimeException.class, () -> accountClient.updateAccount(ACCOUNT_EXAMPLE));
    }
}
