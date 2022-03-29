package com.harmelodic.init.microservice.account;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountCreatedPublisher accountCreatedPublisher;

    public AccountService(AccountRepository accountRepository,
                          AccountCreatedPublisher accountCreatedPublisher) {
        this.accountRepository = accountRepository;
        this.accountCreatedPublisher = accountCreatedPublisher;
    }

    public Account openAccount(Account account) {
        Account accountToOpen = new Account(UUID.randomUUID(), account.name(), account.customerId());
        Account createdAccount = accountRepository.openAccount(accountToOpen);
        accountCreatedPublisher.publish(createdAccount);
        return createdAccount;
    }

    public List<Account> fetchAllAccounts() {
        return accountRepository.fetchAllAccounts();
    }

    public Account fetchAccountById(UUID id) {
        return accountRepository.fetchAccountById(id);
    }

    public Account updateAccount(Account account) {
        return accountRepository.updateAccount(account);
    }

    public void deleteAccountById(UUID id) {
        accountRepository.deleteAccountById(id);
    }
}
