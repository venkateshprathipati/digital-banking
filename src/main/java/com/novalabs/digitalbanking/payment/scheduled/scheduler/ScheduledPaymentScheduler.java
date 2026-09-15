package com.novalabs.digitalbanking.payment.scheduled.scheduler;

import com.novalabs.digitalbanking.payment.scheduled.config.ScheduledPaymentProperties;
import com.novalabs.digitalbanking.payment.scheduled.service.ScheduledPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledPaymentScheduler {

    private final ScheduledPaymentProcessor processor;
    private final ScheduledPaymentProperties properties;

    @Scheduled(
            fixedDelayString = "${banking.scheduled-payment.poll-delay-ms}"
    )
    public void processDuePayments(){
        if (!properties.enabled()){
            return;
        }
        processor.processDuePayments(properties.batchSize());
    }
}
