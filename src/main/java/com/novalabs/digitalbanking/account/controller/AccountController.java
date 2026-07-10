package com.novalabs.digitalbanking.account.controller;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

//    @PostMapping
//    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
//            @Valid @RequestBody CreateAccountRequest request
//    ){
//        AccountResponse response =
//                accountService.create(request);
//
//    log.info(LogMessages.ACCOUNT_CREATED,
//            account.getAccountNumber());

//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseUtil.created(
//                response, AccountConstants.ACCOUNT_CREATED
//        ));
//    }

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
