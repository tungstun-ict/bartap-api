package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.presentation.controllers.setup.BarIntegrationTestLifeCycle;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIntegrationTest extends BarIntegrationTestLifeCycle {
    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get orders")
    void getOrders() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/orders", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].amount").value(1d))
                .andExpect(jsonPath("$[0].creationDate").exists())
                .andExpect(jsonPath("$[0].product").exists())
                .andExpect(jsonPath("$[0].bartender").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get orders not allowed")
    void getOrdersNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/orders", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get orders of session")
    void getOrdersOfSession() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/orders", bar.getId(), session.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].amount").value(1d))
                .andExpect(jsonPath("$[0].creationDate").exists())
                .andExpect(jsonPath("$[0].product").exists())
                .andExpect(jsonPath("$[0].bartender").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get orders of session not allowed")
    void getOrdersOfSessionsNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/orders", bar.getId(), session.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get order of session")
    void getOrderOfSession() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/orders/%s", bar.getId(), session.getId(), order.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(1d))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.product").exists())
                .andExpect(jsonPath("$.bartender").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get order of session not allowed")
    void getOrderOfSessionsNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/orders/%s", bar.getId(), session.getId(), order.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get orders of bill")
    void getOrdersOfBill() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills/%s/orders", bar.getId(), session.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].amount").value(1d))
                .andExpect(jsonPath("$[0].creationDate").exists())
                .andExpect(jsonPath("$[0].product").exists())
                .andExpect(jsonPath("$[0].bartender").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get orders of bill not allowed")
    void getOrdersOfBillsNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills/%s/orders", bar.getId(), session.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get order of bill")
    void getOrderOfBill() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills/%s/orders/%s", bar.getId(), session.getId(), bill.getId(), order.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(1d))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.product").exists())
                .andExpect(jsonPath("$.bartender").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get order of bill not allowed")
    void getOrderOfBillsNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills/%s/orders/%s", bar.getId(), session.getId(), bill.getId(), order.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Delete order of bill")
    void deleteOrderOfBill() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/sessions/%s/bills/%s/orders/%s", bar.getId(), session.getId(), bill.getId(), order.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Delete order of bill not allowed")
    void deleteOrderOfBillsNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/sessions/%s/bills/%s/orders/%s", bar.getId(), session.getId(), bill.getId(), order.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Add order to bill")
    void addOrderToBill() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("productId", product.getId());
        jsonObject.put("amount", 1);

        RequestBuilder request = MockMvcRequestBuilders
                .put(String.format("/api/bars/%s/sessions/%s/bills/%s", bar.getId(), session.getId(), bill.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.payed").value(false))
                .andExpect(jsonPath("$.totalPrice").value(2d))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.session").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Add order to bill not allowed")
    void addOrderToBillsNotAllowed() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("productId", product.getId());
        jsonObject.put("amount", 1);
        RequestBuilder request = MockMvcRequestBuilders
                .put(String.format("/api/bars/%s/sessions/%s/bills/%s", bar.getId(), session.getId(), bill.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}