package com.novalabs.digitalbanking.payment.scheduled.dto;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ScheduledPaymentResponse(
        Long id,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        Currency currency,
        Instant scheduledAt,
        ScheduledPaymentStatus status,
        String paymentReference,
        String failureReason
) {
}
