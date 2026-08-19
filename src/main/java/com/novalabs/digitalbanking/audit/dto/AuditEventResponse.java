package com.novalabs.digitalbanking.audit.dto;

import com.novalabs.digitalbanking.audit.enums.ActorType;
import com.novalabs.digitalbanking.audit.enums.AuditEventType;
import com.novalabs.digitalbanking.audit.enums.ResourceType;

import java.time.LocalDateTime;

public record AuditEventResponse(
        Long id,
        AuditEventType eventType,
        String actorId,
        ActorType actorType,
        ResourceType resourceType,
        String resourceId,
        String description,
        String ipAddress,
        String correlationId,
        LocalDateTime createdAt
) {
}
