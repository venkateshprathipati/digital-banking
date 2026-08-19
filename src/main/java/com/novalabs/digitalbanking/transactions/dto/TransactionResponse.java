package com.novalabs.digitalbanking.transactions.dto;

import com.novalabs.digitalbanking.transactions.enums.TransactionStatus;
import com.novalabs.digitalbanking.transactions.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionReference,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        String description,
        LocalDateTime createdAt
) {
}
