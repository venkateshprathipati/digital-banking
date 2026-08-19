package com.novalabs.digitalbanking.audit.entity;

import com.novalabs.digitalbanking.audit.enums.ActorType;
import com.novalabs.digitalbanking.audit.enums.AuditEventType;
import com.novalabs.digitalbanking.audit.enums.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "audit_events",
        indexes = {
                @Index(
                        name = "idx_audit_resource",
                        columnList = "resource_type, resource_id"
                ),
                @Index(
                        name = "idx_audit_actor",
                        columnList = "actor_id"
                ),
                @Index(
                        name = "idx_audit_created",
                        columnList = "created_at"
                )
        }
)
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 60)
    private AuditEventType eventType;

    @Column(name = "actor_id")
    private String actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", length = 30)
    private ActorType actorType;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 40)
    private ResourceType resourceType;

    @Column(name = "resource_id", nullable = false, length = 100)
    private String resourceId;

    @Column(length = 255)
    private String description;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
