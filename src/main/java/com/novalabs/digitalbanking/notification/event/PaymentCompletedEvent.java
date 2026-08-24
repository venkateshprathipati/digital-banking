package com.novalabs.digitalbanking.notification.event;

import com.novalabs.digitalbanking.account.enums.Currency;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String paymentReference,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        Currency currency
) {
}
