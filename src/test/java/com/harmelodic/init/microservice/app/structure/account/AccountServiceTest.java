package com.harmelodic.init.microservice.app.structure.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountCreatedPublisher accountCreatedPublisher;

    @InjectMocks
    AccountService accountService;

    @Test
    void openAccountSuccess() throws AccountRepository.AccountRepositoryException {
        Account account = new Account(null, "Some name", UUID.randomUUID());
        when(accountRepository.openAccount(any(Account.class)))
                .thenAnswer((Answer<Account>) invocationOnMock -> (Account) invocationOnMock.getArguments()[0]);

        Account receivedAccount = assertDoesNotThrow(() -> accountService.openAccount(account));

        assertNotNull(receivedAccount.id());
        assertEquals(account.name(), receivedAccount.name());
        assertEquals(account.customerId(), receivedAccount.customerId());
        verify(accountCreatedPublisher, times(1)).publish(receivedAccount);
    }

    @Test
    void openAccountFail() throws AccountRepository.AccountRepositoryException {
        when(accountRepository.openAccount(isA(Account.class))).thenThrow(AccountRepository.AccountRepositoryException.class);

        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        assertThrows(AccountService.FailedToOpenAccountException.class, () -> accountService.openAccount(account));
        verify(accountCreatedPublisher, times(0)).publish(account);
    }

    @Test
    void fetchAllAccountsSuccess() throws AccountRepository.AccountRepositoryException {
        List<Account> accountList = List.of(
                new Account(UUID.randomUUID(), "Some name", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "Some other name", UUID.randomUUID())
        );
        when(accountRepository.fetchAllAccounts()).thenReturn(accountList);

        List<Account> retrievedAccounts = assertDoesNotThrow(accountService::fetchAllAccounts);

        assertEquals(accountList, retrievedAccounts);
    }

    @Test
    void fetchAllAccountsFail() throws AccountRepository.AccountRepositoryException {
        when(accountRepository.fetchAllAccounts()).thenThrow(AccountRepository.AccountRepositoryException.class);

        assertThrows(AccountService.FailedToFetchAllAccountsException.class, accountService::fetchAllAccounts);
    }

    @Test
    void fetchAccountByIdSuccess() throws AccountRepository.AccountRepositoryException, AccountRepository.AccountDoesNotExistException {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        when(accountRepository.fetchAccountById(account.id())).thenReturn(account);

        Account receivedAccount = assertDoesNotThrow(() -> accountService.fetchAccountById(account.id()));

        assertEquals(account, receivedAccount);
    }

    @Test
    void fetchAccountByIdFail() throws AccountRepository.AccountRepositoryException, AccountRepository.AccountDoesNotExistException {
        UUID uuid = UUID.randomUUID();
        when(accountRepository.fetchAccountById(uuid)).thenThrow(AccountRepository.AccountRepositoryException.class);

        assertThrows(AccountService.FailedToFetchAccountGeneralException.class, () -> accountService.fetchAccountById(uuid));
    }

    @Test
    void updateAccountSuccess() throws AccountRepository.AccountRepositoryException, AccountRepository.AccountDoesNotExistException {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        doNothing().when(accountRepository).updateAccount(account);

        assertDoesNotThrow(() -> accountService.updateAccount(account));
    }

    @Test
    void updateAccountFail() throws AccountRepository.AccountRepositoryException, AccountRepository.AccountDoesNotExistException {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        doThrow(AccountRepository.AccountRepositoryException.class).when(accountRepository).updateAccount(account);

        assertThrows(AccountService.FailedToUpdateAccountException.class, () -> accountService.updateAccount(account));
    }

    @Test
    void deleteAccountByIdSuccess() throws AccountRepository.AccountRepositoryException {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        doNothing().when(accountRepository).deleteAccountById(account.id());

        assertDoesNotThrow(() -> accountService.deleteAccountById(account.id()));

        verify(accountRepository, times(1)).deleteAccountById(account.id());
    }

    @Test
    void deleteAccountByIdFail() throws AccountRepository.AccountRepositoryException {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        doThrow(AccountRepository.AccountRepositoryException.class).when(accountRepository).deleteAccountById(account.id());

        assertThrows(AccountService.FailedToDeleteAccountException.class, () -> accountService.deleteAccountById(account.id()));
    }
}
