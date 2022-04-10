package com.harmelodic.init.microservice.account;

import com.harmelodic.init.microservice.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        Application.class,
        AccountRepository.class
})
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository repository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("""
                TRUNCATE TABLE account;
                    """);
    }

    final RowMapper<Account> accountMapper = ((rs, rowNum) -> new Account(
            rs.getObject("id", UUID.class),
            rs.getString("name"),
            rs.getObject("customer_id", UUID.class)
    ));

    @Test
    void openAccount() {
        Account inputAccount = new Account(UUID.randomUUID(), "An Account", UUID.randomUUID());

        repository.openAccount(inputAccount);

        List<Account> accountList = jdbcTemplate.query("SELECT * FROM account;", accountMapper);

        assertEquals(1, accountList.size());

        Account account = accountList.get(0);
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
        inputAccounts.forEach(account -> jdbcTemplate.update("""
                        INSERT INTO account(id, `name`, customer_id)
                        VALUES (?, ?, ?)
                        """,
                account.id(), account.name(), account.customerId()));


        List<Account> retrievedAccounts = repository.fetchAllAccounts();

        assertEquals(inputAccounts, retrievedAccounts);
    }

    @Test
    void fetchAccountById() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account -> jdbcTemplate.update("""
                        INSERT INTO account(id, `name`, customer_id)
                        VALUES (?, ?, ?)
                        """,
                account.id(), account.name(), account.customerId()));


        Account retrievedAccount = repository.fetchAccountById(inputAccounts.get(1).id());

        assertEquals(inputAccounts.get(1), retrievedAccount);
    }

    @Test
    void updateAccount() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account -> jdbcTemplate.update("""
                        INSERT INTO account(id, `name`, customer_id)
                        VALUES (?, ?, ?)
                        """,
                account.id(), account.name(), account.customerId()));

        Account accountToChange = new Account(
                inputAccounts.get(1).id(),
                "Some other name",
                UUID.randomUUID()
        );

        Account retrievedAccount = repository.updateAccount(accountToChange);

        assertEquals(accountToChange, retrievedAccount);
    }

    @Test
    void deleteAccountById() {
        List<Account> inputAccounts = List.of(
                new Account(UUID.randomUUID(), "An Account", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 2", UUID.randomUUID()),
                new Account(UUID.randomUUID(), "An Account 3", UUID.randomUUID())
        );
        inputAccounts.forEach(account -> jdbcTemplate.update("""
                        INSERT INTO account(id, `name`, customer_id)
                        VALUES (?, ?, ?)
                        """,
                account.id(), account.name(), account.customerId()));


        repository.deleteAccountById(inputAccounts.get(1).id());

        List<Account> onlyOnesLeft = List.of(
                inputAccounts.get(0),
                inputAccounts.get(2)
        );

        List<Account> accountList = jdbcTemplate.query("SELECT * FROM account;", accountMapper);

        assertEquals(onlyOnesLeft, accountList);
    }
}