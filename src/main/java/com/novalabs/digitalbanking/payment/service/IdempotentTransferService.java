package com.novalabs.digitalbanking.payment.service;

import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.idempotency.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotentTransferService {

    private final TransferService transferService;
    private final IdempotencyService idempotencyService;

    @Transactional
    public TransferResponse transfer(
            UUID userId,
            String idempotencyKey,
            TransferRequest request
    ) {
        Optional<TransferResponse> existing = idempotencyService.findExisting(
                userId, idempotencyKey, request);
        if (existing.isPresent()) {
            return existing.get();
        }

        idempotencyService.claim(
                userId, idempotencyKey, request);

        TransferResponse response = transferService.transfer(request);
        idempotencyService.complete(
                userId,
                idempotencyKey,
                response
        );
        return response;
    }
}
