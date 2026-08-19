package com.novalabs.digitalbanking;

import com.novalabs.digitalbanking.account.repository.AccountRepository;
import com.novalabs.digitalbanking.payment.repository.PaymentRepository;
import com.novalabs.digitalbanking.support.PostgresTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresTestContainerConfig.class)
class DigitalBankingApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	void contextLoads() {

		assertThat(dataSource)
				.isNotNull();

		assertThat(accountRepository)
				.isNotNull();

		assertThat(paymentRepository)
				.isNotNull();
	}
}