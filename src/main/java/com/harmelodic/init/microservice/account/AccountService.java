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

    public Account openAccount(Account account) throws FailedToOpenAccountException {
        try {
            Account accountToOpen = new Account(UUID.randomUUID(), account.name(), account.customerId());
            Account createdAccount = accountRepository.openAccount(accountToOpen);
            accountCreatedPublisher.publish(createdAccount);
            return createdAccount;
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToOpenAccountException(e);
        }
    }

    public List<Account> fetchAllAccounts() throws FailedToFetchAllAccountsException {
        try {
            return accountRepository.fetchAllAccounts();
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToFetchAllAccountsException(e);
        }
    }

    public Account fetchAccountById(UUID id) throws FailedToFetchAccountException {
        try {
            return accountRepository.fetchAccountById(id);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToFetchAccountException(e);
        }
    }

    public void updateAccount(Account account) throws FailedToUpdateAccountException {
        try {
            accountRepository.updateAccount(account);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToUpdateAccountException(e);
        }
    }

    public void deleteAccountById(UUID id) throws FailedToDeleteAccountException {
        try {
            accountRepository.deleteAccountById(id);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToDeleteAccountException(e);
        }
    }

    public static class FailedToOpenAccountException extends Exception {
        private FailedToOpenAccountException(Throwable throwable) {
            super("Failed to open Account", throwable);
        }
    }

    public static class FailedToFetchAllAccountsException extends Exception {
        private FailedToFetchAllAccountsException(Throwable throwable) {
            super("Failed to fetch all Accounts", throwable);
        }
    }

    public static class FailedToFetchAccountException extends Exception {
        private FailedToFetchAccountException(Throwable throwable) {
            super("Failed to fetch single Account", throwable);
        }
    }

    public static class FailedToUpdateAccountException extends Exception {
        private FailedToUpdateAccountException(Throwable throwable) {
            super("Failed to update Account", throwable);
        }
    }

    public static class FailedToDeleteAccountException extends Exception {
        public FailedToDeleteAccountException(Throwable e) {
            super("Failed to delete Account", e);
        }
    }
}
