package com.novalabs.digitalbanking.payment.service;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.common.exception.AccountFrozenException;
import com.novalabs.digitalbanking.common.exception.InsufficientBalanceException;
import com.novalabs.digitalbanking.common.exception.ResourceNotFoundException;
import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        validateRequest(request);

        Long firstLockId = Math.min(
                request.sourceAccountId(),
                request.destinationAccountId()
        );

        Long secondLockId = Math.max(
                request.sourceAccountId(),
                request.destinationAccountId()
        );

        Account firstAccount = getAccountForUpdate(firstLockId);
        Account secondAccount = getAccountForUpdate(secondLockId);

        Account sourceAccount;
        Account destinationAccount;

        if (firstAccount.getId().equals(request.sourceAccountId())) {
            sourceAccount = firstAccount;
            destinationAccount = secondAccount;
        } else {
            sourceAccount = secondAccount;
            destinationAccount = firstAccount;
        }

        validateAccounts(
                sourceAccount,
                destinationAccount,
                request.amount(),
                request.currency());

        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(request.amount())
        );

        destinationAccount.setBalance(
                destinationAccount.getBalance().add(request.amount())
        );

        Payment payment = Payment.builder()
                .paymentReference(generatePaymentReference())
                .sourceAccountId(sourceAccount.getId())
                .destinationAccountId(destinationAccount.getId())
                .amount(request.amount())
                .currency(request.currency())
                .build();

        payment.markProcessing();
        payment.markCompleted();

        paymentRepository.save(payment);
        return new TransferResponse(
                payment.getPaymentReference(),
                sourceAccount.getId(),
                destinationAccount.getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus()
        );

    }

    private static void validateAccounts(Account sourceAccount, Account destinationAccount, BigDecimal amount, Currency currency) {
        if (sourceAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException("Source account is not active");
        }

        if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException("Destination account is not active");
        }

        if (sourceAccount.getCurrency() != currency) {
            throw new IllegalArgumentException("Source account currency does not match transfer currency");
        }

        if (destinationAccount.getCurrency() != currency) {
            throw new IllegalArgumentException("Destination account currency does not match transfer currency");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }
    }

    private Account getAccountForUpdate(Long accountId) {
        return accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found : " + accountId
                        ));
    }

    private void validateRequest(TransferRequest request) {
        if (request.sourceAccountId().equals(request.destinationAccountId())) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }
    }

    private String generatePaymentReference() {
        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 20)
                        .toUpperCase();
    }

}
