package com.novalabs.digitalbanking.audit.listener;

import com.novalabs.digitalbanking.audit.dto.AuditEventCommand;
import com.novalabs.digitalbanking.audit.enums.ActorType;
import com.novalabs.digitalbanking.audit.enums.AuditEventType;
import com.novalabs.digitalbanking.audit.enums.ResourceType;
import com.novalabs.digitalbanking.audit.service.AuditService;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedAuditListener {

    private final AuditService auditService;

    public void handle(PaymentCompletedEvent event){
        AuditEventCommand command = new AuditEventCommand(
                AuditEventType.TRANSACTION_COMPLETED,
                null,
                ActorType.SYSTEM,
                ResourceType.PAYMENT,
                event.paymentReference(),
                "Payment transfer completed",
                null,
                null
        );
        auditService.record(command);
        log.info("Audit event recorded for paymentReference={}",event.paymentReference());
    }
}
