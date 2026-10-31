package com.novalabs.digitalbanking.payment.scheduled.service;

import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import com.novalabs.digitalbanking.payment.scheduled.repository.ScheduledPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduledPaymentClaimService {

    private final ScheduledPaymentRepository repository;

    @Transactional
    public boolean claim(Long paymentId) {

        int updated =
                repository.claimPayment(
                        paymentId,
                        ScheduledPaymentStatus.SCHEDULED,
                        ScheduledPaymentStatus.PROCESSING
                );

        return updated == 1;
    }
}