package com.harmelodic.init.microservice.account;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AccountRepository {

    public Account openAccount(Account account) {
        return account;
    }

    public List<Account> fetchAllAccounts() {
        return List.of();
    }

    public Account fetchAccountById(UUID id) {
        return null;
    }

    public Account updateAccount(Account account) {
        return account;
    }

    public void deleteAccountById(UUID id) {
        // DELETE FROM account WHERE id = ${id};
    }
}
