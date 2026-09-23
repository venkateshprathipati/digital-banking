package com.novalabs.digitalbanking.transactions.service;

import com.novalabs.digitalbanking.payment.event.PaymentCompletedEvent;
import com.novalabs.digitalbanking.transactions.entity.Transaction;
import com.novalabs.digitalbanking.transactions.enums.TransactionStatus;
import com.novalabs.digitalbanking.transactions.enums.TransactionType;
import com.novalabs.digitalbanking.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionEventHandler {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void handle(PaymentCompletedEvent event) {
        Transaction debitTransaction = Transaction.builder()
                .transactionReference(generateReference())
                .accountId(event.sourceAccountId())
                .type(TransactionType.TRANSFER)
                .amount(event.amount())
                .currency(event.currency().name())
                .status(TransactionStatus.SUCCESS)
                .description("Transfer to account " + event.destinationAccountId())
                .build();

        Transaction creditTransaction = Transaction.builder()
                .transactionReference(generateReference())
                .accountId(event.destinationAccountId())
                .type(TransactionType.TRANSFER)
                .amount(event.amount())
                .currency(event.currency().name())
                .status(TransactionStatus.SUCCESS)
                .description("Transfer from account " + event.sourceAccountId())
                .build();

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        log.info("Transaction history created for paymentReference={}",
                event.paymentReference());
    }

    private String generateReference() {
        return "TXN-" + UUID.randomUUID();
    }
}
