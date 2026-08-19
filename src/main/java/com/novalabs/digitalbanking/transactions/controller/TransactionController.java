package com.novalabs.digitalbanking.transactions.controller;

import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import com.novalabs.digitalbanking.transactions.dto.TransactionResponse;
import com.novalabs.digitalbanking.transactions.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ApiResponseFactory factory;

    /**
     * Get transaction history for an account.
     */
    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAccountTransactions(
            @PathVariable Long accountId,
            HttpServletRequest request
    ) {
        List<TransactionResponse> transactions =
                transactionService.findByAccountId(accountId);

        return ResponseEntity.ok(
                factory.ok(
                        transactions,
                        "Transaction history fetched successfully",
                        request.getRequestURI()
                )
        );
    }

    /**
     *
     * Get transaction by internal ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {
        TransactionResponse transaction = transactionService.findById(id);
        return ResponseEntity.ok(
                factory.ok(
                        transaction,
                        "Transaction fetched successfully",
                        servletRequest.getRequestURI()
                )
        );
    }

    /**
     * Get transaction by external business reference.
     * <p>
     * Example:
     * GET /v1/transactions/reference/TXN-20260819-000001
     */
    @GetMapping("/reference/{transactionReference}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<TransactionResponse>> getByReference(
            @PathVariable String transactionReference, HttpServletRequest request) {
        TransactionResponse transaction = transactionService.findByReference(transactionReference);
        return ResponseEntity.ok(
                factory.ok(
                        transaction,
                        "Transaction fetched successfully",
                        request.getRequestURI()
                )
        );
    }
}
