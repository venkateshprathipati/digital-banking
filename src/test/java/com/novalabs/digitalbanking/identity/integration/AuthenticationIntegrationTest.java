package com.novalabs.digitalbanking.identity.integration;

import com.novalabs.digitalbanking.support.PostgresTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresTestContainerConfig.class)
class AuthenticationIntegrationTest {

    @Test
    void contextLoads() {
        // Spring context, security configuration,
        // database and identity infrastructure must start successfully.
    }
}