package com.novalabs.digitalbanking.audit.service;

import com.novalabs.digitalbanking.audit.dto.AuditEventCommand;
import com.novalabs.digitalbanking.audit.dto.AuditEventResponse;
import com.novalabs.digitalbanking.audit.entity.AuditEvent;
import com.novalabs.digitalbanking.audit.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditEventRepository repository;

    /**
     * Record an audit event
     * <p>
     * This method is normally called from business service
     */
    @Transactional
    public void record(AuditEventCommand command) {
        AuditEvent event = AuditEvent.builder()
                .eventType(command.eventType())
                .actorId(command.actorId())
                .actorType(command.actorType())
                .resourceType(command.resourceType())
                .resourceId(command.resourceId())
                .description(command.description())
                .ipAddress(command.ipAddress())
                .correlationId(command.correlationId())
                .build();

        repository.save(event);
    }

    /**
     * Retrieve audit history for a resource.
     *
     */
    @Transactional(readOnly = true)
    public List<AuditEventResponse> findByResource(
            String resourceType, String resourceId
    ) {
        return repository.findByResourceTypeAndResourceIdOrderByCreatedAtDesc(
                        resourceType,
                        resourceId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditEventResponse toResponse(AuditEvent event) {
        return new AuditEventResponse(
                event.getId(),
                event.getEventType(),
                event.getActorId(),
                event.getActorType(),
                event.getResourceType(),
                event.getResourceId(),
                event.getDescription(),
                event.getIpAddress(),
                event.getCorrelationId(),
                event.getCreatedAt()
        );
    }
}
