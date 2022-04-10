package com.harmelodic.init.microservice.only.used.in.init;

import au.com.dius.pact.consumer.MockServer;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
class ExampleAccountClientDeleteAccountTest {

    private final Pattern UUID_PATTERN =
            Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    private final Account ACCOUNT_EXAMPLE = new Account(
            UUID.fromString("35e30f1c-bbe3-4ee8-8d1d-d320615e554e"),
            "Matt",
            UUID.fromString("ecaa5fc1-4587-4734-adf1-40cbcbecad8a"));

    @Pact(consumer = "MyExampleService")
    public V4Pact deleteAccountThatExists(PactDslWithProvider builder) {
        return builder
                .given("an account exists with ID", Map.of(
                        "id", ACCOUNT_EXAMPLE.id().toString()
                ))
                .uponReceiving("a request to delete the account")
                .method("DELETE")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .willRespondWith()
                .status(200)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "deleteAccountThatExists")
    void deleteAccountThatExistsTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertDoesNotThrow(() -> accountClient.deleteAccount(ACCOUNT_EXAMPLE.id()));
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact deleteAccountThatDoesNotExist(PactDslWithProvider builder) {
        return builder
                .given("An account does not exist")
                .uponReceiving("A request to delete an account")
                .method("DELETE")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .willRespondWith()
                .status(404)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "deleteAccountThatDoesNotExist")
    void deleteAccountThatDoesNotExistTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(RuntimeException.class, () -> accountClient.deleteAccount(ACCOUNT_EXAMPLE.id()));
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact deleteAccountServerError(PactDslWithProvider builder) {
        return builder
                .given("A Server Error will occur")
                .uponReceiving("a request to delete an account")
                .method("DELETE")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .willRespondWith()
                .status(500)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "deleteAccountServerError")
    void deleteAccountServerErrorTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(RuntimeException.class, () -> accountClient.deleteAccount(ACCOUNT_EXAMPLE.id()));
    }
}
