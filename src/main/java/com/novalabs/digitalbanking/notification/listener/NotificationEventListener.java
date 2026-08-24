package com.novalabs.digitalbanking.notification.listener;

import com.novalabs.digitalbanking.notification.event.FraudDetectedEvent;
import com.novalabs.digitalbanking.notification.event.PaymentCompletedEvent;
import com.novalabs.digitalbanking.notification.event.PaymentFailedEvent;
import com.novalabs.digitalbanking.notification.event.PaymentRejectedEvent;
import com.novalabs.digitalbanking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.debug("Received payment failed for paymentReference={}", event.paymentReference());
        notificationService.notifyPaymentFailed(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.debug("Received payment completed for paymentReference={}", event.paymentReference());
        notificationService.notifyPaymentCompleted(event);
    }

    @EventListener
    public void handlePaymentRejected(PaymentRejectedEvent event) {
        notificationService.notifyPaymentRejected(event);
    }

    @EventListener
    public void handleFraudDetected(FraudDetectedEvent event) {
        notificationService.notifyFraudDetected(event);
    }

}
