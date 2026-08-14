package com.novalabs.digitalbanking.payment.entity;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_reference",
                        columnList = "payment_reference",
                        unique = true
                ),
                @Index(
                        name = "idx_payment_source_account",
                        columnList = "source_account_id"
                ),
                @Index(
                        name = "idx_payment_destination_account",
                        columnList = "destination_account_id"
                ),
                @Index(
                        name = "idx_paymant_status",
                        columnList = "status")
        }
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "payment_reference",
            nullable = false,
            unique = true,
            length = 50
    )
    private String paymentReference;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Version
    @Column(nullable = false)
    private Long version;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Builder
    private Payment(
            String paymentReference,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount,
            Currency currency
    ) {
        validateCreation(
                sourceAccountId,
                destinationAccountId,
                amount,
                currency
        );

        this.paymentReference = paymentReference;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.PENDING;
    }

    private void markProcessing() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING payments can move to PROCESSING"
            );
        }

        this.status = PaymentStatus.PROCESSING;
    }

    private void markCompleted() {
        if (status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only PROCESSING payments can move to COMPLETED"
            );
        }

        this.status = PaymentStatus.COMPLETED;
    }

    private void markFailed() {
        if (status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only PROCESSING payments can move to FAILED"
            );
        }

        this.status = PaymentStatus.FAILED;
    }

    private void validateCreation(
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount,
            Currency currency
    ) {
        if (sourceAccountId == null) {
            throw new IllegalArgumentException(
                    "Source account ID is required"
            );
        }

        if (destinationAccountId == null) {
            throw new IllegalArgumentException(
                    "Destination account ID is required"
            );
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
            throw new IllegalArgumentException(
                    "Currency is required"
            );
        }
    }
}
