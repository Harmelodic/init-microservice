package com.harmelodic.init.microservice.mock.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

/**
 * An example client for this Account Service.
 * <p>
 * In the real world, you should not create this in your tests.
 * This is only here to provide a client to the ExampleAccountClient... tests.
 */
class ExampleAccountClient {

    final RestClient restClient;

    ExampleAccountClient(RestClient.Builder builder, String baseUrl) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    Account createAccount(Account account) {
        return restClient.post()
                .uri("/accounts")
                .body(account)
                .retrieve()
                .body(Account.class);
    }

    List<Account> fetchAllAccounts() {
        return restClient.get()
                .uri("/accounts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    Account fetchAccount(UUID id) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts/{id}")
                        .build(id.toString()))
                .retrieve()
                .body(Account.class);
    }

    HttpStatusCode updateAccount(Account account) {
        return restClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts/{id}")
                        .build(account.id()))
                .body(account)
                .retrieve()
                .toEntity(Void.class)
                .getStatusCode();
    }

    void deleteAccount(UUID accountId) {
        restClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts/{id}")
                        .build(accountId))
                .retrieve()
                .toBodilessEntity();
    }
}
