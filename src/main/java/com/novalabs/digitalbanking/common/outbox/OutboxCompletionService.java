package com.novalabs.digitalbanking.common.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OutboxCompletionService {

    private final OutboxEventRepository repository;

    @Transactional
    public void markPublished(Long eventId, String workerId) {

        OutboxEvent event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException(
                                "Outbox event not found: " + eventId
                        )
                );
        if (!event.isClaimedBy(workerId)){
            return;
        }

        event.markPublished();
    }

    @Transactional
    public void markFailed(Long eventId,String workerId,Exception exception){
        OutboxEvent event = repository.findById(eventId)
                .orElseThrow(()->new IllegalStateException(
                        "Outbox event not found: " + eventId
                ));

        if (!event.isClaimedBy(workerId)){
            return;
        }
        int maxRetries = 10;
        long delaySeconds = Math.min(300, (long) Math.pow(2,event.getRetryCount()));
        LocalDateTime nextRetryAt = LocalDateTime.now().plusSeconds(delaySeconds);

        event.recordFailure(
                maxRetries,
                exception.getMessage(),
                nextRetryAt
        );
    }
}
