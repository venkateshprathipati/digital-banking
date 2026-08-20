package com.novalabs.digitalbanking.fraud.model;

import com.novalabs.digitalbanking.account.entity.Account;

import java.math.BigDecimal;

public record FraudContext(
        Account sourceAccount,
        Account destinationAccount,
        BigDecimal amount
) {
}
