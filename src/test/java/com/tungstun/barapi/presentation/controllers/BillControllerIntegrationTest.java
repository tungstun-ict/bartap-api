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

class BillControllerIntegrationTest extends BarIntegrationTestLifeCycle {
    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Get bills")
    void getBills() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/bills", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].payed").value(false))
                .andExpect(jsonPath("$[0].totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$[0].customer").exists())
                .andExpect(jsonPath("$[0].orders").exists())
                .andExpect(jsonPath("$[0].session").exists());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("get bills which you arent connected with")
    void getBillsNotOwned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/bills", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Get bills of session")
    void getBillsOfSession() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills", bar.getId(), session.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].payed").value(false))
                .andExpect(jsonPath("$[0].totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$[0].customer").exists())
                .andExpect(jsonPath("$[0].orders").exists())
                .andExpect(jsonPath("$[0].session").exists());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("get bill of session which you arent connected with")
    void getBillOfSessionNotOwned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills", bar.getId(), session.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Get bill of session")
    void getBillOfSession() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills/%s", bar.getId(), session.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.payed").value(false))
                .andExpect(jsonPath("$.totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.session").exists());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Get bill of person from active session")
    void getBillOfPersonFromActiveSession() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/active/people/%s/bill", bar.getId(), ownerPerson.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.payed").value(false))
                .andExpect(jsonPath("$.totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.session").exists());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("get bill of person of active session not allowd")
    void getBillOfPersonOfActiveSessionNotOwned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/active/people/%s/bill", bar.getId(), ownerPerson.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("get bill of session which you arent connected with")
    void getBillsOfSessionNotOwned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/sessions/%s/bills/%s", bar.getId(), session.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Get bills of customer")
    void getBillsOfCustomer() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people/%s/bills", bar.getId(), ownerPerson.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].payed").value(false))
                .andExpect(jsonPath("$[0].totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$[0].session").exists());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("get bills of customer which you arent connected with")
    void getBillsOfCustomerNotOwned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people/%s/bills", bar.getId(), ownerPerson.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Get bill of customer")
    void getBillOfCustomer() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people/%s/bills/%s", bar.getId(), ownerPerson.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.payed").value(false))
                .andExpect(jsonPath("$.totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.session").exists());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("get bill of customer which you arent connected with")
    void getBillOfCustomerNotOwned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people/%s/bills/%s", bar.getId(), ownerPerson.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Create bill")
    void createBillForSession() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerId", anonymous.getId());

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/api/bars/%s/sessions/%s/", bar.getId(), session.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.payed").value(false))
                .andExpect(jsonPath("$.totalPrice").value(0d))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.session").exists());
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DisplayName("Create bill not allowed")
    void createBillForSessionNotAllowed() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerId", anonymous.getId());

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/api/bars/%s/sessions/%s/", bar.getId(), session.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Set isPayed bill")
    void setIsPayedBillForSession() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerId", anonymous.getId());

        RequestBuilder request = MockMvcRequestBuilders
                .patch(String.format("/api/bars/%s/sessions/%s/bills/%s?isPayed=true", bar.getId(), session.getId(), bill.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.payed").value(true))
                .andExpect(jsonPath("$.totalPrice").value(bill.calculateTotalPrice()))
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.session").exists());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("Set isPayed bill no bool")
    void setIsPayedBillForSessionNoBool() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerId", anonymous.getId());

        RequestBuilder request = MockMvcRequestBuilders
                .patch(String.format("/api/bars/%s/sessions/%s/bills/%s", bar.getId(), session.getId(), bill.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "owner")
    @DisplayName("delete bill")
    void deletePayedBillOfSession() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/sessions/%s/bills/%s", bar.getId(), session.getId(), bill.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}
