package com.harmelodic.init.microservice.account.customer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Customer Service")
class CustomerClientTest {

    private final Pattern UUID_PATTERN =
            Pattern.compile("[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}");

    private final Customer CUSTOMER_EXAMPLE = new Customer(
            UUID.fromString("8abc1642-6e61-487d-ba45-bca35cf160c1"),
            "Matt",
            "Smith");

    @Pact(consumer = "Account Service")
    public V4Pact fetchCustomerWhenExists(PactDslWithProvider builder) {
        return builder
                .given("Customer Exists")
                .uponReceiving("A valid UUID for an existing customer")
                .method("GET")
                .matchPath(
                        String.format("/customers/%s", UUID_PATTERN),
                        String.format("/customers/%s", CUSTOMER_EXAMPLE.id()))
                .willRespondWith()
                .status(200)
                .headers(Map.of(
                        "Content-Type", "application/json"
                ))
                .body(newJsonBody(o -> {
                    o.uuid("id", CUSTOMER_EXAMPLE.id());
                    o.stringType("forename", CUSTOMER_EXAMPLE.forename());
                    o.stringType("surname", CUSTOMER_EXAMPLE.surname());
                }).build())
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchCustomerWhenExists")
    void testFetchCustomerWhenExists(MockServer mockServer) {
        CustomerClient customerClient = new CustomerClient(WebClient.builder(), mockServer.getUrl());

        Customer receivedCustomer = customerClient.fetchCustomer(CUSTOMER_EXAMPLE.id());

        assertEquals(CUSTOMER_EXAMPLE, receivedCustomer);
    }

    // ... other test cases for different client responses.
}
