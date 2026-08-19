package com.novalabs.digitalbanking.audit.dto;

import com.novalabs.digitalbanking.audit.enums.ActorType;
import com.novalabs.digitalbanking.audit.enums.AuditEventType;
import com.novalabs.digitalbanking.audit.enums.ResourceType;

public record AuditEventCommand(
        AuditEventType eventType,
        String actorId,
        ActorType actorType,
        ResourceType resourceType,
        String resourceId,
        String description,
        String ipAddress,
        String correlationId
) {
}
