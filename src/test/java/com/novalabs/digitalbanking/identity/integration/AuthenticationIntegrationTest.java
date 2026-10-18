package com.novalabs.digitalbanking.identity.integration;

import com.novalabs.digitalbanking.support.PostgresTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(PostgresTestContainerConfig.class)
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Spring context, security configuration, database and identity infrastructure must start.
    }

    @Test
    void registerCustomer_shouldReturnCreatedAndCustomerRole() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "register.customer",
                                  "email": "register.customer@example.com",
                                  "password": "SecurePass123!",
                                  "role": "CUSTOMER"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }

    @Test
    void registerAdmin_shouldBeRejected() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "register.admin",
                                  "email": "register.admin@example.com",
                                  "password": "SecurePass123!",
                                  "role": "ADMIN"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void registerEmployee_shouldBeRejected() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "register.employee",
                                  "email": "register.employee@example.com",
                                  "password": "SecurePass123!",
                                  "role": "EMPLOYEE"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void registerRoleWithTrailingCharacters_shouldFailValidation() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "register.invalidrole",
                                  "email": "register.invalidrole@example.com",
                                  "password": "SecurePass123!",
                                  "role": "CUSTOMER123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
