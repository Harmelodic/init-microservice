package com.harmelodic.init.microservice.app.structure.account;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
class AccountRepository {

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

    AccountRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Account openAccount(Account account) throws AccountRepositoryException {
        try {
            jdbcClient.sql("INSERT INTO account(id, name, customer_id) VALUES (:id, :name, :customer_id)")
                    .param("id", account.id())
                    .param("name", account.name())
                    .param("customer_id", account.customerId())
                    .update();
            return account;
        } catch (DataAccessException e) {
            throw new AccountRepositoryException("Failed to open account in Database", e);
        }
    }

    List<Account> fetchAllAccounts() throws AccountRepositoryException {
        try {
            return jdbcClient.sql("SELECT id, name, customer_id FROM account")
                    .query(Account.class)
                    .list();
        } catch (DataAccessException e) {
            throw new AccountRepositoryException("Failed to fetch all accounts", e);
        }
    }

    Account fetchAccountById(UUID id) throws AccountDoesNotExistException, AccountRepositoryException {
        try {
            return jdbcClient.sql("SELECT id, name, customer_id FROM account WHERE id = :id")
                    .param("id", id)
                    .query(Account.class)
                    .single();
        } catch (EmptyResultDataAccessException e) {
            throw new AccountDoesNotExistException("No account exists for this ID", e);
        } catch (DataAccessException e) {
            throw new AccountRepositoryException("Failed to fetch account", e);
        }
    }

    void updateAccount(Account account) throws AccountDoesNotExistException, AccountRepositoryException {
        try {
            jdbcClient.sql("SELECT id, name, customer_id FROM account WHERE id = :id")
                    .param("id", account.id())
                    .query(Account.class)
                    .single(); // Throws EmptyResultDataAccessException if Account it doesn't exist.
            jdbcClient.sql("UPDATE account SET name = :name, customer_id = :customer_id WHERE id = :id")
                    .param("name", account.name())
                    .param("customer_id", account.customerId())
                    .param("id", account.id())
                    .update();
        } catch (EmptyResultDataAccessException e) {
            throw new AccountDoesNotExistException("Account does not exist", e);
        } catch (DataAccessException e) {
            throw new AccountRepositoryException("Failed to update account", e);
        }
    }

    void deleteAccountById(UUID id) throws AccountRepositoryException {
        try {
            jdbcClient.sql("DELETE FROM account WHERE id = :id")
                    .param("id", id)
                    .update();
        } catch (DataAccessException e) {
            throw new AccountRepositoryException("Failed to delete account", e);
        }
    }

    static class AccountDoesNotExistException extends Exception {
        private AccountDoesNotExistException(String message, Throwable e) {
            super(message, e);
        }
    }

    static class AccountRepositoryException extends Exception {
        private AccountRepositoryException(String message, Throwable e) {
            super(message, e);
        }
    }
}
