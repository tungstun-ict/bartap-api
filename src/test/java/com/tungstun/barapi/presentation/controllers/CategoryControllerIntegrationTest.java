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
//class CategoryControllerIntegrationTest extends BarIntegrationTestLifeCycle {
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Get categories")
//    void getCategories() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/categories", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].categoryId").exists())
//                .andExpect(jsonPath("$[0].name").value("Drinks"))
//                .andExpect(jsonPath("$[0].productType").value("DRINK"));
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Get categories not allowed")
//    void getCategoriesNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/categories", bar.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Get category")
//    void getCategory() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/categories/%s", bar.getId(), category.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.name").value("Drinks"))
//                .andExpect(jsonPath("$.productType").value("DRINK"));
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Get category not allowed")
//    void getCategoryNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(String.format("/api/bars/%s/categories/%s", bar.getId(), category.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Create category")
//    void createCategory() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "Bitterbal");
//        jsonObject.put("productType", "FOOD");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .post(String.format("/api/bars/%s/categories", bar.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.name").value("Bitterbal"))
//                .andExpect(jsonPath("$.productType").value("FOOD"));
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Get category not allowed")
//    void createCategoryNotAllowed() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "BitterbalNew");
//        jsonObject.put("productType", "DRINK");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .post(String.format("/api/bars/%s/categories", bar.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Update category")
//    void updateCategory() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "BitterbalNew");
//        jsonObject.put("productType", "DRINK");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(String.format("/api/bars/%s/categories/%s", bar.getId(), category.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").exists())
//                .andExpect(jsonPath("$.name").value("BitterbalNew"))
//                .andExpect(jsonPath("$.productType").value("DRINK"));
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Update category not allowed")
//    void updateCategoryNotAllowed() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "BitterbalNew");
//        jsonObject.put("productType", "DRINK");
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(String.format("/api/bars/%s/categories/%s", bar.getId(), category.getId()))
//                .content(jsonObject.toString())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "owner")
//    @DisplayName("Delete category")
//    void deleteCategory() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete(String.format("/api/bars/%s/categories/%s", bar.getId(), category.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "anonymous")
//    @DisplayName("Delete category not allowed")
//    void deleteCategoryNotAllowed() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete(String.format("/api/bars/%s/categories/%s", bar.getId(), category.getId()))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andExpect(status().isForbidden());
//    }
//}