//package com.tungstun.barapi.presentation.controllers;
//
//import com.tungstun.barapi.presentation.controllers.setup.BarIntegrationTestLifeCycle;
//import org.json.JSONObject;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class BarControllerIntegrationTest extends BarIntegrationTestLifeCycle {
//    @Test
//    @WithMockUser(userId = "owner")
//    @DisplayName("get bars")
//    void getBars() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get("/api/bars")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].categoryId").exists());
//    }
//
//    @Test
//    @WithMockUser(userId = "anonymous")
//    @DisplayName("get bars empty")
//    void getBarEmpty() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get("/api/bars")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").doesNotExist());
//    }
//
//    @Test
//    @WithMockUser(userId = "owner")
//    @DisplayName("get bar")
//    void getBar() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get("/api/bars/"+bar.getId())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").value(bar.getId()))
//                .andExpect(jsonPath("$.address").value(bar.getDetails().getAddress()))
//                .andExpect(jsonPath("$.name").value(bar.getDetails().getName()))
//                .andExpect(jsonPath("$.mail").value(bar.getDetails().getMail()))
//                .andExpect(jsonPath("$.phoneNumber").value(bar.getDetails().getPhoneNumber()));
//    }
//
//    @Test
//    @WithMockUser(userId = "anonymous")
//    @DisplayName("get bar which you arent connected with")
//    void getBarNotOwned() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get("/api/bars/"+bar.getId())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(userId = "owner")
//    @DisplayName("create bar")
//    void createBar() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("address", "address 1");
//        jsonObject.put("name", "bar2");
//        jsonObject.put("mail", "mail@mail.com");
//        jsonObject.put("phoneNumber", "0600000000");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/api/bars/")
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.address").value(jsonObject.get("address")))
//                .andExpect(jsonPath("$.name").value(jsonObject.get("name")))
//                .andExpect(jsonPath("$.mail").value(jsonObject.get("mail")))
//                .andExpect(jsonPath("$.phoneNumber").value(jsonObject.get("phoneNumber")));
//    }
//
//    @Test
//    @WithMockUser(userId = "owner")
//    @DisplayName("update bar")
//    void updateBar() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("address", "address new");
//        jsonObject.put("name", "bar new");
//        jsonObject.put("mail", "mail@mail.com new");
//        jsonObject.put("phoneNumber", "0600000000 new");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put("/api/bars/" + bar.getId())
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.address").value(jsonObject.get("address")))
//                .andExpect(jsonPath("$.name").value(jsonObject.get("name")))
//                .andExpect(jsonPath("$.mail").value(jsonObject.get("mail")))
//                .andExpect(jsonPath("$.phoneNumber").value(jsonObject.get("phoneNumber")));
//    }
//
//    @Test
//    @WithMockUser(userId = "anonymous")
//    @DisplayName("update bar which you arent connected with")
//    void updateBarNotOwned() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("address", "address new");
//        jsonObject.put("name", "bar new");
//        jsonObject.put("mail", "mail@mail.com new");
//        jsonObject.put("phoneNumber", "0600000000 new");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put("/api/bars/" + bar.getId())
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(userId = "owner")
//    @DisplayName("delete bar ")
//    void deleteBar() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete("/api/bars/" + bar.getId())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(userId = "anonymous")
//    @DisplayName("delete bar which you arent connected with")
//    void deleteBarNotOwned() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete("/api/bars/" + bar.getId())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//}