package com.novalabs.digitalbanking.notification.event;

import java.math.BigDecimal;

public record FraudDetectedEvent(
        String paymentReference,
        Long sourceAccountId,
        BigDecimal amount,
        String ruleCode,
        String reason
) {
}
