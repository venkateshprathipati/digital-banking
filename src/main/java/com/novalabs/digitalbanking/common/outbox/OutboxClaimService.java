package com.novalabs.digitalbanking.common.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxClaimService {
    private final OutboxEventRepository repository;

    @Value("${banking.outbox.batch-size:50}")
    private int batchSize;

    @Value("${banking.outbox.lease-duration-seconds:60}")
    private long leaseDurationSeconds;

    @Transactional
    public List<OutboxEventSnapshot> claimBatch(String workerId) {
        List<OutboxEvent> events =
                repository.findClaimableEvents(batchSize);

        LocalDateTime leaseUntil = LocalDateTime.now().plusSeconds(leaseDurationSeconds);

        return events.stream()
                .map(event -> {
                    event.claim(workerId, leaseUntil);
                    return new OutboxEventSnapshot(
                            event.getId(),
                            event.getEventId().toString(),
                            event.getPayload(),
                            event.getTopic(),
                            workerId
                    );
                }).toList();
    }
}
