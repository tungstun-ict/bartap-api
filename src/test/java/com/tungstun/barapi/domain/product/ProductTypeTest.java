package com.tungstun.barapi.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTypeTest {

    @ParameterizedTest
    @EnumSource(ProductType.class)
    @DisplayName("Get product type with uppercase successfully")
    void getProductTypeUpper(ProductType productType) {
        assertDoesNotThrow(() -> ProductType.getProductType(productType.toString().toUpperCase()));
    }

    @ParameterizedTest
    @EnumSource(ProductType.class)
    @DisplayName("Get product type with lowercase successfully")
    void getProductTypeLower(ProductType productType) {
        assertDoesNotThrow(() -> ProductType.getProductType(productType.toString().toLowerCase()));
    }

    @Test
    @DisplayName("Get product type with empty string")
    void getProductTypeWrongValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ProductType.getProductType("")
        );
    }
    @Test
    @DisplayName("Get product type with wrong string")
    void getProductTypeWrongValue2() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ProductType.getProductType("test wrong")
        );
    }
}