package com.novalabs.digitalbanking.common.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@NoArgsConstructor
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status = OutboxStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "next_attempt_at", nullable = false)
    private LocalDateTime nextAttemptAt;

    @Column(name = "locked_by")
    private String lockedBy;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public OutboxEvent(
            UUID eventId,
            String aggregateType,
            String aggregateId,
            String eventType,
            String payload,
            String topic
    ) {
        this.eventId = eventId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.topic = topic;
        this.status = OutboxStatus.PENDING;
        this.retryCount = 0;
        this.nextAttemptAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markPublished() {
        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.lockedBy = null;
        this.lockedUntil = null;
        this.lastError = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void recordFailure(
            int maxRetries,
            String errorMessage,
            LocalDateTime nextRetryAt) {
        this.retryCount++;
        this.lastError = errorMessage;
        this.lockedBy = null;
        this.lockedUntil = null;
        this.updatedAt = LocalDateTime.now();

        if (this.retryCount >= maxRetries) {
            this.status = OutboxStatus.FAILED;
        } else {
            this.nextAttemptAt = nextRetryAt;
        }
    }

    public void claim(String workerId, LocalDateTime leaseUntil) {
        this.lockedBy = workerId;
        this.lockedUntil = leaseUntil;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isClaimedBy(String workerId) {
        return workerId.equals(this.lockedBy);
    }

}
