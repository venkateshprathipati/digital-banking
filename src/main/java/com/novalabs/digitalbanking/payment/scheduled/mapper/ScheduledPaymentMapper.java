package com.novalabs.digitalbanking.payment.scheduled.mapper;

import com.novalabs.digitalbanking.payment.scheduled.dto.ScheduledPaymentResponse;
import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import org.springframework.stereotype.Component;

@Component
public class ScheduledPaymentMapper {

    public ScheduledPaymentResponse toResponse(ScheduledPayment payment){
        return new ScheduledPaymentResponse(
                payment.getId(),
                payment.getSourceAccountId(),
                payment.getDestinationAccountId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getScheduledAt(),
                payment.getStatus(),
                payment.getPaymentReference(),
                payment.getFailureReason()
        );
    }
}
