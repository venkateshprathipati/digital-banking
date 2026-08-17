package com.novalabs.digitalbanking.payment.service;

import com.novalabs.digitalbanking.account.entity.Account;
import com.novalabs.digitalbanking.account.enums.AccountStatus;
import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.payment.dto.TransferRequest;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class TransferServiceRollbackTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @MockitoSpyBean
    private PaymentRepository paymentRepositorySpy;

    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setup() {
        paymentRepository.deleteAll();
        accountRepository.deleteAll();

        sourceAccount = accountRepository.save(
                Account.builder()
                        .accountNumber("ACC-ROLLBACK-001")
                        .customerId(1001L)
                        .balance(new BigDecimal("10000.00"))
                        .status(AccountStatus.ACTIVE)
                        .currency(Currency.INR)
                        .version(0L)
                        .build()
        );

        destinationAccount = accountRepository.save(
                Account.builder()
                        .accountNumber("ACC-ROLLBACK-002")
                        .customerId(1002L)
                        .balance(new BigDecimal("5000.00"))
                        .status(AccountStatus.ACTIVE)
                        .currency(Currency.INR)
                        .version(0L)
                        .build()
        );
    }

    @Test
    void shouldRollbackAccountBalancesWhenPaymentSaveFails() {

        //Arrange
        BigDecimal originalSourceBalance =
                sourceAccount.getBalance();

        BigDecimal originalDestinationBalance =
                destinationAccount.getBalance();

        TransferRequest request = new TransferRequest(
                sourceAccount.getId(),
                destinationAccount.getId(),
                new BigDecimal("2000.00"),
                Currency.INR
        );

        /* Force PaymentRepository.save() to fail.
         *
         * The exception happens after:
         *
         * 1. Source account is loaded
         * 2. Destination account is loaded
         * 3. Source balance is reduced
         * 4. Destination balance is increased
         * 5. Payment object is created
         * Therefore the transaction must rollback
         * the account balance changes.
         *
         */

        doThrow(new RuntimeException("Simulate payment persistence failure"))
                .when(paymentRepositorySpy)
                .save(any(Payment.class));
        assertThrows(RuntimeException.class, () -> transferService.transfer(request));

        /*
         * Important:
         *
         * We must reload the entities from the database.
         * Do not assert against the already-loaded objects.
         */
        Account sourceAfterRollback = accountRepository.findById(sourceAccount.getId())
                .orElseThrow();

        Account destinationAfterRollback =
                accountRepository.findById(destinationAccount.getId())
                        .orElseThrow();

        //Assert
        assertEquals(originalSourceBalance,
                sourceAfterRollback.getBalance(),
                "Source account balance should be rolled back");

        assertEquals(originalDestinationBalance,
                destinationAfterRollback.getBalance(),
                "Destination account balance should be rolled back");

        assertEquals(0,
                paymentRepository.count(),
                "Payment should not be persisted after rollback");

    }
}
