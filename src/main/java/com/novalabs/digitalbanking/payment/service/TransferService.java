package com.novalabs.digitalbanking.payment.service;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.common.exception.ResourceNotFoundException;
import com.novalabs.digitalbanking.fraud.engine.FraudEngine;
import com.novalabs.digitalbanking.fraud.model.FraudContext;
import com.novalabs.digitalbanking.notification.event.PaymentCompletedEvent;
import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.dto.TransferResponse;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.factory.PaymentFactory;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import com.novalabs.digitalbanking.payment.validation.AccountTransferValidator;
import com.novalabs.digitalbanking.payment.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    private final TransferValidator transferValidator;
    private final AccountTransferValidator accountTransferValidator;
    private final PaymentFactory paymentFactory;
    private final FraudEngine fraudEngine;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        transferValidator.validate(request);

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

        accountTransferValidator.validate(
                sourceAccount,
                destinationAccount,
                request.amount(),
                request.currency());

        fraudEngine.evaluate(new FraudContext(
                sourceAccount,
                destinationAccount,
                request.amount()
        ));

        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(request.amount())
        );

        destinationAccount.setBalance(
                destinationAccount.getBalance().add(request.amount())
        );

        Payment payment = paymentFactory.create(
                sourceAccount.getId(),
                destinationAccount.getId(),
                request.amount(),
                request.currency()
        );

        payment.markCompleted();

        paymentRepository.save(payment);

        eventPublisher.publishEvent(
                new PaymentCompletedEvent(
                        payment.getPaymentReference(),
                        sourceAccount.getId(),
                        destinationAccount.getId(),
                        payment.getAmount(),
                        payment.getCurrency()
                )
        );

        return new TransferResponse(
                payment.getPaymentReference(),
                sourceAccount.getId(),
                destinationAccount.getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus()
        );

    }

    private Account getAccountForUpdate(Long accountId) {
        return accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found : " + accountId
                        ));
    }
}
