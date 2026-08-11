package com.novalabs.digitalbanking.audit;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AuditableEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
