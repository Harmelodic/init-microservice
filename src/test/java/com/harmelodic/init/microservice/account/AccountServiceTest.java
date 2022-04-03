package com.harmelodic.init.microservice.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    void openAccountSuccess() {
        Account account = new Account(null, "Some name", UUID.randomUUID());
        when(accountRepository.openAccount(any(Account.class))).thenAnswer((Answer<Account>) invocationOnMock -> (Account) invocationOnMock.getArguments()[0]);

        Account receivedAccount = accountService.openAccount(account);

        assertNotNull(receivedAccount.id());
        assertEquals(account.name(), receivedAccount.name());
        assertEquals(account.customerId(), receivedAccount.customerId());
        verify(accountCreatedPublisher, times(1)).publish(receivedAccount);
    }

    @Test
    void openAccountFail() {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        when(accountRepository.openAccount(account)).thenThrow(new RuntimeException("Failed to open account"));

        assertThrows(RuntimeException.class, () -> accountService.openAccount(account));
        verify(accountCreatedPublisher, times(0)).publish(account);
    }

    @Test
    void fetchAllAccountsSuccess() {
        List<Account> accountList = List.of(
                new Account(UUID.randomUUID(), "Some name", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "Some other name", UUID.randomUUID())
        );
        when(accountRepository.fetchAllAccounts()).thenReturn(accountList);

        List<Account> retrievedAccounts = accountService.fetchAllAccounts();

        assertEquals(accountList, retrievedAccounts);
    }

    @Test
    void fetchAllAccountsFail() {
        when(accountRepository.fetchAllAccounts()).thenThrow(new RuntimeException("Failed to fetch Accounts"));

        assertThrows(RuntimeException.class, () -> accountService.fetchAllAccounts());
    }

    @Test
    void fetchAccountByIdSuccess() {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        when(accountRepository.fetchAccountById(account.id())).thenReturn(account);

        Account receivedAccount = accountService.fetchAccountById(account.id());

        assertEquals(account, receivedAccount);
    }

    @Test
    void fetchAccountByIdFail() {
        UUID uuid = UUID.randomUUID();
        when(accountRepository.fetchAccountById(uuid)).thenThrow(new RuntimeException("Failed to open account"));

        assertThrows(RuntimeException.class, () -> accountService.fetchAccountById(uuid));
    }

    @Test
    void updateAccountSuccess() {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        when(accountRepository.updateAccount(account)).thenReturn(account);

        Account receivedAccount = accountService.updateAccount(account);

        assertEquals(account, receivedAccount);
    }

    @Test
    void updateAccountFail() {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        when(accountRepository.updateAccount(account)).thenThrow(new RuntimeException("Failed to open account"));

        assertThrows(RuntimeException.class, () -> accountService.updateAccount(account));
    }

    @Test
    void deleteAccountByIdSuccess() {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        doNothing().when(accountRepository).deleteAccountById(account.id());

        accountService.deleteAccountById(account.id());

        verify(accountRepository, times(1)).deleteAccountById(account.id());
    }

    @Test
    void deleteAccountByIdFail() {
        Account account = new Account(UUID.randomUUID(), "Some name", UUID.randomUUID());
        doThrow(new RuntimeException("Failed to delete")).when(accountRepository).deleteAccountById(account.id());

        assertThrows(RuntimeException.class, () -> accountService.deleteAccountById(account.id()));
    }
}