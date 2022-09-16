package com.tungstun.barapi.domain.search;

import com.tungstun.barapi.domain.product.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductSearchEngineTest {
    private static final Long BAR_ID = 123L;

    private static Category category;
    private static Category category2;
    private static List<Product> allProducts;

    private ProductSearchEngine searchEngine;

    @BeforeAll
    static void beforeAll() throws IllegalAccessException {
        category = new CategoryFactory("category").create();
        category2 = new CategoryFactory("category2").create();

        allProducts = List.of(
                new ProductBuilder("product", category).build(),
                new ProductBuilder("product", category).setType(ProductType.DRINK).build(),
                new ProductBuilder("product", category).setFavorite(true).build(),
                new ProductBuilder("product", category).setBrand("brandbrandbrand").build(),
                new ProductBuilder("product", category2).build(),
                new ProductBuilder("product", category2).setType(ProductType.DRINK).build(),
                new ProductBuilder("product", category2).setFavorite(true).build(),
                new ProductBuilder("product", category2).setBrand("brandbrandbrand").setPrice(2.5d).build()
        );
    }

    @BeforeEach
    void setUp() {
        searchEngine = new ProductSearchEngine(new ProductSimilaritySearchAlgorithm());
    }

    @Test
    void searchAllProductsWithoutFilters_ReturnsAllProducts() {
        Collection<Product> products = searchEngine
                .search(allProducts);

        assertEquals(allProducts.size(), products.size());
    }

    @Test
    void searchProductsWithBrandName_ReturnsTwoProducts() {
        Collection<Product> products = searchEngine
                .addSearchText("brandbrandbrand")
                .search(allProducts);

        assertEquals(2, products.size());
    }

    @Test
    void searchProductsOfCategory_returnsAllCategoryProducts() {
        Collection<Product> products = searchEngine
                .addCategoryIdFilter(category.getId())
                .search(allProducts);

        assertEquals(4, products.size());
    }

    @Test
    void searchProductsOfTypeDrink_returnsTwoProducts() {
        Collection<Product> products = searchEngine
                .addProductTypeFilter(ProductType.DRINK)
                .search(allProducts);

        assertEquals(2, products.size());
    }

    @Test
    void searchProductsOfTypeFood_returnsZeroProducts() {
        Collection<Product> products = searchEngine
                .addProductTypeFilter(ProductType.FOOD)
                .search(allProducts);

        assertTrue(products.isEmpty());
    }

    @Test
    void searchProductsOfTypeOther_returnsSixProducts() {
        Collection<Product> products = searchEngine
                .addProductTypeFilter(ProductType.OTHER)
                .search(allProducts);

        assertEquals(6, products.size());
    }

    @Test
    void searchProductsWithCustomPriceIsAboveFilter_ReturnsOneProduct() {
        Collection<Product> products = searchEngine
                .addCustomFilter(product -> product.getPrice() != null && product.getPrice().amount().doubleValue() >= 2.0d)
                .search(allProducts);

        assertEquals(1, products.size());
    }
}
