package com.harmelodic.init.microservice.app.structure.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({AccountRepository.class})
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    AccountRepository repository;

    @BeforeEach
    void setUp() {
        jdbcClient.sql("TRUNCATE TABLE account").update();
    }

    @Test
    void openAccount() {
        Account inputAccount = new Account(UUID.randomUUID(), "An Account", UUID.randomUUID());

        assertDoesNotThrow(() -> repository.openAccount(inputAccount));

        List<Account> accountList = jdbcClient.sql("SELECT id, name, customer_id FROM account").query(Account.class).list();

        assertEquals(1, accountList.size());

        Account account = accountList.getFirst();
        assertEquals(inputAccount.id(), account.id());
        assertEquals(inputAccount.name(), account.name());
        assertEquals(inputAccount.customerId(), account.customerId());
    }

    @Test
    void fetchAllAccounts() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account ->
                jdbcClient.sql("INSERT INTO account(id, name, customer_id) VALUES (:id, :name, :customer_id)")
                        .param("id", account.id())
                        .param("name", account.name())
                        .param("customer_id", account.customerId())
                        .update());


        List<Account> retrievedAccounts = assertDoesNotThrow(repository::fetchAllAccounts);

        assertEquals(inputAccounts, retrievedAccounts);
    }

    @Test
    void fetchAccountById() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account ->
                jdbcClient.sql("INSERT INTO account(id, name, customer_id) VALUES (:id, :name, :customer_id)")
                        .param("id", account.id())
                        .param("name", account.name())
                        .param("customer_id", account.customerId())
                        .update()
        );


        Account retrievedAccount = assertDoesNotThrow(() -> repository.fetchAccountById(inputAccounts.get(1).id()));

        assertEquals(inputAccounts.get(1), retrievedAccount);
    }

    @Test
    void updateAccount() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account ->
                jdbcClient.sql("INSERT INTO account(id, name, customer_id) VALUES (:id, :name, :customer_id)")
                        .param("id", account.id())
                        .param("name", account.name())
                        .param("customer_id", account.customerId())
                        .update()
        );

        Account accountToChange = new Account(
                inputAccounts.get(1).id(),
                "Some other name",
                UUID.randomUUID()
        );

        assertDoesNotThrow(() -> repository.updateAccount(accountToChange));
    }

    @Test
    void deleteAccountById() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account ->
                jdbcClient.sql("INSERT INTO account(id, name, customer_id) VALUES (:id, :name, :customer_id)")
                        .param("id", account.id())
                        .param("name", account.name())
                        .param("customer_id", account.customerId())
                        .update()
        );


        assertDoesNotThrow(() -> repository.deleteAccountById(inputAccounts.get(1).id()));

        List<Account> onlyOnesLeft = List.of(
                inputAccounts.get(0),
                inputAccounts.get(2)
        );

        List<Account> accountList = jdbcClient.sql("SELECT id, name, customer_id FROM account").query(Account.class).list();

        assertEquals(onlyOnesLeft, accountList);
    }
}
