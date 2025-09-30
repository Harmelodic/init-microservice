package com.harmelodic.init.microservice.app.structure.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
class AccountController {

    private final AccountService accountService;

    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    Account postAccount(@RequestBody Account account) {
        try {
            return accountService.openAccount(account);
        } catch (AccountService.AccountServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to open Account", e);
        }
    }

    @GetMapping
    List<Account> getAllAccounts() {
        try {
            return accountService.fetchAllAccounts();
        } catch (AccountService.AccountServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Accounts", e);
        }
    }

    @GetMapping(path = "/{id}")
    Account getAccountById(@PathVariable("id") UUID id) {
        try {
            return accountService.fetchAccountById(id);
        } catch (AccountService.AccountDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist", e);
        } catch (AccountService.AccountServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get Account", e);
        }
    }

    @PatchMapping(path = "/{id}")
    ResponseEntity<Void> updateAccount(@PathVariable("id") UUID id, @RequestBody Account account) {
        if (!id.equals(account.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID submitted and ID in account is not matching.");
        }

        try {
            accountService.updateAccount(account);
            return ResponseEntity.ok().build();
        } catch (AccountService.AccountDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update Account, as it does not exist", e);
        } catch (AccountService.AccountServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Account", e);
        }
    }

    @DeleteMapping(path = "/{id}")
    void deleteAccountById(@PathVariable("id") UUID id) {
        try {
            accountService.deleteAccountById(id);
        } catch (AccountService.AccountServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete account", e);
        }
    }
}
