package com.novalabs.digitalbanking.payment.validation;

import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import org.springframework.stereotype.Component;

@Component
public class TransferValidator {

    public void validate(TransferRequest request) {
        if (request.sourceAccountId().equals(request.destinationAccountId())) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }
    }
}
