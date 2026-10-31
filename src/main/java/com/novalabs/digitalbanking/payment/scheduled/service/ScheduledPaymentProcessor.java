package com.novalabs.digitalbanking.payment.scheduled.service;

import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.enums.ScheduledPaymentStatus;
import com.novalabs.digitalbanking.payment.scheduled.repository.ScheduledPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledPaymentProcessor {

    private final ScheduledPaymentRepository scheduledPaymentRepository;
    private final ScheduledPaymentClaimService claimService;
    private final ScheduledPaymentExecutionService executionService;
    private final ScheduledPaymentFailureService failureService;

    public void processDuePayments(int batchSize) {

        if (batchSize <= 0) {
            throw new IllegalArgumentException(
                    "Scheduled payment batch size must be greater than zero"
            );
        }

        List<ScheduledPayment> duePayments =
                scheduledPaymentRepository.findDuePayments(
                        ScheduledPaymentStatus.SCHEDULED,
                        Instant.now(),
                        PageRequest.of(0, batchSize)
                );

        if (duePayments.isEmpty()) {
            log.debug("No scheduled payments are due");
            return;
        }

        log.info(
                "Found {} scheduled payments ready for processing",
                duePayments.size()
        );

        for (ScheduledPayment payment : duePayments) {

            Long paymentId = payment.getId();

            try {

                /*
                 * Transaction 1:
                 *
                 * SCHEDULED -> PROCESSING
                 *
                 * This transaction commits independently.
                 */
                boolean claimed =
                        claimService.claim(paymentId);

                if (!claimed) {
                    log.debug(
                            "Scheduled payment {} was already claimed",
                            paymentId
                    );
                    continue;
                }

                /*
                 * Transaction 2:
                 *
                 * PROCESSING -> transfer -> COMPLETED
                 *
                 * If transfer fails, this transaction rolls back.
                 */
                executionService.execute(paymentId);

            } catch (Exception exception) {

                log.error(
                        "Scheduled payment failed: id={}, reason={}",
                        paymentId,
                        exception.getMessage(),
                        exception
                );

                /*
                 * Transaction 3:
                 *
                 * PROCESSING -> FAILED
                 */
                try {

                    failureService.markFailed(
                            paymentId,
                            exception.getMessage()
                    );

                } catch (Exception failureException) {

                    log.error(
                            "Unable to persist failure status for scheduled payment {}",
                            paymentId,
                            failureException
                    );
                }
            }
        }
    }
}