package com.tungstun.barapi.application.product;

import com.tungstun.barapi.application.product.command.CreateProduct;
import com.tungstun.barapi.application.product.command.DeleteProduct;
import com.tungstun.barapi.application.product.command.UpdateProduct;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import com.tungstun.barapi.port.persistence.product.SpringProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProductCommandHandlerIntegrationTest {
    @Autowired
    private SpringCategoryRepository categoryRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringProductRepository productRepository;
    @Autowired
    private ProductCommandHandler productCommandHandler;

    private Bar bar;
    private Category category;
    private Product product;

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
        Product product3 = new ProductBuilder("product3", category3)
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
    @DisplayName("create product")
    void createProduct() throws EntityNotFoundException {
        CreateProduct command = new CreateProduct(
                bar.getId(),
                "testName",
                "testBrand",
                2.5,
                250d,
                true,
                ProductType.DRINK.toString(),
                category.getId()
        );

        UUID id = productCommandHandler.handle(command);

        Product actualProduct = productRepository.findById(id).orElseThrow();
        assertEquals(command.name(), actualProduct.getName());
        assertEquals(command.brand(), actualProduct.getBrand());
        assertEquals(command.size(), actualProduct.getSize());
        assertEquals(command.price(), actualProduct.getPrice().amount().doubleValue());
        assertTrue(actualProduct.isFavorite());
        assertEquals(command.productType(), actualProduct.getType().toString());
        assertEquals(command.categoryId(), actualProduct.getCategory().getId());
    }

    @Test
    @DisplayName("update product")
    void updateProduct() throws EntityNotFoundException {

        UpdateProduct command = new UpdateProduct(
                bar.getId(),
                product.getId(),
                "testNameNew",
                "testBrandNew",
                5d,
                50d,
                false,
                ProductType.FOOD.toString(),
                category.getId()
        );

        UUID id = productCommandHandler.handle(command);


        Product actualProduct = productRepository.findById(id).orElseThrow();
        assertEquals(command.name(), actualProduct.getName());
        assertEquals(command.brand(), actualProduct.getBrand());
        assertEquals(command.size(), actualProduct.getSize());
        assertEquals(command.price(), actualProduct.getPrice().amount().doubleValue());
        assertFalse(actualProduct.isFavorite());
        assertEquals(command.productType(), actualProduct.getType().toString());
        assertEquals(command.categoryId(), actualProduct.getCategory().getId());
    }

    @Test
    @DisplayName("delete product")
    void deleteProduct() {
        DeleteProduct command = new DeleteProduct(product.getId());

        assertDoesNotThrow(() -> productCommandHandler.handle(command));
    }
}
