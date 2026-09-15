package com.novalabs.digitalbanking.payment.scheduled.dto;

import com.novalabs.digitalbanking.account.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateScheduledPaymentRequest(
        @NotNull(message = "Source account ID is required")
        Long sourceAccountId,
        @NotNull(message = "Destination account ID is required")
        Long destinationAccountId,
        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        BigDecimal amount,
        @NotNull(message = "Currency is required")
        Currency currency,
        @NotNull(message = "Scheduled time is required")
        @Future(message = "Scheduled time must be in the future")
        Instant scheduledAt

) {
}
