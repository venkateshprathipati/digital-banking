package com.novalabs.digitalbanking.payment.service;

import com.novalabs.digitalbanking.payment.dto.PaymentHistoryPageResponse;
import com.novalabs.digitalbanking.payment.dto.PaymentHistoryResponse;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentHistoryService {

    private static final int MAX_PAGE_SIZE = 100;

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public PaymentHistoryPageResponse getPaymentHistory(
            Long accountId,
            int page,
            int size
    ) {
        validatePagination(page, size);

        int normalizedSize = Math.min(size, MAX_PAGE_SIZE);

        PageRequest pageable = PageRequest.of(
                page,
                normalizedSize,
                Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                )
        );

        Page<Payment> paymentPage =
                paymentRepository.findBySourceAccountIdOrDestinationAccountId(
                        accountId,
                        accountId,
                        pageable
                );

        return new PaymentHistoryPageResponse(
                paymentPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                paymentPage.getNumber(),
                paymentPage.getSize(),
                paymentPage.getTotalElements(),
                paymentPage.getTotalPages(),
                paymentPage.isFirst(),
                paymentPage.isLast()
        );
    }

    private PaymentHistoryResponse toResponse(
            Payment payment
    ) {
        return new PaymentHistoryResponse(
                payment.getPaymentReference(),
                payment.getSourceAccountId(),
                payment.getDestinationAccountId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }

    private void validatePagination(int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to zero"
            );
        }

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Size must be greater than zero"
            );
        }
    }
}