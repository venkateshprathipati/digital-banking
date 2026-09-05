package com.novalabs.digitalbanking.payment.idempotency.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novalabs.digitalbanking.common.exception.IdempotencyKeyReuseException;
import com.novalabs.digitalbanking.common.exception.IdempotencyRequestProcessingException;
import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.idempotency.entitiy.IdempotencyStatus;
import com.novalabs.digitalbanking.payment.idempotency.entitiy.PaymentIdempotency;
import com.novalabs.digitalbanking.payment.idempotency.repository.PaymentIdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final PaymentIdempotencyRepository repository;
    private final RequestFingerprintService fingerprintService;
    private final ObjectMapper objectMapper;

    @Transactional
    public Optional<TransferResponse> findExisting(
            UUID userId, String idempotencyKey, TransferRequest request
    ) {
        PaymentIdempotency record = repository.findByUserIdAndIdempotencyKey(
                userId, idempotencyKey
        ).orElse(null);

        if (record == null) return Optional.empty();

        String requestHash = fingerprintService.generate(request);
        validateFingerprint(record, requestHash);

        if (record.getStatus() == IdempotencyStatus.COMPLETED) {
            return Optional.of(
                    deserializeResponse(
                            record.getResponsePayload()
                    )
            );

        }
        return Optional.empty();
    }

    @Transactional
    public void claim(
            UUID userId,
            String idempotencyKey,
            TransferRequest request
    ) {
        validateKey(idempotencyKey);

        String requestHash = fingerprintService.generate(request);

        PaymentIdempotency record = new PaymentIdempotency(
                userId, idempotencyKey, requestHash
        );

        try {
            repository.saveAndFlush(record);
        } catch (DataIntegrityViolationException exception) {
            PaymentIdempotency existing = repository.findByUserIdAndIdempotencyKey(
                    userId, idempotencyKey
            ).orElseThrow(() -> exception);

            validateFingerprint(
                    existing,
                    requestHash
            );

            if (existing.getStatus() == IdempotencyStatus.COMPLETED) {
                return;
            }
            throw exception;
        }
    }

    @Transactional
    public void complete(UUID userId, String idempotencyKey, TransferResponse response) {
        PaymentIdempotency record = repository.findByUserIdAndIdempotencyKey(
                userId,
                idempotencyKey
        ).orElseThrow(() ->
                new IllegalStateException(
                        "Idempotency record not found"
                )
        );

        String payload = serializeResponse(response);

        record.markCompleted(
                response.paymentReference(),
                payload
        );

        repository.save(record);

    }

    public void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(
                    "Idempotency-Key header is required"
            );
        }

        if (key.length() > 100) {
            throw new IllegalArgumentException(
                    "Idempotency-Key must not exceed 100 characters"
            );
        }
    }

    private void validateFingerprint(
            PaymentIdempotency record,
            String requestHash
    ) {
        if (!record.getRequestHash().equals(requestHash)) {
            throw new IdempotencyKeyReuseException("Idempotency key was reused with a different request");
        }

        if (record.getStatus() == IdempotencyStatus.PROCESSING) {
            throw new IdempotencyRequestProcessingException(
                    "Request with this idempotency key is already being processed"
            );
        }
    }

    private String serializeResponse(TransferResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Unable to serialize transfer response", exception
            );
        }
    }

    private TransferResponse deserializeResponse(String payload) {
        try {
            return objectMapper.readValue(payload, TransferResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Unable to deserialize transfer response", exception
            );
        }
    }
}
