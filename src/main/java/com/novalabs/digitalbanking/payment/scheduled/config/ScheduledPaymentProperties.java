package com.novalabs.digitalbanking.payment.scheduled.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.scheduled-payment")
public record ScheduledPaymentProperties(
        boolean enabled,
        long pollDelayMs,
        int batchSize
) {
}
