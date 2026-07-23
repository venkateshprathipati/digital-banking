package com.novalabs.digitalbanking.identity.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiration
) {
}
