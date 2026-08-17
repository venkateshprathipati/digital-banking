package com.novalabs.digitalbanking.payment.controller;

import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import com.novalabs.digitalbanking.payment.dto.PaymentHistoryPageResponse;
import com.novalabs.digitalbanking.payment.service.PaymentHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentHistoryController {

    private final PaymentHistoryService paymentHistoryService;
    private final ApiResponseFactory factory;

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<PaymentHistoryPageResponse>> getPaymentHistory(
            @RequestParam Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest servletRequest
    ){
        PaymentHistoryPageResponse response = paymentHistoryService.getPaymentHistory(
                accountId, page, size);

        return ResponseEntity.ok(
                factory.ok(
                        response,
                        "Payment history retrieved successfully",
                        servletRequest.getRequestURI()
                )
        );
    }
}
