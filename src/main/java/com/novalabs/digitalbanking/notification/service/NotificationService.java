package com.novalabs.digitalbanking.notification.service;

import com.novalabs.digitalbanking.notification.event.FraudDetectedEvent;
import com.novalabs.digitalbanking.notification.event.PaymentCompletedEvent;
import com.novalabs.digitalbanking.notification.event.PaymentFailedEvent;
import com.novalabs.digitalbanking.notification.event.PaymentRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifyPaymentFailed(
            PaymentFailedEvent event
    ) {
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
    }

    public void notifyPaymentCompleted(
            PaymentCompletedEvent event
    ) {
        log.info(
                "NOTIFICATION | event=PAYMENT_COMPLETED" +
                        " | paymentReference={}" +
                        " | sourceAccountId={}" +
                        " | destinationAccountId={}" +
                        " | amount={}" +
                        " | currency={}",
                event.paymentReference(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                event.amount(),
                event.currency()
        );
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
    }
}
