package com.novalabs.digitalbanking.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    @Query(value = """
                  SELECT * FROM outbox_events
                  WHERE status = 'PENDING'
                   AND next_attempt_at <= CURRENT_TIMESTAMP
                   AND (
                   locked_until IS NULL
                   OR locked_until < CURRENT_TIMESTAMP
                   )
                   ORDER BY created_at ASC
                   LIMIT :batchSize
                   FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxEvent> findClaimableEvents(@Param("batchSize") int batchSize);
}
