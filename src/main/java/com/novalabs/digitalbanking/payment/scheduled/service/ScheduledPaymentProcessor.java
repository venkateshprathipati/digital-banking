package com.novalabs.digitalbanking.payment.scheduled.service;

import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import com.novalabs.digitalbanking.payment.scheduled.repository.ScheduledPaymentRepository;
import com.novalabs.digitalbanking.payment.service.IdempotentTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledPaymentProcessor {

    private final ScheduledPaymentRepository scheduledPaymentRepository;
    private final IdempotentTransferService idempotentTransferService;

    /**
     * Finds and processes a bounded batch of scheduled payments
     * whose execution time has arrived.
     * <p>
     * The scheduler should call this method.
     *
     * @param batchSize
     */
    public void processDuePayments(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Scheduled payment size must be greater than zero");
        }

        List<ScheduledPayment> duePayments =
                scheduledPaymentRepository.findDuePayments(
                        ScheduledPaymentStatus.SCHEDULED,
                        Instant.now(),
                        PageRequest.of(0, batchSize)
                );
        if (duePayments.isEmpty()) {
            return;
        }

        log.info(
                "Found {} scheduled payments ready for processing", duePayments.size()
        );

        for (ScheduledPayment scheduledPayment : duePayments) {
            processSinglePayment(scheduledPayment.getId());
        }
    }

    @Transactional
    public void processSinglePayment(Long scheduledPaymentId) {
        ScheduledPayment scheduledPayment =
                scheduledPaymentRepository.findById(scheduledPaymentId)
                        .orElse(null);

        if (scheduledPayment == null) {
            log.warn("Scheduled payment {} no longer exists", scheduledPaymentId);
            return;
        }

        if (scheduledPayment.getStatus() != ScheduledPaymentStatus.SCHEDULED) {
            log.debug("Skipping scheduled payment {} becuase status is {}", scheduledPaymentId, scheduledPayment.getStatus());
            return;
        }

        log.info("Starting scheduled payment processing: id={}, scheduledAt={}", scheduledPayment.getId(), scheduledPayment.getScheduledAt());
        try {
            /**
             * Move the schedule into PROCESSING before executing the actual transfer.
             */
            scheduledPayment.markProcessing();
            /**
             * IMPORTANT:
             * Do NOT directly manipulate Account.balance here.
             *
             * The existing transfer/idempotency path remains responsible
             * for:
             * - account validation
             * - deterministic account locking
             * - balance validation
             * - fraud checks
             * - Payment creation
             * - transaction handling
             */
            TransferRequest transferRequest =
                    new TransferRequest(
                            scheduledPayment.getSourceAccountId(),
                            scheduledPayment.getDestinationAccountId(),
                            scheduledPayment.getAmount(),
                            scheduledPayment.getCurrency()
                    );

            String idempotencyKey = "scheduled-payment-" + scheduledPayment.getId();
            UUID userId = scheduledPayment.getUserId();
            TransferResponse response = idempotentTransferService.transfer(
                    userId,
                    idempotencyKey,
                    transferRequest
            );
            scheduledPayment.markCompleted(
                    response.paymentReference()
            );

            log.info("Scheduled payment completed successfully: " + "id={},paymentReference={}",
                    scheduledPayment.getId(),
                    response.paymentReference()
            );


        } catch (Exception exception) {
            scheduledPayment.markFailed(exception.getMessage());

            log.error("Scheduled payment failed: id={},reason={}", scheduledPayment.getId(),
                    exception.getMessage(), exception);
        }

    }
}
