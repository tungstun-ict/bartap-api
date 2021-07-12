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

class PersonControllerIntegrationTest extends BarIntegrationTestLifeCycle {
    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get people")
    void getPeople() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("testPerson"))
                .andExpect(jsonPath("$[0].phoneNumber").value("0600000000"))
                .andExpect(jsonPath("$[0].user").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get people not allowed")
    void getPeopleNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get person")
    void getPerson() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people/%s", bar.getId(), person.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("testPerson"))
                .andExpect(jsonPath("$.phoneNumber").value("0600000000"))
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get person not allowed")
    void getPersonNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/people/%s", bar.getId(), person.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Add person")
    void addPerson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Customer");
        jsonObject.put("phoneNumber", "0600000000");

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/api/bars/%s/people", bar.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(jsonObject.get("name")))
                .andExpect(jsonPath("$.phoneNumber").value(jsonObject.get("phoneNumber")))
                .andExpect(jsonPath("$.user").doesNotExist());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Add person not allowed")
    void addPersonNotAllowed() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "CustomerNew");
        jsonObject.put("phoneNumber", "0600000000");

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/api/bars/%s/people", bar.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Update person")
    void updatePerson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "CustomerNew");
        jsonObject.put("phoneNumber", "0699999999");

        RequestBuilder request = MockMvcRequestBuilders
                .put(String.format("/api/bars/%s/people/%s", bar.getId(), person.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(jsonObject.get("name")))
                .andExpect(jsonPath("$.phoneNumber").value(jsonObject.get("phoneNumber")))
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Update person not allowed")
    void updatePersonNotAllowed() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "CustomerNew");
        jsonObject.put("phoneNumber", "0600000000");

        RequestBuilder request = MockMvcRequestBuilders
                .put(String.format("/api/bars/%s/people/%s", bar.getId(), person.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Delete person")
    void deletePerson() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/people/%s", bar.getId(), person.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Delete person not allowed")
    void deletePersonNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/people/%s", bar.getId(), person.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}