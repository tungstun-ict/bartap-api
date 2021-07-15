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

class ProductControllerIntegrationTest extends BarIntegrationTestLifeCycle {
    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get products")
    void getProducts() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/products", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].brand").value(product.getBrand()))
                .andExpect(jsonPath("$[0].size").value(product.getSize()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].favorite").value(product.isFavorite()))
                .andExpect(jsonPath("$[0].category").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get products not allowed")
    void getProductsNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/products", bar.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Get product")
    void getProduct() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/products/%s", bar.getId(), product.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.brand").value(product.getBrand()))
                .andExpect(jsonPath("$.size").value(product.getSize()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.favorite").value(product.isFavorite()))
                .andExpect(jsonPath("$.category").exists());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Get product not allowed")
    void getProductNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(String.format("/api/bars/%s/products/%s", bar.getId(), product.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Create product")
    void createProduct() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "product2");
        jsonObject.put("brand", "brand");
        jsonObject.put("size", 400d);
        jsonObject.put("price", 2.5);
        jsonObject.put("isFavorite", false);
        jsonObject.put("categoryId", category.getId());


        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/api/bars/%s/products", bar.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(jsonObject.get("name")))
                .andExpect(jsonPath("$.brand").value(jsonObject.get("brand")))
                .andExpect(jsonPath("$.size").value(jsonObject.get("size")))
                .andExpect(jsonPath("$.price").value(jsonObject.get("price")))
                .andExpect(jsonPath("$.favorite").value(jsonObject.get("isFavorite")))
                .andExpect(jsonPath("$.category").value(jsonObject.get("categoryId")));
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Create product not allowed")
    void createProductNotAllowed() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "product2");
        jsonObject.put("brand", "brand");
        jsonObject.put("size", 400d);
        jsonObject.put("price", 2.5);
        jsonObject.put("isFavorite", false);
        jsonObject.put("categoryId", category.getId());

        RequestBuilder request = MockMvcRequestBuilders
                .post(String.format("/api/bars/%s/products", bar.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Update product")
    void updateProduct() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "product2New");
        jsonObject.put("brand", "brandNew");
        jsonObject.put("size", 200d);
        jsonObject.put("price", 3d);
        jsonObject.put("isFavorite", true);
        jsonObject.put("categoryId", category.getId());


        RequestBuilder request = MockMvcRequestBuilders
                .put(String.format("/api/bars/%s/products/%s", bar.getId(), product.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(jsonObject.get("name")))
                .andExpect(jsonPath("$.brand").value(jsonObject.get("brand")))
                .andExpect(jsonPath("$.size").value(jsonObject.get("size")))
                .andExpect(jsonPath("$.price").value(jsonObject.get("price")))
                .andExpect(jsonPath("$.favorite").value(jsonObject.get("isFavorite")))
                .andExpect(jsonPath("$.category").value(jsonObject.get("categoryId")));
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Update product not allowed")
    void updateProductNotAllowed() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "product2New");
        jsonObject.put("brand", "brandNew");
        jsonObject.put("size", 200d);
        jsonObject.put("price", 3d);
        jsonObject.put("isFavorite", true);
        jsonObject.put("categoryId", category.getId());

        RequestBuilder request = MockMvcRequestBuilders
                .put(String.format("/api/bars/%s/products/%s", bar.getId(), product.getId()))
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "BAR_OWNER")
    @DisplayName("Delete product")
    void deleteProduct() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/products/%s", bar.getId(), product.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "notConnectedUser", roles = "BAR_OWNER")
    @DisplayName("Delete product not allowed")
    void deleteProductNotAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete(String.format("/api/bars/%s/products/%s", bar.getId(), product.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}