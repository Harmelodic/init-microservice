package com.harmelodic.init.microservice.account;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    final AccountService accountService;

    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    public Account postAccount(@RequestBody Account account) {
        try {
            return accountService.openAccount(account);
        } catch (AccountService.FailedToOpenAccountException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to open Account", e);
        }
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        try {
            return accountService.fetchAllAccounts();
        } catch (AccountService.FailedToFetchAllAccountsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Accounts", e);
        }
    }

    @GetMapping(path = "/{id}")
    public Account getAccountById(@PathVariable("id") UUID id) {
        try {
            return accountService.fetchAccountById(id);
        } catch (AccountService.FailedToFetchAccountException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get Account", e);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Void> updateAccount(@PathVariable("id") UUID id, @RequestBody Account account) {
        if (id.equals(account.id())) {
            try {
                accountService.updateAccount(account);
            } catch (AccountService.FailedToUpdateAccountException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Account", e);
            }
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID submitted and ID in account is not matching.");
        }
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAccountById(@PathVariable("id") UUID id) {
        try {
            accountService.deleteAccountById(id);
        } catch (AccountService.FailedToDeleteAccountException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete account", e);
        }
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleException() {
        return ResponseEntity.notFound().build();
    }
}
