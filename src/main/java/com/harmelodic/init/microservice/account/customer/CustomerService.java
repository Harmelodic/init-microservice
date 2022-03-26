package com.harmelodic.init.microservice.account.customer;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record CustomerService(CustomerClient customerClient) {
    public Customer fetchCustomerById(UUID id) {
        return customerClient.fetchCustomer(id);
    }
}
