package com.novalabs.digitalbanking.payment.dto;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.enums.PaymentStatus;

import java.math.BigDecimal;

public record TransferResponse(
        String paymentReference,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        Currency currency,
        PaymentStatus status
) {
}
