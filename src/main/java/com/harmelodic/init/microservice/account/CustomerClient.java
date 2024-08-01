package com.harmelodic.init.microservice.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CustomerClient {

    private final RestClient restClient;

    CustomerClient(RestClient.Builder builder,
                   @Value("${app.customer.client.baseUrl") String baseUrl) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public Customer fetchCustomer(UUID id) throws FailedToFetchCustomerException {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/customers/{id}")
                            .build(id.toString()))
                    .retrieve()
                    .body(Customer.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new FailedToFetchCustomerException(e);
        }
    }

    public static class FailedToFetchCustomerException extends Exception {
        public FailedToFetchCustomerException(Throwable e) {
            super("Failed to fetch Customer from customer API", e);
        }
    }
}
