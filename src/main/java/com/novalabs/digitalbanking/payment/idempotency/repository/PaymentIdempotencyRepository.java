package com.novalabs.digitalbanking.payment.idempotency.repository;

import com.novalabs.digitalbanking.payment.idempotency.entitiy.PaymentIdempotency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentIdempotencyRepository extends JpaRepository<PaymentIdempotency,Long> {

    Optional<PaymentIdempotency> findByUserIdAndIdempotencyKey(UUID userId, String idempotencyKey);

}
