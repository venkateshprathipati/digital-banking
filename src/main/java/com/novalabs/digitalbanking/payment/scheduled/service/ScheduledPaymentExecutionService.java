package com.novalabs.digitalbanking.payment.scheduled.service;

import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import com.novalabs.digitalbanking.payment.scheduled.repository.ScheduledPaymentRepository;
import com.novalabs.digitalbanking.payment.service.IdempotentTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledPaymentExecutionService {

    private final ScheduledPaymentRepository scheduledPaymentRepository;
    private final IdempotentTransferService idempotentTransferService;

    @Transactional
    public void execute(Long scheduledPaymentId) {

        ScheduledPayment payment =
                scheduledPaymentRepository.findById(scheduledPaymentId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Scheduled payment not found: "
                                                + scheduledPaymentId
                                )
                        );

        if (payment.getStatus() != ScheduledPaymentStatus.PROCESSING) {
            log.warn(
                    "Skipping payment {} because status is {}",
                    scheduledPaymentId,
                    payment.getStatus()
            );
            return;
        }

        TransferRequest request =
                new TransferRequest(
                        payment.getSourceAccountId(),
                        payment.getDestinationAccountId(),
                        payment.getAmount(),
                        payment.getCurrency()
                );

        String idempotencyKey =
                "scheduled-payment-" + payment.getId();

        TransferResponse response =
                idempotentTransferService.transfer(
                        payment.getUserId(),
                        idempotencyKey,
                        request
                );

        payment.markCompleted(
                response.paymentReference()
        );

        scheduledPaymentRepository.save(payment);

        log.info(
                "Scheduled payment completed: id={}, paymentReference={}",
                payment.getId(),
                response.paymentReference()
        );
    }
}