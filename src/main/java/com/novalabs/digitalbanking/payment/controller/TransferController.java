package com.novalabs.digitalbanking.payment.controller;

import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.service.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final ApiResponseFactory factory;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(
            @Valid @RequestBody TransferRequest request,
            HttpServletRequest servletRequest
    ) {
        TransferResponse response = transferService.transfer(request);

        return ResponseEntity.ok(
                factory.ok(
                        response,
                        "Transfer completed successfully",
                        servletRequest.getRequestURI()
                )
        );
    }
}
