package com.novalabs.digitalbanking.payment.scheduled.repository;

import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ScheduledPaymentRepository
        extends JpaRepository<ScheduledPayment, Long> {

    /**
     * Finds payments that are due for execution.
     * This query intentionally does NOT use a pessimistic lock.
     * Concurrency is handled by claimPayment(), which atomically
     * changes SCHEDULED -> PROCESSING.
     */
    @Query("""
        SELECT sp
        FROM ScheduledPayment sp
        WHERE sp.status = :status
          AND sp.scheduledAt <= :now
        ORDER BY sp.scheduledAt ASC, sp.id ASC
        """)
    List<ScheduledPayment> findDuePayments(
            @Param("status") ScheduledPaymentStatus status,
            @Param("now") Instant now,
            Pageable pageable
    );

    /**
     * Atomically claims a scheduled payment for processing.
     * Only one scheduler/instance can successfully claim a payment.
     * Example SQL semantics:
     * UPDATE scheduled_payments
     * SET status = 'PROCESSING'
     * WHERE id = ?
     *   AND status = 'SCHEDULED';
     * Result:
     *   1 -> successfully claimed
     *   0 -> already claimed / completed / unavailable
     */
    @Modifying
    @Query("""
        UPDATE ScheduledPayment sp
        SET sp.status = :processingStatus
        WHERE sp.id = :id
          AND sp.status = :scheduledStatus
        """)
    int claimPayment(
            @Param("id") Long id,
            @Param("scheduledStatus") ScheduledPaymentStatus scheduledStatus,
            @Param("processingStatus") ScheduledPaymentStatus processingStatus
    );
}
