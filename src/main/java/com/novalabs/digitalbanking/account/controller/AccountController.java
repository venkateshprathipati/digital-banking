package com.novalabs.digitalbanking.account.controller;

import com.novalabs.digitalbanking.account.dto.*;
import com.novalabs.digitalbanking.account.service.AccountService;
import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.novalabs.digitalbanking.common.constants.AccountConstants.*;


@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final ApiResponseFactory factory;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> create(
            @Valid @RequestBody CreateAccountRequest request,
            HttpServletRequest servletRequest) {
        AccountResponse response = accountService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(factory.created(
                                response, ACCOUNT_CREATED,
                                servletRequest.getRequestURI()
                        )
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request,
            HttpServletRequest servletRequest) {
        AccountResponse response = accountService.update(id, request);
        return ResponseEntity.ok(
                factory.ok(
                        response,
                        ACCOUNT_UPDATED,
                        servletRequest.getRequestURI()
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts(HttpServletRequest servletRequest) {
        List<AccountResponse> accounts = accountService.findAll();
        return ResponseEntity.ok(
                factory.ok(
                        accounts,
                        ACCOUNT_FETCHED,
                        servletRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> getById(@PathVariable Long id, HttpServletRequest request) {
        AccountResponse account = accountService.findById(id);
        return ResponseEntity.ok(
                factory.ok(
                        account, ACCOUNT_FETCHED, request.getRequestURI()
                )
        );
    }

    @GetMapping("/account-number/{accountNumber}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> getByAccountNumber(@PathVariable String accountNumber, HttpServletRequest request) {
        AccountResponse account = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(
                factory.ok(
                        account,
                        ACCOUNT_FETCHED,
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> deposit(
            @PathVariable Long id,
            @Valid @RequestBody DepositRequest request,
            HttpServletRequest servletRequest) {
        AccountResponse response = accountService.deposit(id, request);
        return ResponseEntity.ok(factory.ok(
                response, "Amount deposited successfully", servletRequest.getRequestURI()
        ));
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> withdraw(
            @PathVariable Long id,
            @Valid @RequestBody WithdrawRequest request,
            HttpServletRequest servletRequest
    ) {
        AccountResponse response = accountService.withdraw(id, request);
        return ResponseEntity.ok(
                factory.ok(
                        response,
                        "Amount withdrawn successfully",
                        servletRequest.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> freeze(
            @PathVariable Long id, HttpServletRequest servletRequest
    ) {
        AccountResponse response = accountService.freeze(id);

        return ResponseEntity.ok(
                factory.ok(
                        response,
                        "Account frozen successfully",
                        servletRequest.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/unfreeze")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> unfreeze(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {
        AccountResponse response = accountService.unfreeze(id);
        return ResponseEntity.ok(
                factory.ok(
                        response,
                        "Account unfrozen successfully",
                        servletRequest.getRequestURI()
                )
        );
    }
}
