package com.novalabs.digitalbanking.notification.service;

import com.novalabs.digitalbanking.notification.idempotency.NotificationIdempotencyService;
import com.novalabs.digitalbanking.payment.event.FraudDetectedEvent;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import com.novalabs.digitalbanking.payment.event.PaymentFailedEvent;
import com.novalabs.digitalbanking.payment.event.PaymentRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationSender notificationSender;
    private final NotificationIdempotencyService notificationIdempotencyService;

    public void notifyPaymentCompleted(
            PaymentCompletedEvent event
    ) {
        String notificationType = "PAYMENT_COMPLETED";
        String channel = "SMS";

        String notificationKey =
                buildNotificationKey(
                        event.paymentReference(),
                        notificationType,
                        channel
                );

        boolean claimed =
                notificationIdempotencyService.tryClaim(
                        notificationKey,
                        event.paymentReference(),
                        notificationType,
                        channel
                );

        if (!claimed) {
            log.info(
                    "Duplicate notification skipped. notificationKey={}", notificationKey
            );
            return;
        }

        log.info("NOTIFICATION | event=PAYMENT_COMPLETED" +
                        " | paymentReference={}" +
                        " | sourceAccountId={}" +
                        " | destinationAccountId={}" +
                        " | amount={}" +
                        " | currency={}",
                event.paymentReference(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                event.amount(),
                event.currency());

        String message = String.format(
                "Payment %s completed successfully. Amount=%s %s",
                event.paymentReference(),
                event.amount(),
                event.currency()
        );

        try {
            notificationSender.send(
                    event.paymentReference(),
                    message
            );
            notificationIdempotencyService.markCompleted(notificationKey);
            log.info(
                    "Notification completed. notificationKey={}", notificationKey
            );

        } catch (RuntimeException e) {
            notificationIdempotencyService.markFailed(notificationKey);
            log.error(
                    "Notification failed. notificationKey={}", notificationKey, e
            );
            throw e;
        }
    }

    public void notifyPaymentFailed(
            PaymentFailedEvent event
    ) {
        String notificationType = "PAYMENT_FAILED";
        String channel = "SMS";

        String notificationKey =
                buildNotificationKey(
                        event.paymentReference(),
                        notificationType,
                        channel);
        boolean claimed =
                notificationIdempotencyService.tryClaim(
                        notificationKey,
                        event.paymentReference(),
                        notificationType,
                        channel);
        if (!claimed) {
            log.info(
                    "Duplicate PAYMENT_FAILED notification skipped. " + "paymentReference={}",
                    event.paymentReference());
            return;
        }
        log.info(
                "NOTIFICATION | event=PAYMENT_FAILED" +
                        " | paymentReference={}" +
                        " | sourceAccountId={}" +
                        " | destinationAccountId={}" +
                        " | reason={}",
                event.paymentReference(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                event.reason()
        );

        String message =
                String.format("Payment %s failed. Reason=%s", event.paymentReference(),
                        event.reason());
        try {
            notificationSender.send(
                    event.paymentReference(),
                    message);
            notificationIdempotencyService.markCompleted(notificationKey);
            log.info(
                    "PAYMENT_FAILED notification completed. " + "paymentReference={}",
                    event.paymentReference());
        } catch (RuntimeException exception) {
            notificationIdempotencyService.markFailed(notificationKey);
            log.error(
                    "PAYMENT_FAILED notification failed. " + "paymentReference={}",
                    event.paymentReference(),
                    exception);
            throw exception;
        }
    }

    public void notifyPaymentRejected(
            PaymentRejectedEvent event
    ) {
        log.info(
                "NOTIFICATION | event=PAYMENT_REJECTED" +
                        " | paymentReference={}" +
                        " | sourceAccountId={}" +
                        " | destinationAccountId={}" +
                        " | amount={}" +
                        " | currency={}" +
                        " | reason={}",
                event.paymentReference(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                event.amount(),
                event.currency(),
                event.reason()
        );
        String message = String.format(
                "Payment %s rejected. Reason=%s",
                event.paymentReference(),
                event.reason()
        );

        notificationSender.send(
                event.paymentReference(),
                message
        );
    }

    public void notifyFraudDetected(
            FraudDetectedEvent event
    ) {
        log.info(
                "NOTIFICATION | event=FRAUD_DETECTION" +
                        " | paymentReference={}" +
                        " | sourceAccountId={}" +
                        " | amount={}" +
                        " | ruleCode={}" +
                        " | reason={}",
                event.paymentReference(),
                event.sourceAccountId(),
                event.amount(),
                event.ruleCode(),
                event.reason()
        );

        String message = String.format(
                "Fraud detected for payment %s. Rule=%s",
                event.paymentReference(),
                event.ruleCode()
        );

        notificationSender.send(
                event.paymentReference(),
                message
        );
    }

    private String buildNotificationKey(String paymentReference, String notificationType, String channel) {
        return paymentReference +
                ":" +
                notificationType +
                ":" +
                channel;
    }
}
