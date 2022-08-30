package com.tungstun.barapi.application.product;

import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.application.product.query.ListProductsOfBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ProductQueryHandlerIntegrationTest {
    @Autowired
    private SpringCategoryRepository categoryRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private ProductQueryHandler productQueryHandler;

    private Bar bar;
    private Category category;
    private Product product;
    private Product product3;

    @BeforeEach
    void setup() {
        category = categoryRepository.save(new Category(UUID.randomUUID(), "Drinks"));
        Category category2 = categoryRepository.save(new Category(UUID.randomUUID(), "Food"));
        Category category3 = categoryRepository.save(new Category(UUID.randomUUID(), "Other"));

        product = new ProductBuilder("product", category)
                .setPrice(1.0)
                .setSize(100)
                .setType(ProductType.DRINK)
                .setBrand("meet")
                .build();
        Product product2 = new ProductBuilder("product2", category2)
                .setPrice(2.0)
                .setSize(200)
                .setType(ProductType.FOOD)
                .setBrand("your")
                .setFavorite(true)
                .build();
        product3 = new ProductBuilder("product3", category3)
                .setPrice(3.0)
                .setSize(300)
                .setType(ProductType.OTHER)
                .setBrand("maker")
                .build();

        bar = new BarBuilder("bar")
                .setCategories(List.of(category, category2, category3))
                .setProducts(List.of(product, product2, product3))
                .build();

        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("List all products of bar")
    void listProductsOfBar() throws EntityNotFoundException {
        List<Product> resProducts = productQueryHandler.handle(
                new ListProductsOfBar(bar.getId(), null, null, null, null)
        );

        assertEquals(3, resProducts.size());
    }

    @Test
    @DisplayName("List all products of bar based on category id")
    void listProductsOfBar_Category() throws EntityNotFoundException {
        List<Product> resProducts = productQueryHandler.handle(
                new ListProductsOfBar(bar.getId(), category.getId(), null, null, null)
        );

        assertEquals(1, resProducts.size());
        assertEquals(product.getId(), resProducts.get(0).getId());
    }

    @ParameterizedTest
    @CsvSource({
            "true, 1",
            "false, 2"
    })
    @DisplayName("List all products of bar based on favorite")
    void listProductsOfBar_Favorite(boolean isFavorite, int expectedAmount) throws EntityNotFoundException {
        List<Product> resProducts = productQueryHandler.handle(
                new ListProductsOfBar(bar.getId(), null, isFavorite, null, null)
        );

        assertEquals(expectedAmount, resProducts.size());
    }

    @Test
    @DisplayName("List all products of bar based on product type")
    void listProductsOfBar_ProductType() throws EntityNotFoundException {
        List<Product> resProducts = productQueryHandler.handle(
                new ListProductsOfBar(bar.getId(), null, null, product.getType().toString(), null)
        );

        assertEquals(1, resProducts.size());
        assertEquals(product.getId(), resProducts.get(0).getId());
    }

    @Test
    @DisplayName("List all products of bar based on search term")
    void listProductsOfBar_SearchTerm() throws EntityNotFoundException {
        List<Product> resProducts = productQueryHandler.handle(
                new ListProductsOfBar(bar.getId(), null, null, null, "maker")
        );

        assertEquals(1, resProducts.size());
        assertEquals(product3.getId(), resProducts.get(0).getId());
    }

    @Test
    @DisplayName("Get product of bar")
    void getProductOfBar() throws EntityNotFoundException {
        Product resProduct = productQueryHandler.handle(new GetProduct(bar.getId(), product.getId()));

        assertEquals(product.getId(), resProduct.getId());
    }

    @Test
    @DisplayName("Get not existing product of bar")
    void getNotExistingProductOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> productQueryHandler.handle(new GetProduct(bar.getId(), UUID.randomUUID()))
        );
    }
}
