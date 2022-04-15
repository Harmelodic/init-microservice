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
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.SERVER_ERROR_WILL_OCCUR;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.UUID_PATTERN;
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

    @Pact(consumer = "MyExampleService")
    public V4Pact updateAccountSuccess(PactDslWithProvider builder) {
        return builder
                .given(ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID, Map.of(
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
                .body(newJsonBody(o -> {
                    o.uuid("id", ACCOUNT_EXAMPLE.id());
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
                .willRespondWith()
                .status(200)
                .body(newJsonBody(o -> {
                    o.uuid("id", ACCOUNT_EXAMPLE.id());
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
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
                .given(ACCOUNT_DOES_NOT_EXIST, Map.of(
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
                .body(newJsonBody(o -> {
                    o.uuid("id", ACCOUNT_EXAMPLE.id());
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
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
                .given(SERVER_ERROR_WILL_OCCUR)
                .uponReceiving("a request to update account")
                .method("PATCH")
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .body(newJsonBody(o -> {
                    o.uuid("id", ACCOUNT_EXAMPLE.id());
                    o.stringType("name", ACCOUNT_EXAMPLE.name());
                    o.uuid("customerId", ACCOUNT_EXAMPLE.customerId());
                }).build())
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
