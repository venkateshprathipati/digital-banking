package com.novalabs.digitalbanking.account.controller;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService  accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account account) {
        return accountService.save(account);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@RequestBody Account account) {
        return accountService.save(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @GetMapping("/number/{accountNumber}")
    public Account getByAccountNumber(@PathVariable String accountNumber) {

        return accountService.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
