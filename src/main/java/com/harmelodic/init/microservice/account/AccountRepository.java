package com.harmelodic.init.microservice.account;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AccountRepository {

    /**
     * <p>
     * Why JDBCTemplate? Why not use Jakarta Persistence?
     * </p
     * <ol>
     *     <li>
     *     I find Jakarta Persistence just makes the application more complicated,
     *     when you could just learn and used SQL with JDBC.
     *     </li>
     *     <li>
     *     It means we can keep all the Database logic in the Repository,
     *     as intended by the Repository pattern.
     *     </li>
     * </ol>
     */
    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Account> rowMapper = ((rs, rowNum) -> new Account(
            rs.getObject("id", UUID.class),
            rs.getString("name"),
            rs.getObject("customer_id", UUID.class)));

    public Account openAccount(Account account) {
        jdbcTemplate.update("""
                        INSERT INTO account(id, `name`, customer_id)
                        VALUES (?, ?, ?);
                        """,
                account.id(), account.name(), account.customerId());
        return account;
    }

    public List<Account> fetchAllAccounts() {
        return jdbcTemplate.query("SELECT * FROM account;", rowMapper);
    }

    public Account fetchAccountById(UUID id) {
        return jdbcTemplate.queryForObject("SELECT * FROM account WHERE id = ?", Account.class, id);
    }

    public Account updateAccount(Account account) {
        jdbcTemplate.update("""
                        UPDATE account
                        SET `name` = ?, customer_id = ?
                        WHERE id = ?;
                        """,
                account.name(), account.customerId(), account.id());
        return account;
    }

    public void deleteAccountById(UUID id) {
        jdbcTemplate.update("DELETE FROM account WHERE id = ?", id);
    }
}