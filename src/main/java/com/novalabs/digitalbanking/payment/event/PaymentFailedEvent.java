package com.novalabs.digitalbanking.payment.event;

public record PaymentFailedEvent(
        String paymentReference,
        Long sourceAccountId,
        Long destinationAccountId,
        String reason
) {
}
