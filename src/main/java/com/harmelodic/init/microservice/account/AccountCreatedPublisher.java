package com.harmelodic.init.microservice.account;

import org.springframework.stereotype.Component;

@Component
public class AccountCreatedPublisher {

    public void publish(Account account) {
        // Publish message to an event bus
    }
}
