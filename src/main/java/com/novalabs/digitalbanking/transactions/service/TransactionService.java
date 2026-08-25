package com.novalabs.digitalbanking.transactions.service;

import com.novalabs.digitalbanking.common.exception.ResourceNotFoundException;
import com.novalabs.digitalbanking.transactions.dto.TransactionResponse;
import com.novalabs.digitalbanking.transactions.entity.Transaction;
import com.novalabs.digitalbanking.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository repository;

    /**
     * Fetch transaction history for an account.
     * Transactions are returned newest first.
     */
    public List<TransactionResponse> findByAccountId(Long accountId) {

        List<Transaction> transactions = repository.findByAccountIdOrderByCreatedAtDesc(accountId);

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Fetch a single transaction by its database ID.
     */
    public TransactionResponse findById(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found : " + id
                        ));

        return toResponse(transaction);
    }

    /**
     * Fetch a transaction using the external business reference.
     */
    public TransactionResponse findByReference(
            String transactionReference
    ) {
        Transaction transaction =
                repository.findByTransactionReference(transactionReference)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Transaction not found : " + transactionReference));

        return toResponse(transaction);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionReference(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }

}
