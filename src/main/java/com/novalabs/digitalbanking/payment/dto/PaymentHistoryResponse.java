package com.novalabs.digitalbanking.payment.dto;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        String paymentReference,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        Currency currency,
        PaymentStatus status,
        LocalDateTime createdAt
) {
}
