package com.novalabs.digitalbanking.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novalabs.digitalbanking.common.kafka.PaymentEventProducer;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private static final int MAX_RETRIES = 10;
    private static final int ACK_TIMEOUT_SECONDS = 10;

    private final OutboxEventRepository outboxEventRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final ObjectMapper objectMapper;

    @Scheduled(
            fixedDelayString = "${banking.outbox.poll-delay-ms:5000}"
    )
    public void publishPendingEvents() {
        var pendingEvents = outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        );

        for (OutboxEvent outboxEvent : pendingEvents) {
            try {
                PaymentCompletedEvent event =
                        objectMapper.readValue(
                                outboxEvent.getPayload(),
                                PaymentCompletedEvent.class
                        );

                paymentEventProducer.publish(event)
                        .get(ACK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                outboxEvent.markPublished();
                outboxEventRepository.save(outboxEvent);

                log.info("Outbox event published. eventId={},paymentReference={}",
                        outboxEvent.getEventId(),
                        event.paymentReference());

            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                log.warn("Outbox publishing interrupted");
                return;
            } catch (Exception exception) {
                outboxEvent.recordFailure(MAX_RETRIES);
                outboxEventRepository.save(outboxEvent);

                log.error(
                        "Outbox publishing failed. eventId={},retries={},status={}",
                        outboxEvent.getEventId(),
                        outboxEvent.getRetryCount(),
                        outboxEvent.getStatus(),
                        exception
                );

            }
        }
    }
}
