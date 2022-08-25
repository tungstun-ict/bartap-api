package com.tungstun.barapi.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProductBuilderTest {
    private ProductBuilder productBuilder;

    @BeforeEach
    void setUp() {
        Category category = new CategoryFactory("category").create();
        productBuilder = new ProductBuilder("product", category);
    }

    @Test
    @DisplayName("Build basic product")
    void buildBasicProduct() {
        assertDoesNotThrow(productBuilder::build);
    }

    @Test
    @DisplayName("Build full product")
    void buildProduct() {
        productBuilder.setBrand("brand")
                .setSize(200)
                .setFavorite(true)
                .setType(ProductType.DRINK)
                .setPrice(2.5);

        assertDoesNotThrow(productBuilder::build);
    }

    @Test
    @DisplayName("Build product has default values")
    void buildProductHasDefaultValues() {
        Product product = productBuilder.build();

        assertEquals(0, product.getSize());
        assertEquals(0.0, product.getPrice().amount().doubleValue());
        assertEquals("", product.getBrand());
        assertFalse(product.isFavorite());
        assertEquals(ProductType.OTHER, product.getType());
    }
}