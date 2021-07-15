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
                new ProductBuilder().setName("Pils vaasje").setBrand("Heineken").build(),
                new ProductBuilder().setName("Pils fluitje").setBrand("Heineken").build(),
                new ProductBuilder().setName("Pils pitcher").setBrand("Heineken").build(),
                new ProductBuilder().setName("Pils vaasje").setBrand("Amstel").build(),
                new ProductBuilder().setName("Pils fluitje").setBrand("Amstel").build(),
                new ProductBuilder().setName("Pils pitcher").setBrand("Amstel").build(),
                new ProductBuilder().setName("BBQ borrelnootjes").setBrand("Duyvis").build(),
                new ProductBuilder().setName("Tijger borrelnootjes").setBrand("Duyvis").build(),
                new ProductBuilder().setName("Chardonnay").setBrand("Stoney Creek").build(),
                new ProductBuilder().setName("Sauvignon blanc").setBrand("Stoney Creek").build(),
                new ProductBuilder().setName("Juicy cider").setBrand("Apple bandit").build(),
                new ProductBuilder().setName("Classic cider").setBrand("Apple bandit").build(),
                new ProductBuilder().setName("Bessen Jenever").setBrand("Hooghoudt").build(),
                new ProductBuilder().setName("Graan Jenever").setBrand("Hooghoudt").build()
        );
    }

    @Test
    @DisplayName("search 'Pils'")
    void searchPils() {
        List<Product> foundProducts = engine.search(source, "Pils");

        assertEquals(9, foundProducts.size());
    }
}