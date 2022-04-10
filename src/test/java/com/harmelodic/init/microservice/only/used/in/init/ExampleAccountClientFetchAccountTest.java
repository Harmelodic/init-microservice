package com.harmelodic.init.microservice.only.used.in.init;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
class ExampleAccountClientFetchAccountTest {

    private final Pattern UUID_PATTERN =
            Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

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

    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAllAccountsSuccess(PactDslWithProvider builder) {
        return builder
                .given("3 Accounts exist")
                .uponReceiving("a request for all accounts")
                .method("GET")
                .path("/accounts")
                .willRespondWith()
                .status(200)
                .body(Objects.requireNonNull(new PactDslJsonArray()
                        .object()
                        .uuid("id")
                        .stringType("name")
                        .closeObject()
                        .object()
                        .uuid("id")
                        .stringType("name")
                        .closeObject()
                        .object()
                        .uuid("id", UUID.randomUUID())
                        .stringType("name")
                        .closeObject())
                )
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
                .given("A Server Error will occur")
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
    void fetchAccountWhenExistsTest(MockServer mockServer) {
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
    void fetchAccountWhenNoCustomerExistsForItTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(WebClientResponseException.InternalServerError.class, () -> accountClient.fetchAccount(ACCOUNT_EXAMPLE.id()));
    }

    @Pact(consumer = "MyExampleService")
    public V4Pact fetchAccountButServiceIsUnavailable(PactDslWithProvider builder) {
        return builder
                .given("A Server error will occur")
                .uponReceiving("A valid UUID for an existing account")
                .method("GET")
                .matchPath(
                        String.format("/accounts/%s", UUID_PATTERN),
                        String.format("/accounts/%s", ACCOUNT_EXAMPLE.id()))
                .willRespondWith()
                .status(503)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "fetchAccountButServiceIsUnavailable")
    void fetchAccountButServiceIsUnavailableTest(MockServer mockServer) {
        ExampleAccountClient accountClient = new ExampleAccountClient(WebClient.builder(), mockServer.getUrl());

        assertThrows(WebClientResponseException.ServiceUnavailable.class, () -> accountClient.fetchAccount(ACCOUNT_EXAMPLE.id()));
    }

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
