package com.novalabs.digitalbanking.payment.scheduled.entity;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "scheduled_payments",
        indexes = {
                @Index(
                        name = "idx_scheduled_payment_due",
                        columnList = "status,scheduled_at"
                ),
                @Index(
                        name = "idx,scheduled_payment_source_accout",
                        columnList = "source_account_id"
                ),
                @Index(
                        name = "idx_scheduled_payment_destination_account",
                        columnList = "destination_account_id"
                )
        }
)
public class ScheduledPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_account_id", nullable = false)
    private Long sourceAccountId;

    @Column(name = "destination_account_id", nullable = false)
    private Long destinationAccountId;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private Currency currency;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScheduledPaymentStatus status;

    @Column(name = "payment_reference", length = 50)
    private String paymentReference;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Version
    @Column(nullable = false)
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant timestamps;

    @Builder
    private ScheduledPayment(
            UUID userId,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount,
            Currency currency,
            Instant scheduledAt
    ) {
        validateCreation(
                sourceAccountId, destinationAccountId, amount, currency, scheduledAt
        );
        this.userId = userId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.currency = currency;
        this.scheduledAt = scheduledAt;
        this.status = ScheduledPaymentStatus.SCHEDULED;
    }

    public void markProcessing() {
        if (status != ScheduledPaymentStatus.SCHEDULED) {
            throw new IllegalStateException("Only SCHEDULED payments can move to PROCESSING");
        }
        this.status = ScheduledPaymentStatus.PROCESSING;
    }

    public void markCompleted(String paymentReference) {
        if (status != ScheduledPaymentStatus.PROCESSING) {
            throw new IllegalStateException("Only PROCESSING payments can move to COMPLETED");
        }
        this.paymentReference = paymentReference;
        this.status = ScheduledPaymentStatus.COMPLETED;
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        if (status != ScheduledPaymentStatus.PROCESSING) {
            throw new IllegalStateException("Only PROCESSING payments can move to FAILED");
        }
        this.status = ScheduledPaymentStatus.FAILED;
        this.failureReason = truncate(reason);
    }

    public void cancel() {
        if (status != ScheduledPaymentStatus.SCHEDULED) {
            throw new IllegalStateException("Only SCHEDULED payments can be cancelled");
        }
        this.status = ScheduledPaymentStatus.CANCELLED;
    }

    private void validateCreation(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, Currency currency, Instant scheduledAt) {
        if (sourceAccountId == null) {
            throw new IllegalArgumentException("Source account ID is required");
        }

        if (destinationAccountId == null) {
            throw new IllegalArgumentException("Destination account ID is required");
        }

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (scheduledAt == null) {
            throw new IllegalArgumentException("Scheduled date and time are required");
        }
    }

    private String truncate(String failureReason) {
        if (failureReason == null) return null;
        return failureReason.length() <= 500
                ? failureReason
                : failureReason.substring(0, 500);
    }

}
