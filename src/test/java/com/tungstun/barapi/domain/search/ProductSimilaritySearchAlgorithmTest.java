package com.tungstun.barapi.domain.search;

import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductSimilaritySearchAlgorithmTest {
    private static List<Product> allProducts;
    private ProductSimilaritySearchAlgorithm searchAlgorithm;

    @BeforeAll
    static void beforeAll() {
        allProducts = List.of(
                new ProductBuilder("product", null).build(),
                new ProductBuilder("drink", null).build(),
                new ProductBuilder("food", null).build(),
                new ProductBuilder("product", null).setBrand("brand").build(),
                new ProductBuilder("drink", null).setBrand("tarp company").build(),
                new ProductBuilder("food", null).setBrand("bartap").build()
        );
    }

    @BeforeEach
    void setUp() {
        searchAlgorithm = new ProductSimilaritySearchAlgorithm();
    }

    @Test
    void searchForExactBrand_ReturnsProductOfBrand() {
        Collection<Product> products = searchAlgorithm.apply(allProducts, "brand");

        assertEquals(1, products.size());
    }

    @Test
    void searchForExactBrand_ReturnsProductOfTarpCompany() {
        Collection<Product> products = searchAlgorithm.apply(allProducts, "tarp company");

        assertTrue(products.size() >= 1);
    }

    @Test
    void searchForExactBrand_ReturnsProductOfBartap() {
        Collection<Product> products = searchAlgorithm.apply(allProducts, "bartap");

        assertEquals(1, products.size());
    }

    @Test
    void searchForPartialBrand_ReturnsProduct() {
        Collection<Product> products = searchAlgorithm.apply(allProducts, "tarp co");

        assertEquals(1, products.size());
    }

    @Test
    void searchNameAndBrandCombined_ReturnsProduct() {
        Collection<Product> products = searchAlgorithm.apply(allProducts, "product brand");

        assertTrue(products.size() >= 1);
    }

    @Test
    void searchBrandAndNameCombined_ReturnsProduct() {
        Collection<Product> products = searchAlgorithm.apply(allProducts, "product brand");

        assertTrue(products.size() >= 1);
    }
}
