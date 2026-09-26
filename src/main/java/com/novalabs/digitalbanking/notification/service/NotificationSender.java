package com.novalabs.digitalbanking.notification.service;

import com.novalabs.digitalbanking.common.exception.NotificationDeliveryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationSender {

    @Retryable(
            retryFor = NotificationDeliveryException.class,
            maxAttemptsExpression = "${banking.notification.retry.max-attempts}",
            backoff = @Backoff(
                    delayExpression = "${banking.notification.retry.initial-delay-ms}",
                    multiplierExpression = "${banking.notification.retry.multiplier}"
            )
    )
    public void send(String paymentReference, String message) {
        log.info("Sending notification. paymentReference={}", paymentReference);
        callNotificationProvider(paymentReference, message);
    }

    @Recover
    public void recover(NotificationDeliveryException exception, String paymentReference, String message) {
        log.error("Notification delivery permanently failed after retries. " + "paymentreference={}", paymentReference, exception);
        throw exception;

    }

    private void callNotificationProvider(
            String paymentReference,
            String message
    ) {
        /*
        * Replace this with actual SMS/email provider call.
        * Example:
        * notificationClient.send(
        * paymentReference,
        * message
        * );
        */
    }
}
