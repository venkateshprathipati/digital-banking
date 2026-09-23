package com.novalabs.digitalbanking.payment.event;

import com.novalabs.digitalbanking.audit.listener.PaymentCompletedAuditListener;
import com.novalabs.digitalbanking.transactions.service.TransactionEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedEventListener {

    private final TransactionEventHandler transactionEventHandler;
    private final PaymentCompletedAuditListener auditListener;

    @Async("bankingEventExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(PaymentCompletedEvent event){
        log.info("Processing PaymentCompletedEvent asynchronously. paymentReference={}",
                event.paymentReference());
        transactionEventHandler.handle(event);
        auditListener.handle(event);
    }
}
