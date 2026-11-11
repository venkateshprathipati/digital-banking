package com.novalabs.digitalbanking.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novalabs.digitalbanking.common.kafka.PaymentEventProducer;
import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    //private static final int MAX_RETRIES = 10;
    private static final int ACK_TIMEOUT_SECONDS = 10;

    private final OutboxClaimService claimService;
    private final OutboxCompletionService completionService;
    private final PaymentEventProducer paymentEventProducer;
    private final ObjectMapper objectMapper;

    @Value("${banking.outbox.worker-id:outbox-publisher-1}")
    private String workerId;

    @Scheduled(
            fixedDelayString = "${banking.outbox.poll-delay-ms:5000}"
    )
    public void publishPendingEvents() {
        var events = claimService.claimBatch(workerId);
        if (events.isEmpty()) {
            return;
        }
        log.info("Claimed {} outbox events. workerId={}", events.size(), workerId);
        for (OutboxEventSnapshot event : events) {
            try {
                PaymentCompletedEvent paymentCompletedEvent =
                        objectMapper.readValue(event.payload(), PaymentCompletedEvent.class);
                paymentEventProducer.publish(paymentCompletedEvent).get(ACK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                completionService.markPublished(event.id(), workerId);
                log.info("Outbox event published. eventId{}", event.eventId());

            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                log.warn("Outbox publisher interrupted. eventId={}", event.eventId());
                return;
            } catch (Exception exception) {
                completionService.markFailed(event.id(), workerId, exception);
                log.error("Outbox event publishing failed. eventId={}", event.eventId(), exception);
            }
        }
    }
}
