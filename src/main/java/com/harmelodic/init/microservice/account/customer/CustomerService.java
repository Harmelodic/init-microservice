package com.harmelodic.init.microservice.account.customer;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerClient customerClient;

    public CustomerService(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    public Customer fetchCustomerById(UUID id) {
        return customerClient.fetchCustomer(id);
    }
}
