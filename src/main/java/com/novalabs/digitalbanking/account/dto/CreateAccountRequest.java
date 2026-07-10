package com.novalabs.digitalbanking.account.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank
        String customerId
) {
}
