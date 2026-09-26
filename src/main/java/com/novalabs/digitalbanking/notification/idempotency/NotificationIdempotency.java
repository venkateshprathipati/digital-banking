package com.novalabs.digitalbanking.notification.idempotency;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification_idempotency")
public class NotificationIdempotency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_key", nullable = false, length = 200)
    private String notificationKey;

    @Column(name = "payment_reference", nullable = false, length = 50)
    private String paymentReference;

    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType;

    @Column(name = "channel", nullable = false, length = 20)
    private String channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationIdempotencyStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public NotificationIdempotency(
            String notificationKey,
            String paymentReference,
            String notificationType,
            String channel
    ) {
        this.notificationKey = notificationKey;
        this.paymentReference = paymentReference;
        this.notificationType = notificationType;
        this.channel = channel;
        this.status = NotificationIdempotencyStatus.PROCESSING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void markCompleted() {
        if (status != NotificationIdempotencyStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only PROCESSING notification records can be completed"
            );
        }
        this.status = NotificationIdempotencyStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed() {
        if (status != NotificationIdempotencyStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only PROCESSING notification records can be failed"
            );
        }
        this.status = NotificationIdempotencyStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }
}
