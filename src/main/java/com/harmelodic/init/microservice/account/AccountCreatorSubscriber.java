package com.harmelodic.init.microservice.account;

import org.springframework.stereotype.Component;

@Component
public class AccountCreatorSubscriber {
    private final AccountService accountService;

    public AccountCreatorSubscriber(AccountService accountService) {
        this.accountService = accountService;
    }

    public void subscribe() {
        // Subscribe to an event bus
    }

    public void onReceiveMessage(SomeMessage message) {
        // Process received messages
        Account account = message.account();
        try {
            accountService.openAccount(account);
        } catch (AccountService.FailedToOpenAccountException e) { // NOPMD
            // Nack message or save it somewhere to deal with
        }
    }

    public record SomeMessage(Account account) {
    }
}
