package com.harmelodic.init.microservice.account.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class CustomerClient {

    final WebClient webClient;

    CustomerClient(WebClient.Builder builder,
                   @Value("${app.customer.client.baseUrl") String baseUrl) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public Customer fetchCustomer(UUID id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/customers/{id}")
                        .build(id.toString()))
                .retrieve()
                .bodyToMono(Customer.class)
                .block();
    }
}
