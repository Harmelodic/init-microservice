package com.harmelodic.init.microservice.app.structure.account;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class AccountService {
    private final AccountRepository accountRepository;
    private final AccountCreatedPublisher accountCreatedPublisher;

    AccountService(AccountRepository accountRepository,
                   AccountCreatedPublisher accountCreatedPublisher) {
        this.accountRepository = accountRepository;
        this.accountCreatedPublisher = accountCreatedPublisher;
    }

    @WithSpan
    Account openAccount(Account account) throws AccountServiceException {
        try {
            Account accountToOpen = new Account(UUID.randomUUID(), account.name(), account.customerId());
            Account createdAccount = accountRepository.openAccount(accountToOpen);
            accountCreatedPublisher.publish(createdAccount);
            return createdAccount;
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new AccountServiceException(e);
        }
    }

    @WithSpan
    List<Account> fetchAllAccounts() throws AccountServiceException {
        try {
            return accountRepository.fetchAllAccounts();
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new AccountServiceException(e);
        }
    }

    @WithSpan
    Account fetchAccountById(UUID id) throws AccountDoesNotExistException, AccountServiceException {
        try {
            return accountRepository.fetchAccountById(id);
        } catch (AccountRepository.AccountDoesNotExistException e) {
            throw new AccountDoesNotExistException(e);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new AccountServiceException(e);
        }
    }

    @WithSpan
    void updateAccount(Account account) throws AccountDoesNotExistException, AccountServiceException {
        try {
            accountRepository.updateAccount(account);
        } catch (AccountRepository.AccountDoesNotExistException e) {
            throw new AccountDoesNotExistException(e);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new AccountServiceException(e);
        }
    }

    @WithSpan
    void deleteAccountById(UUID id) throws AccountServiceException {
        try {
            accountRepository.deleteAccountById(id);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new AccountServiceException(e);
        }
    }

    static class AccountDoesNotExistException extends Exception {
        private AccountDoesNotExistException(Throwable throwable) {
            super("Failed to fetch single Account, because it doesn't exist", throwable);
        }
    }

    static class AccountServiceException extends Exception {
        private AccountServiceException(Throwable throwable) {
            super("Account operation failure", throwable);
        }
    }
}
