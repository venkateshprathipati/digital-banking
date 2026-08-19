package com.novalabs.digitalbanking.payment.factory;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.generator.PaymentReferenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final PaymentReferenceGenerator referenceGenerator;

    public Payment create(
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount,
            Currency currency
    ){
        Payment payment = Payment.builder()
                .paymentReference(referenceGenerator.generate())
                .sourceAccountId(sourceAccountId)
                .destinationAccountId(destinationAccountId)
                .amount(amount)
                .currency(currency)
                .build();

        payment.markProcessing();

        return payment;
    }
}
