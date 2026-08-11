package com.novalabs.digitalbanking.identity.dto;

import com.novalabs.digitalbanking.identity.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegistrationResponse(
        UUID id,
        String username,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
