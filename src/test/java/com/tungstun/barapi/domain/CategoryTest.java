package com.tungstun.barapi.domain;

import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {
    private static Stream<Arguments> provideConstructorArguments() {
        return Stream.of(
                Arguments.of("category", ProductType.DRINK),
                Arguments.of("category", ProductType.FOOD),
                Arguments.of("category", ProductType.OTHER)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    @DisplayName("Correct constructor values, does not throw")
    void constructorTest(String name, ProductType type) {
        assertDoesNotThrow(
                () -> new Category(name, type)
        );
    }

    @Test
    @DisplayName("get name returns name")
    void getName() {
        String name = "category";
        Category category = new Category(name, ProductType.DRINK);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("set name sets name correctly")
    void setName() {
        Category category = new Category("category", ProductType.DRINK);
        String name = "name";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("get product type")
    void getProductType() {
        Category category = new Category("category", ProductType.DRINK);
        assertEquals(ProductType.DRINK, category.getProductType());
    }

    @Test
    @DisplayName("Set product types correctly")
    void setProductType() {
        for (ProductType type : ProductType.values()) {
            Category category = new Category("category", type);
            assertEquals(type, category.getProductType());
        }
    }
}


