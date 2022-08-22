package com.tungstun.barapi.domain.search.engine.product;

import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.search.compare.JaccardStringSimilarityComparer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductSearchEngineTest {
    @Autowired
    private ProductSearchEngine engine;
    private List<Product> source;

    @BeforeEach
    void setUp() {
        engine = new ProductSearchEngine(new JaccardStringSimilarityComparer());
        source = List.of(
                new ProductBuilder(123L, "Pils vaasje", null).setBrand("Heineken").build(),
                new ProductBuilder(123L, "Pils fluitje", null).setBrand("Heineken").build(),
                new ProductBuilder(123L, "Pils pitcher", null).setBrand("Heineken").build(),
                new ProductBuilder(123L, "Pils vaasje", null).setBrand("Amstel").build(),
                new ProductBuilder(123L, "Pils fluitje", null).setBrand("Amstel").build(),
                new ProductBuilder(123L, "Pils pitcher", null).setBrand("Amstel").build(),
                new ProductBuilder(123L, "BBQ borrelnootjes", null).setBrand("Duyvis").build(),
                new ProductBuilder(123L, "Tijger borrelnootjes", null).setBrand("Duyvis").build(),
                new ProductBuilder(123L, "Chardonnay", null).setBrand("Stoney Creek").build(),
                new ProductBuilder(123L, "Sauvignon blanc", null).setBrand("Stoney Creek").build(),
                new ProductBuilder(123L, "Juicy cider", null).setBrand("Apple bandit").build(),
                new ProductBuilder(123L, "Classic cider", null).setBrand("Apple bandit").build(),
                new ProductBuilder(123L, "Bessen Jenever", null).setBrand("Hooghoudt").build(),
                new ProductBuilder(123L, "Graan Jenever", null).setBrand("Hooghoudt").build()
        );
    }

    @Test
    @DisplayName("search 'Pils'")
    void searchPils() {
        List<Product> foundProducts = engine.search(source, "Pils");

        assertEquals(9, foundProducts.size());
    }
}