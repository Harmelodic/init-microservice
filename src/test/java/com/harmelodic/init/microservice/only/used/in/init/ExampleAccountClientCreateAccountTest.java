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

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.ACCOUNT_DOES_NOT_EXIST;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.ACCOUNT_EXAMPLE;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.SERVER_ERROR_WILL_OCCUR;
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

    @Pact(consumer = "MyExampleService")
    public V4Pact createAccountSuccess(PactDslWithProvider builder) {
        return builder
                .given(ACCOUNT_DOES_NOT_EXIST)
                .uponReceiving("a request to create an account")
                .method("POST")
                .path("/accounts")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .body(newJsonBody(o -> {
                    o.nullValue("id");
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
                .willRespondWith()
                .status(200)
                .body(newJsonBody(o -> {
                    o.uuid("id");
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
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
                .given(SERVER_ERROR_WILL_OCCUR)
                .uponReceiving("a request to create an account")
                .method("POST")
                .path("/accounts")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .body(newJsonBody(o -> {
                    o.nullValue("id");
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
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
