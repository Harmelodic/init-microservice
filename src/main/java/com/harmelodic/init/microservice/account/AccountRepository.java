package com.harmelodic.init.microservice.account;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepository {

    /**
     * <p>
     * Why JDBC? Why not use Jakarta Persistence?
     * </p
     * <ol>
     *     <li>
     *     I find Jakarta Persistence just makes the application more complicated,
     *     when you could just learn and use SQL, with JDBC.
     *     </li>
     *     <li>
     *     It means we can keep all the Database logic in the Repository,
     *     as intended by the Repository pattern, rather than putting Database logic throughout the application.
     *     </li>
     *     <li>
     *     If the Repository needs refactoring to a NoSQL database, or a different type of SQL database,
     *     we have just one place to edit the Database logic. Which is, again, an intention of the Repository pattern.
     *     </li>
     * </ol>
     */
    private final JdbcClient jdbcClient;

    public AccountRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Account openAccount(Account account) {
        jdbcClient.sql("INSERT INTO account(id, name, customer_id) VALUES (:id, :name, :customer_id)")
                .param("id", account.id())
                .param("name", account.name())
                .param("customer_id", account.customerId())
                .update();
        return account;
    }

    public List<Account> fetchAllAccounts() {
        return jdbcClient.sql("SELECT id, name, customer_id FROM account;").query(Account.class).list();
    }

    public Account fetchAccountById(UUID id) {
        return jdbcClient.sql("SELECT id, name, customer_id FROM account WHERE id = :id")
                .param("id", id)
                .query(Account.class)
                .single();
    }

    public Account updateAccount(Account account) {
        Optional<Account> optionalAccount = jdbcClient.sql("SELECT id, name, customer_id FROM account WHERE id = :id")
                .param("id", account.id())
                .query(Account.class)
                .optional();
        if (optionalAccount.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        jdbcClient.sql("UPDATE account SET name = :name, customer_id = :customer_id WHERE id = :id;")
                .param("name", account.name())
                .param("customer_id", account.customerId())
                .param("id", account.id());
        return account;
    }

    public void deleteAccountById(UUID id) {
        jdbcClient.sql("DELETE FROM account WHERE id = :id")
                .param("id", id)
                .update();
    }
}
