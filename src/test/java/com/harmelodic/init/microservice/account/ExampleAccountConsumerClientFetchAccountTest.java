package com.harmelodic.init.microservice.account;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Account Service")
class ExampleAccountConsumerClientFetchAccountTest {

    private final Pattern UUID_PATTERN =
            Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    private final Account ACCOUNT_EXAMPLE = new Account(
            UUID.fromString("35e30f1c-bbe3-4ee8-8d1d-d320615e554e"),
            "Matt",
            UUID.fromString("ecaa5fc1-4587-4734-adf1-40cbcbecad8a"));


    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAccountWhenExists(PactDslWithProvider builder) {
        return builder
                .given("Account Exists and is assigned to a Customer")
                .uponReceiving("A valid UUID for an existing account")
                .method("GET")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .willRespondWith()
                .status(200)
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .body(new PactDslJsonBody()
                        .uuid("id", ACCOUNT_EXAMPLE.id())
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchAccountWhenExists")
    void testFetchAccountWhenExists(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        Account receivedAccount = accountClient.fetchAccount(ACCOUNT_EXAMPLE.id());

        assertEquals(ACCOUNT_EXAMPLE, receivedAccount);
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAccountWhenNoCustomerExistsForIt(PactDslWithProvider builder) {
        return builder
                .given("Account Exists but no Customer exists for it")
                .uponReceiving("A valid UUID for an existing account")
                .method("GET")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .willRespondWith()
                .status(500)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchAccountWhenNoCustomerExistsForIt")
    void testFetchAccountWhenNoCustomerExistsForIt(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(WebClientResponseException.InternalServerError.class, () -> accountClient.fetchAccount(ACCOUNT_EXAMPLE.id()));
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAccountButServiceIsUnavailable(PactDslWithProvider builder) {
        return builder
                .given("A Server error")
                .uponReceiving("A valid UUID for an existing account")
                .method("GET")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", "something-invalid"))
                .willRespondWith()
                .status(503)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchAccountButServiceIsUnavailable")
    void testFetchAccountButServiceIsUnavailable(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(WebClientResponseException.ServiceUnavailable.class, () -> accountClient.fetchAccount(ACCOUNT_EXAMPLE.id()));
    }
}
