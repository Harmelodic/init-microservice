package com.harmelodic.init.microservice.only.used.in.init;

import com.harmelodic.init.microservice.account.Account;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * An example client for this Account Service.
 *
 * In the real world, you should not create this in your tests.
 * This is only here to provide a client to the ExampleAccountClient... tests.
 */
public class ExampleAccountClient {

    final WebClient webClient;

    ExampleAccountClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public Account createAccount(Account account) {
        return webClient.post()
                .uri("/accounts")
                .body(Mono.just(account), Account.class)
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
                .body(Mono.just(account), Account.class)
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
