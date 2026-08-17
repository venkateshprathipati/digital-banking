package com.novalabs.digitalbanking.payment.integration;

import com.novalabs.digitalbanking.account.enums.Currency;
import com.novalabs.digitalbanking.payment.dto.PaymentHistoryPageResponse;
import com.novalabs.digitalbanking.payment.entity.Payment;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import com.novalabs.digitalbanking.payment.service.PaymentHistoryService;
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
class PaymentHistoryIntegrationTest {

    private static final Long ACCOUNT_ID = 1001L;
    private static final Long OTHER_ACCOUNT_ID = 2001L;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        createPaymentsForAccount();
        createPaymentForAnotherAccount();
    }

    @Test
    void shouldReturnPagingPaymentHistory() {
        //Act
        PaymentHistoryPageResponse response =
                paymentHistoryService.getPaymentHistory(
                        ACCOUNT_ID,
                        0,
                        10
                );

        //Assert
        assertThat(response).isNotNull();

        assertThat(response.page()).isEqualTo(0);

        assertThat(response.size())
                .isEqualTo(10);

        assertThat(response.totalElements())
                .isEqualTo(25);

        assertThat(response.totalPages())
                .isEqualTo(3);

        assertThat(response.first())
                .isTrue();

        assertThat(response.last())
                .isFalse();

        assertThat(response.content())
                .hasSize(10);

        assertThat(response.content())
                .allMatch(payment ->
                        payment.sourceAccountId().equals(ACCOUNT_ID)
                                || payment.destinationAccountId().equals(ACCOUNT_ID)
                );
    }


    private void createPaymentsForAccount() {
        for (int i = 1; i <= 25; i++) {
            Payment payment;
            if (i % 2 == 0) {
                payment = createPayment(
                        "PAY-ACCOUNT-" + i,
                        ACCOUNT_ID,
                        OTHER_ACCOUNT_ID,
                        new BigDecimal(i * 100)
                );
            } else {
                payment = createPayment(
                        "PAY-ACCOUNT-" + i,
                        OTHER_ACCOUNT_ID,
                        ACCOUNT_ID,
                        new BigDecimal(i * 100)
                );
            }
            paymentRepository.save(payment);
        }
        paymentRepository.flush();
    }


    private void createPaymentForAnotherAccount() {
        Payment payment = createPayment(
                "PAY-OTHER-001",
                3001L,
                3002L,
                new BigDecimal("500.00")
        );
        paymentRepository.saveAndFlush(payment);
    }


    private Payment createPayment(
            String reference,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount
    ) {
        Payment payment = Payment.builder()
                .paymentReference(reference)
                .sourceAccountId(sourceAccountId)
                .destinationAccountId(destinationAccountId)
                .amount(amount)
                .currency(Currency.INR)
                .build();

        payment.markProcessing();
        payment.markCompleted();

        return payment;
    }
}
