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

    public void onMessage(SomeMessage message) {
        // Process received messages
        Account account = message.account();
        accountService.openAccount(account);
    }

    record SomeMessage(Account account) {
    }
}
