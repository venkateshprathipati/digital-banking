package com.novalabs.digitalbanking.identity.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllAccounts_withoutAuthentication_shouldReturn401() throws Exception {
        mockMvc.perform(get("/v1/accounts")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "customer",
            roles = "CUSTOMER"
    )
    void getAllAccounts_asCustomer_shouldReturn403() throws Exception {
        mockMvc.perform(get("/v1/accounts")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "employee",
            roles = "EMPLOYEE"
    )
    void getAllAccounts_asEmployee_shouldReturn200() throws Exception{
        mockMvc.perform(get("/v1/accounts")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void getAllAccounts_asAdmin_shouldReturn200() throws Exception{
        mockMvc.perform(get("/v1/accounts")).andExpect(status().isOk());
    }


}
