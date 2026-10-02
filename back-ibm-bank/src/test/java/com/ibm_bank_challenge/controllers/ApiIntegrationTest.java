package com.ibm_bank_challenge.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void invalidCustomerIsRejected() throws Exception {
        mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"age\":-1,\"email\":\"x\",\"accountNumber\":\"1\",\"branch\":\"1\",\"bankName\":\"B\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {
        String email = UUID.randomUUID() + "@test.com";
        createCustomer(email, "111-1");
        mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(customerJson(email, "222-2")))
                .andExpect(status().isConflict());
    }

    @Test
    void unknownCustomerReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/customers/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));
    }

    @Test
    void depositWithdrawAndTransferFlow() throws Exception {
        String a = createCustomer(UUID.randomUUID() + "@test.com", "301-1");
        String b = createCustomer(UUID.randomUUID() + "@test.com", "302-2");

        transaction(null, a, "100.00", "DEPOSIT", 201);
        transaction(a, b, "40.00", "TRANSFER", 201);
        transaction(a, null, "100.00", "WITHDRAWAL", 400); // only 60 left
        transaction(a, null, "-5", "WITHDRAWAL", 400);
        transaction(a, a, "1.00", "TRANSFER", 400);
        transaction(a, null, "10.00", "NOPE", 400);

        mockMvc.perform(get("/api/customers/" + a)).andExpect(jsonPath("$.balance").value(60.00));
        mockMvc.perform(get("/api/customers/" + b)).andExpect(jsonPath("$.balance").value(40.00));
        mockMvc.perform(get("/api/transactions/" + a + "?size=10"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    private String createCustomer(String email, String account) throws Exception {
        String body = mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson(email, account)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(body);
        return node.get("id").asText();
    }

    private String customerJson(String email, String account) {
        return "{\"name\":\"Test\",\"age\":30,\"email\":\"" + email + "\",\"accountNumber\":\"" + account
                + UUID.randomUUID().toString().substring(0, 4) + "\",\"branch\":\"1234\",\"bankName\":\"Banco\"}";
    }

    private void transaction(String sender, String receiver, String amount, String type, int expected) throws Exception {
        String body = "{\"senderId\":" + (sender == null ? "null" : "\"" + sender + "\"")
                + ",\"receiverId\":" + (receiver == null ? "null" : "\"" + receiver + "\"")
                + ",\"amount\":" + amount + ",\"transactionType\":\"" + type + "\"}";
        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is(expected));
    }
}
