package com.novalabs.digitalbanking.payment.repository;

import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(
            String paymentReference
    );

    boolean existsByPaymentReference(
            String paymentReference
    );

    List<Payment> findBySourceAccountId(
            Long sourceAccountId
    );

    List<Payment> findByDestinationAccountId(
            Long destinationAccountId
    );

    List<Payment> findByStatus(
            PaymentStatus status
    );


}
