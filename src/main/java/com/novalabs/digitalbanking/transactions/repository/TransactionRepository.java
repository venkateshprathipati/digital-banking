package com.novalabs.digitalbanking.transactions.repository;

import com.novalabs.digitalbanking.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);

    Optional<Transaction> findByTransactionReference(String transactionReference);
}
