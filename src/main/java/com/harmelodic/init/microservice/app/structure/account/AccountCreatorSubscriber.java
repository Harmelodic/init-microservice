package com.harmelodic.init.microservice.app.structure.account;

import org.springframework.stereotype.Component;

@Component
class AccountCreatorSubscriber {

    private final AccountService accountService;

    AccountCreatorSubscriber(AccountService accountService) {
        this.accountService = accountService;
    }

    void subscribe() {
        // Subscribe to an event bus
    }

    void onReceiveMessage(SomeMessage message) {
        // Process received messages
        Account account = message.account();
        try {
            accountService.openAccount(account);
        } catch (AccountService.AccountServiceException e) { // NOPMD
            // Nack message or save it somewhere to deal with
        }
    }

    record SomeMessage(Account account) {
    }
}
