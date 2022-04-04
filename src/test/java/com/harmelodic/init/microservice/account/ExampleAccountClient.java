package com.harmelodic.init.microservice.account;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

public class ExampleAccountClient {

    final WebClient webClient;

    ExampleAccountClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public Account createAccount(Account account) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts")
                        .build(account))
                .body(account, Account.class)
                .retrieve()
                .bodyToMono(Account.class)
                .block();
    }

    public List<Account> fetchAllAccounts() {
        return webClient.get()
                .uri("/accounts")
                .retrieve()
                .bodyToFlux(Account.class)
                .collectList()
                .block();
    }

    public Account fetchAccount(UUID id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts/{id}")
                        .build(id.toString()))
                .retrieve()
                .bodyToMono(Account.class)
                .block();
    }

    public Account updateAccount(Account account) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts/{id}")
                        .build(account.id()))
                .body(account, Account.class)
                .retrieve()
                .bodyToMono(Account.class)
                .block();
    }

    public void deleteAccount(UUID accountId) {
        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts/{id}")
                        .build(accountId))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
