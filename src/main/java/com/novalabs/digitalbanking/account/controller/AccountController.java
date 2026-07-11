package com.novalabs.digitalbanking.account.controller;

import com.novalabs.digitalbanking.account.dto.AccountResponse;
import com.novalabs.digitalbanking.account.dto.CreateAccountRequest;
import com.novalabs.digitalbanking.account.dto.UpdateAccountRequest;
import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.service.AccountService;
import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final ApiResponseFactory factory;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(
            @Valid @RequestBody CreateAccountRequest request,
            HttpServletRequest servletRequest) {
        AccountResponse response = accountService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(factory.created(
                                response, "Account created successfully",
                                servletRequest.getRequestURI()
                        )
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request,
            HttpServletRequest servletRequest) {
        AccountResponse response = accountService.update(id, request);
        return ResponseEntity.ok(
                factory.ok(
                        response,
                        "Account updated successfully",
                        servletRequest.getRequestURI()
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts(HttpServletRequest servletRequest) {
        List<AccountResponse> accounts = accountService.findAll();
        return ResponseEntity.ok(
                factory.ok(
                        accounts,
                        "Accounts fetched successfully",
                        servletRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getById(@PathVariable Long id, HttpServletRequest request) {
        AccountResponse account = accountService.findById(id);
        return ResponseEntity.ok(
                factory.ok(
                        account, "Account fetched successfully", request.getRequestURI()
                )
        );
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getByAccountNumber(@PathVariable String accountNumber,HttpServletRequest request) {
        AccountResponse account = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(
                factory.ok(
                        account,
                        "Account fetched successfully",
                        request.getRequestURI()
                )
        );
    }
}
