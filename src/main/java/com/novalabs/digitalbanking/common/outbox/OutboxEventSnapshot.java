package com.novalabs.digitalbanking.common.outbox;

public record OutboxEventSnapshot(
        Long id,
        String eventId,
        String payload,
        String topic,
        String lockedBy
) {
}
