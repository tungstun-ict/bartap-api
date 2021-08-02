package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.presentation.controllers.setup.BarIntegrationTestLifeCycle;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockControllerTest extends BarIntegrationTestLifeCycle {
    private String defaultUrl;
    private JSONObject jsonObject;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        defaultUrl = String.format("/api/bars/%s/products/%s/stock", bar.getId(), product.getId());
        jsonObject = new JSONObject();
        jsonObject.put("amount", 100L);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get product's stock")
    void getStock() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(defaultUrl)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(product.getStock().getAmount()));
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get product's stock not allowed")
    void getStockNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(defaultUrl)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Increase product's stock")
    void increaseStock() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch(defaultUrl + "/increase")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        long newValue = product.getStock().getAmount() + (Long) jsonObject.get("amount");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(newValue));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Decrease product's stock")
    void decreaseStock() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch(defaultUrl + "/decrease")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        long newValue = product.getStock().getAmount() - (Long) jsonObject.get("amount");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(newValue));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Update product's stock")
    void updateStock() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch(defaultUrl + "/update")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        long newValue = (Long) jsonObject.get("amount");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(newValue));
    }

    @ParameterizedTest
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @ValueSource(strings = {"/increase", "/decrease", "/update"})
    @DisplayName("Get product's stock")
    void stockControllerForbiddenCalls(String url) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch(defaultUrl + url)
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}