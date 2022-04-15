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

import java.util.List;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArray;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.SERVER_ERROR_WILL_OCCUR;
import static com.harmelodic.init.microservice.only.used.in.init.TestConstants.THREE_ACCOUNTS_EXIST;
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
class ExampleAccountClientFetchAllAccountsTest {

    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAllAccountsSuccess(PactDslWithProvider builder) {
        return builder
                .given(THREE_ACCOUNTS_EXIST)
                .uponReceiving("a request for all accounts")
                .method("GET")
                .path("/accounts")
                .willRespondWith()
                .status(200)
                .body(newJsonArray(a -> {
                    a.object(o -> {
                        o.uuid("id");
                        o.stringType("name");
                    });
                    a.object(o -> {
                        o.uuid("id");
                        o.stringType("name");
                    });
                    a.object(o -> {
                        o.uuid("id");
                        o.stringType("name");
                    });
                }).build())
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchAllAccountsSuccess")
    void fetchAllAccountsSuccessTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        List<Account> receivedAccounts = accountClient.fetchAllAccounts();

        assertEquals(3, receivedAccounts.size());
        receivedAccounts.forEach(account -> {
            assertNotNull(account.id());
            assertNotNull(account.name());
            // Don't care about customerId, for example
        });
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAllAccountsServerError(PactDslWithProvider builder) {
        return builder
                .given(SERVER_ERROR_WILL_OCCUR)
                .uponReceiving("a request to fetch all accounts")
                .method("GET")
                .path("/accounts")
                .willRespondWith()
                .status(500)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchAllAccountsServerError")
    void fetchAllAccountsServerErrorTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(RuntimeException.class, accountClient::fetchAllAccounts);
    }
}
