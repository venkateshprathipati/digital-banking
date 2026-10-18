package com.novalabs.digitalbanking.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novalabs.digitalbanking.common.config.KafkaConfig;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void recordPaymentCompleted(
            PaymentCompletedEvent event
    ) {
        final String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize payment event", e);
        }

        OutboxEvent outboxEvent = new OutboxEvent(
                UUID.randomUUID(),
                "PAYMENT",
                event.paymentReference(),
                "PaymentCompleted",
                payload,
                KafkaConfig.PAYMENT_COMPLETED_TOPIC
        );

        outboxEventRepository.save(outboxEvent);
    }
}
