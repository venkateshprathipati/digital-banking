package com.novalabs.digitalbanking.payment.idempotency.service;

import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class RequestFingerprintService {

    public String generate(TransferRequest request) {
        String canonicalRequest = String.join(
                "|", String.valueOf(request.sourceAccountId()),
                String.valueOf(request.destinationAccountId()),
                normalizeAmount(request.amount()),
                request.currency().name()
        );
        return sha256(canonicalRequest);
    }

    private String normalizeAmount(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(
                        String.format("%02x", b)
                );
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is not available",
                    e
            );
        }
    }
}
