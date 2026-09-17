package com.novalabs.digitalbanking.payment.scheduled.repository;

import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment, Long> {

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT sp
        FROM ScheduledPayment sp
        WHERE sp.status = :status
        AND sp.scheduledAt <= :now
        ORDER BY sp.scheduledAt ASC, sp.id ASC
        """)
    List<ScheduledPayment> findDuePaymentsForUpdate(
            @Param("status") ScheduledPaymentStatus status,
            @Param("now") Instant now,
            Pageable pageable
    );
}
