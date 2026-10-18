package com.novalabs.digitalbanking.common.kafka;

import com.novalabs.digitalbanking.common.config.KafkaConfig;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, PaymentCompletedEvent>> publish(PaymentCompletedEvent event) {
        log.info(
                "Payment event submitted to Kafka. paymentReference={}",
                event.paymentReference());
        return kafkaTemplate.send(
                KafkaConfig.PAYMENT_COMPLETED_TOPIC,
                event.paymentReference(),
                event
        );
    }
}
