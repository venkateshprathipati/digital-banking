package com.novalabs.digitalbanking.payment.scheduled.service;

import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import com.novalabs.digitalbanking.payment.scheduled.repository.ScheduledPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledPaymentFailureService {

    private final ScheduledPaymentRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(
            Long paymentId,
            String reason
    ) {

        ScheduledPayment payment =
                repository.findById(paymentId)
                        .orElse(null);

        if (payment == null) {
            log.warn(
                    "Scheduled payment {} not found while marking failure",
                    paymentId
            );
            return;
        }

        if (payment.getStatus() != ScheduledPaymentStatus.PROCESSING) {
            log.warn(
                    "Cannot mark scheduled payment {} as FAILED. Current status={}",
                    paymentId,
                    payment.getStatus()
            );
            return;
        }

        payment.markFailed(reason);

        repository.save(payment);

        log.info(
                "Scheduled payment marked FAILED: id={}",
                paymentId
        );
    }
}