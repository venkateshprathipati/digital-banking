package com.novalabs.digitalbanking.notification.event;

public record PaymentFailedEvent(
        String paymentReference,
        Long sourceAccountId,
        Long destinationAccountId,
        String reason
) {
}
