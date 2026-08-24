package com.novalabs.digitalbanking.notification.event;

import com.novalabs.digitalbanking.account.enums.Currency;

import java.math.BigDecimal;

public record PaymentFailedEvent(
        String paymentReference,
        Long sourceAccountId,
        Long destinationAccountId,
        String reason
) {
}
