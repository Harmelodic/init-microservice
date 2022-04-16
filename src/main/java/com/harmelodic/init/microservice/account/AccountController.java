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
        return accountService.openAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.fetchAllAccounts();
    }

    @GetMapping(path = "/{id}")
    public Account getAccountById(@PathVariable UUID id) {
        return accountService.fetchAccountById(id);
    }

    @PatchMapping(path = "/{id}")
    public Account updateAccount(@PathVariable UUID id, @RequestBody Account account) {
        if (id.equals(account.id())) {
            return accountService.updateAccount(account);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID submitted and ID in account is not matching.");
        }
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAccountById(@PathVariable UUID id) {
        accountService.deleteAccountById(id);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleException() {
        return ResponseEntity.notFound().build();
    }
}
