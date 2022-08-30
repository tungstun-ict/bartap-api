package com.tungstun.barapi.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryFactoryTest {
    @Test
    @DisplayName("Create category from CategoryFactory")
    void createCategory() {
        String name = "category";

        Category category = new CategoryFactory(name).create();

        assertNotNull(category.getId());
        assertEquals(name, category.getName());
    }
}