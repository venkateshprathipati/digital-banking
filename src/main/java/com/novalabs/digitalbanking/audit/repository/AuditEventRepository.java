package com.novalabs.digitalbanking.audit.repository;

import com.novalabs.digitalbanking.audit.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditEventRepository extends JpaRepository<AuditEvent,Long> {

    List<AuditEvent> findByResourceTypeAndResourceIdOrderByCreatedAtDesc(
            String resourceType,
            String resourceId
    );
}
