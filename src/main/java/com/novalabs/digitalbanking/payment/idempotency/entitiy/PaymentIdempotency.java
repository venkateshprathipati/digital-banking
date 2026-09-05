package com.novalabs.digitalbanking.payment.idempotency.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "payment_idempotency",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payment_idempotency_user_key",
                        columnNames = {
                                "user_id," +
                                        "idempotency_key"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_payment_idempotency_payment_reference",
                        columnList = "payment_reference"
                ),
                @Index(
                        name = "idx_payment_idempotency_created_at",
                        columnList = "created_at"
                )
        }
)
public class PaymentIdempotency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "idempotency_key", nullable = false, length = 100)
    private String idempotencyKey;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IdempotencyStatus status;

    @Column(name = "payment_reference", length = 50)
    private String paymentReference;

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PaymentIdempotency(
            UUID userId, String idempotencyKey, String requestHash
    ) {
        this.userId = userId;
        this.idempotencyKey = idempotencyKey;
        this.requestHash = requestHash;
        this.status = IdempotencyStatus.PROCESSING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void markCompleted(String paymentReference, String responsePayload) {
        if (status != IdempotencyStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only PROCESSING idempotency records can be completed"
            );
        }
        this.status = IdempotencyStatus.COMPLETED;
        this.paymentReference = paymentReference;
        this.responsePayload = responsePayload;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed() {
        if (status != IdempotencyStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only PROCESSING idempotency records can be failed"
            );
        }
        this.status = IdempotencyStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }


}
