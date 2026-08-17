package com.novalabs.digitalbanking.payment.integration;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import com.novalabs.digitalbanking.payment.service.TransferService;
import com.novalabs.digitalbanking.support.PostgresTestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresTestContainerConfig.class)
class PaymentTransferIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransferService transferService;

    private Account sourceAccount;

    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        accountRepository.deleteAll();

        sourceAccount = accountRepository.save(
                Account.builder()
                        .accountNumber("ACC-TEST-001")
                        .customerId(1001L)
                        .balance(new BigDecimal("10000.00"))
                        .status(AccountStatus.ACTIVE)
                        .currency(Currency.INR)
                        .version(0L)
                        .build()
        );

        destinationAccount = accountRepository.save(
                Account.builder()
                        .accountNumber("ACC-TEST-002")
                        .customerId(1002L)
                        .balance(new BigDecimal("5000.00"))
                        .status(AccountStatus.ACTIVE)
                        .currency(Currency.INR)
                        .version(0L)
                        .build()
        );
    }

    @Test
    void shouldTransferMoneyAndPersistPayment() {

        var request = new TransferRequest(
                sourceAccount.getId(),
                destinationAccount.getId(),
                new BigDecimal("2000.00"),
                Currency.INR
        );

        var response = transferService.transfer(request);

        Account sourceAfterTransfer =
                accountRepository.findById(sourceAccount.getId())
                        .orElseThrow();

        Account destinationAfterTransfer = accountRepository.findById(destinationAccount.getId())
                .orElseThrow();

        Payment payment = paymentRepository.findByPaymentReference(
                response.paymentReference()
        ).orElseThrow();

        assertThat(sourceAfterTransfer.getBalance())
                .isEqualByComparingTo("8000.00");
        assertThat(destinationAfterTransfer.getBalance())
                .isEqualByComparingTo("7000.00");
        assertThat(payment.getAmount())
                .isEqualByComparingTo("2000.00");
        assertThat(payment.getStatus())
                .isEqualTo(response.status());
    }
}
