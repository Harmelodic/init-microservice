package com.harmelodic.init.microservice.account;

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
    Account openAccount(Account account) throws FailedToOpenAccountException {
        try {
            Account accountToOpen = new Account(UUID.randomUUID(), account.name(), account.customerId());
            Account createdAccount = accountRepository.openAccount(accountToOpen);
            accountCreatedPublisher.publish(createdAccount);
            return createdAccount;
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToOpenAccountException(e);
        }
    }

    @WithSpan
    List<Account> fetchAllAccounts() throws FailedToFetchAllAccountsException {
        try {
            return accountRepository.fetchAllAccounts();
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToFetchAllAccountsException(e);
        }
    }

    @WithSpan
    Account fetchAccountById(UUID id) throws FailedToFetchAccountItDoesNotExistException, FailedToFetchAccountGeneralException {
        try {
            return accountRepository.fetchAccountById(id);
        } catch (AccountRepository.AccountDoesNotExistException e) {
            throw new FailedToFetchAccountItDoesNotExistException(e);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToFetchAccountGeneralException(e);
        }
    }

    @WithSpan
    void updateAccount(Account account) throws FailedToUpdateAccountItDoesNotExistException, FailedToUpdateAccountException {
        try {
            accountRepository.updateAccount(account);
        } catch (AccountRepository.AccountDoesNotExistException e) {
            throw new FailedToUpdateAccountItDoesNotExistException(e);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToUpdateAccountException(e);
        }
    }

    @WithSpan
    void deleteAccountById(UUID id) throws FailedToDeleteAccountException {
        try {
            accountRepository.deleteAccountById(id);
        } catch (AccountRepository.AccountRepositoryException e) {
            throw new FailedToDeleteAccountException(e);
        }
    }

    static class FailedToOpenAccountException extends Exception {
        private FailedToOpenAccountException(Throwable throwable) {
            super("Failed to open Account", throwable);
        }
    }

    static class FailedToFetchAllAccountsException extends Exception {
        private FailedToFetchAllAccountsException(Throwable throwable) {
            super("Failed to fetch all Accounts", throwable);
        }
    }

    static class FailedToFetchAccountItDoesNotExistException extends Exception {
        private FailedToFetchAccountItDoesNotExistException(Throwable throwable) {
            super("Failed to fetch single Account, because it doesn't exist", throwable);
        }
    }

    static class FailedToFetchAccountGeneralException extends Exception {
        private FailedToFetchAccountGeneralException(Throwable throwable) {
            super("Failed to fetch single Account", throwable);
        }
    }

    static class FailedToUpdateAccountItDoesNotExistException extends Exception {
        private FailedToUpdateAccountItDoesNotExistException(Throwable throwable) {
            super("Failed to fetch single Account, because it doesn't exist", throwable);
        }
    }

    static class FailedToUpdateAccountException extends Exception {
        private FailedToUpdateAccountException(Throwable throwable) {
            super("Failed to update Account", throwable);
        }
    }

    static class FailedToDeleteAccountException extends Exception {
        public FailedToDeleteAccountException(Throwable e) {
            super("Failed to delete Account", e);
        }
    }
}
