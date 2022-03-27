package com.harmelodic.init.microservice.account.customer;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class CustomerClient {

    final WebClient webClient;

    CustomerClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://customer-api")
                .build();
    }

    public Customer fetchCustomer(UUID id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/customer/{id}")
                        .build(id.toString()))
                .retrieve()
                .bodyToMono(Customer.class)
                .block();
    }
}
