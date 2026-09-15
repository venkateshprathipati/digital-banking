package com.novalabs.digitalbanking.payment.scheduled.service;

import com.novalabs.digitalbanking.payment.scheduled.dto.CreateScheduledPaymentRequest;
import com.novalabs.digitalbanking.payment.scheduled.entity.ScheduledPayment;
import com.novalabs.digitalbanking.payment.scheduled.repository.ScheduledPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduledPaymentService {

    private final ScheduledPaymentRepository repository;

    @Transactional
    public ScheduledPayment create(UUID userId, CreateScheduledPaymentRequest request) {
        ScheduledPayment scheduledPayment =
                ScheduledPayment.builder()
                        .userId(userId)
                        .sourceAccountId(request.sourceAccountId())
                        .destinationAccountId(request.destinationAccountId())
                        .amount(request.amount())
                        .currency(request.currency())
                        .scheduledAt(request.scheduledAt())
                        .build();

        return repository.save(scheduledPayment);
    }

    @Transactional(readOnly = true)
    public ScheduledPayment get(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Scheduled payment not found: " + id)
                );
    }

    @Transactional
    public void cancel(Long id) {
        ScheduledPayment payment = get(id);
        payment.cancel();
    }
}
