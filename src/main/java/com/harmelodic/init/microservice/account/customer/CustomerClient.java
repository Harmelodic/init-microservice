package com.harmelodic.init.microservice.account.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CustomerClient {

    final RestClient restClient;

    CustomerClient(RestClient.Builder builder,
                   @Value("${app.customer.client.baseUrl") String baseUrl) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public Customer fetchCustomer(UUID id) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/customers/{id}")
                        .build(id.toString()))
                .retrieve()
                .body(Customer.class);
    }
}
