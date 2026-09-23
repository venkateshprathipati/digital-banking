package com.novalabs.digitalbanking.notification.listener;

import com.novalabs.digitalbanking.notification.service.NotificationService;
import com.novalabs.digitalbanking.payment.event.FraudDetectedEvent;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import com.novalabs.digitalbanking.payment.event.PaymentFailedEvent;
import com.novalabs.digitalbanking.payment.event.PaymentRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;

    @Async("bankingEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.debug(
                "Received payment completed for paymentReference={}",
                event.paymentReference()
        );
        notificationService.notifyPaymentCompleted(event);
    }

    @Async("bankingEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.debug(
                "Received payment failed for paymentReference={}",
                event.paymentReference()
        );
        notificationService.notifyPaymentFailed(event);
    }

    @Async("bankingEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentRejected(PaymentRejectedEvent event) {
        log.debug(
                "Received payment rejected for paymentReference={}",
                event.paymentReference()
        );
        notificationService.notifyPaymentRejected(event);
    }

    @Async("bankingEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFraudDetected(FraudDetectedEvent event) {
        log.debug(
                "Received fraud detected for paymentReference={}",
                event.paymentReference()
        );
        notificationService.notifyFraudDetected(event);
    }

}
