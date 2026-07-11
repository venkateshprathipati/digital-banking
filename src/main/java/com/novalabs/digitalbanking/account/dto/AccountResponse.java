package com.novalabs.digitalbanking.account.dto;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        Long customerId,
        BigDecimal balance,
        String status
) {
}
