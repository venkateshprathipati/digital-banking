package com.novalabs.digitalbanking.payment.dto;

import com.novalabs.digitalbanking.account.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "Source account ID is required")
        Long sourceAccountId,

        @NotNull(message = "Destination account ID is required")
        Long destinationAccountId,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Transfer amount must be greater than zero"

        )
        BigDecimal amount,

        @NotNull(message = "Currency is required")
        Currency currency
) {
}
