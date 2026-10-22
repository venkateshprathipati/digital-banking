package com.novalabs.digitalbanking.common.kafka;

import com.novalabs.digitalbanking.common.config.KafkaConfig;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentCompletedKafkaConsumer {

    @KafkaListener(
            topics = KafkaConfig.PAYMENT_COMPLETED_TOPIC,
            groupId = "transaction-service"
    )
    public void consume(PaymentCompletedEvent event){
        log.info(
                "Kafka payment.completed consumed. " +
                        "paymentReference={}, sourceAccountId={}, " +
                        "destinationAccountId={}, amount={}, currency={}",
                event.paymentReference(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                event.amount(),
                event.currency()
        );
    }
}
