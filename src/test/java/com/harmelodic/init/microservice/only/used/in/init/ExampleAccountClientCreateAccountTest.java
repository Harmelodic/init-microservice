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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
class ExampleAccountClientCreateAccountTest {

    private final Account ACCOUNT_EXAMPLE = new Account(
            UUID.fromString("35e30f1c-bbe3-4ee8-8d1d-d320615e554e"),
            "Matt",
            UUID.fromString("ecaa5fc1-4587-4734-adf1-40cbcbecad8a"));

    @Pact(consumer = "MyExampleService")
    public V4Pact createAccountSuccess(PactDslWithProvider builder) {
        return builder
                .given("an account does not exist")
                .uponReceiving("a request to create an account")
                .method("POST")
                .path("/accounts")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .body(new PactDslJsonBody()
                        .nullValue("id")
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .uuid("id")
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createAccountSuccess")
    void createAccountSuccessTest(MockServer mockServer) {
        Account newAccount = new Account(null, ACCOUNT_EXAMPLE.name(), ACCOUNT_EXAMPLE.customerId());
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        Account receivedAccount = accountClient.createAccount(newAccount);

        assertNotNull(receivedAccount.id());
        assertEquals(ACCOUNT_EXAMPLE.name(), receivedAccount.name());
        assertEquals(ACCOUNT_EXAMPLE.customerId(), receivedAccount.customerId());
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact createAccountServerFailure(PactDslWithProvider builder) {
        return builder
                .given("A Server Error will occur")
                .uponReceiving("a request to create an account")
                .method("POST")
                .path("/accounts")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .body(new PactDslJsonBody()
                        .nullValue("id")
                        .stringType("name", ACCOUNT_EXAMPLE.name())
                        .uuid("customerId", ACCOUNT_EXAMPLE.customerId()))
                .willRespondWith()
                .status(500)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createAccountServerFailure")
    void createAccountServerFailureTest(MockServer mockServer) {
        Account newAccount = new Account(null, ACCOUNT_EXAMPLE.name(), ACCOUNT_EXAMPLE.customerId());
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(RuntimeException.class, () -> accountClient.createAccount(newAccount));
    }

}
