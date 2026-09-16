package com.novalabs.digitalbanking.payment.scheduled.controller;

import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import com.novalabs.digitalbanking.identity.security.UserPrincipal;
import com.novalabs.digitalbanking.payment.scheduled.dto.CreateScheduledPaymentRequest;
import com.novalabs.digitalbanking.payment.scheduled.dto.ScheduledPaymentResponse;
import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.mapper.ScheduledPaymentMapper;
import com.novalabs.digitalbanking.payment.scheduled.service.ScheduledPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/scheduled-payments")
@RequiredArgsConstructor
public class ScheduledPaymentController {

    private final ScheduledPaymentService scheduledPaymentService;
    private final ScheduledPaymentMapper scheduledPaymentMapper;
    private final ApiResponseFactory apiResponseFactory;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<ScheduledPaymentResponse>> create(
            @Valid @RequestBody CreateScheduledPaymentRequest request,
            HttpServletRequest servletRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();
        UUID userId = principal.getId();
        ScheduledPayment scheduledPayment = scheduledPaymentService.create(userId, request);
        ScheduledPaymentResponse response = scheduledPaymentMapper.toResponse(scheduledPayment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        apiResponseFactory.ok(
                                response,
                                "Scheduled payment created successfully",
                                servletRequest.getRequestURI()
                        )
                );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<ScheduledPaymentResponse>> get(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {
        ScheduledPayment scheduledPayment =
                scheduledPaymentService.get(id);

        ScheduledPaymentResponse response =
                scheduledPaymentMapper.toResponse(scheduledPayment);

        return ResponseEntity.ok(
                apiResponseFactory.ok(
                        response,
                        "Scheduled payment retrieved successfully",
                        servletRequest.getRequestURI()
                )
        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable Long id,
            HttpServletRequest servletRequest) {

        scheduledPaymentService.cancel(id);

        return ResponseEntity.ok(
                apiResponseFactory.ok(
                        null,
                        "Scheduled payment cancelled successfully",
                        servletRequest.getRequestURI()
                )
        );
    }

}
