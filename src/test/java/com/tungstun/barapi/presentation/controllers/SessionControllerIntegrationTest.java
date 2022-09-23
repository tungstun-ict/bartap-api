//package com.tungstun.barapi.presentation.controllers;
//
//import com.tungstun.barapi.port.persistence.session.SpringSessionRepository;
//import com.tungstun.barapi.presentation.controllers.setup.BarIntegrationTestLifeCycle;
//import org.json.JSONObject;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class SessionControllerIntegrationTest extends BarIntegrationTestLifeCycle {
//    @Autowired
//    private SpringSessionRepository repository;
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Get sessions")
//    void getSessions() throws Exception {
//        session.lock();
//        repository.save(session);
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].categoryId").exists())
//                .andExpect(jsonPath("$[0].name").value(session.getName()))
//                .andExpect(jsonPath("$[0].creationDate").exists())
//                .andExpect(jsonPath("$[0].closedDate").exists())
//                .andExpect(jsonPath("$[0].locked").value(true))
//                .andExpect(jsonPath("$[0].bills").exists());
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Get sessions Not Allowed")
//    void getSessionsNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Get session")
//    void getSession() throws Exception {
//        session.lock();
//        repository.save(session);
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.name").value(session.getName()))
//                .andExpect(jsonPath("$.creationDate").exists())
//                .andExpect(jsonPath("$.closedDate").exists())
//                .andExpect(jsonPath("$.locked").value(true))
//                .andExpect(jsonPath("$.bills").exists());
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Get session Not Allowed")
//    void getSessionNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Get active session")
//    void getActiveSession() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions/active", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.name").value(session.getName()))
//                .andExpect(jsonPath("$.creationDate").exists())
//                .andExpect(jsonPath("$.locked").value(false))
//                .andExpect(jsonPath("$.bills").exists());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Get active session when no session is active")
//    void getActiveSessionWhenNoActiveSession() throws Exception {
//        session.lock();
//        repository.save(session);
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions/active", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Get active session Not Allowed")
//    void getActiveSessionNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/sessions/active", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Create session when no session is active")
//    void createSession() throws Exception {
//        session.lock();
//        repository.save(session);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "session");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .post(String.format("/api/bars/%s/sessions", bar.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value(jsonObject.get("name")));
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Create session Not Allowed")
//    void createActiveSessionNotAllowed() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "session");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .post(String.format("/api/bars/%s/sessions", bar.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Update session")
//    void updateSession() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "sessionNew");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value(jsonObject.get("name")));
//    }
//
//
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Update session not editable")
//    void updateSessionNotEditable() throws Exception {
//        session.lock();
//        repository.save(session);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "sessionNew");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Update session Not Allowed")
//    void updateActiveSessionNotAllowed() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "sessionNew");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("End session")
//    void endSession() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .patch(String.format("/api/bars/%s/sessions/%s/end", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(customerId = "owner")
//    @DisplayName("End session not editable")
//    void endSessionNotEditable() throws Exception {
//        session.lock();
//        repository.save(session);
//        RequestBuilder request = MockMvcRequestBuilders
//                .patch(String.format("/api/bars/%s/sessions/%s/end", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockUser(customerId = "anonymous")
//    @DisplayName("End session Not Allowed")
//    void endSessionNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .patch(String.format("/api/bars/%s/sessions/%s/end", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(customerId = "owner")
//    @DisplayName("Lock session")
//    void lockSession() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .patch(String.format("/api/bars/%s/sessions/%s/lock", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(customerId = "anonymous")
//    @DisplayName("Lock session Not Allowed")
//    void lockSessionNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .patch(String.format("/api/bars/%s/sessions/%s/lock", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(customerId = "owner")
//    @DisplayName("Delete session")
//    void deleteSession() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(customerId = "anonymous")
//    @DisplayName("Delete session Not Allowed")
//    void deleteSessionNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete(String.format("/api/bars/%s/sessions/%s", bar.getId(), session.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//}