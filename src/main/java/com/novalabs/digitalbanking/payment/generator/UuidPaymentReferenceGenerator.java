package com.novalabs.digitalbanking.payment.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidPaymentReferenceGenerator implements PaymentReferenceGenerator {
    @Override
    public String generate() {
        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 20)
                        .toUpperCase();
    }
}
