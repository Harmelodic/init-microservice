package com.harmelodic.init.microservice.account;

import org.springframework.stereotype.Component;

@Component
class AccountCreatedPublisher {

    void publish(Account account) {
        // Publish message to an event bus
    }
}
