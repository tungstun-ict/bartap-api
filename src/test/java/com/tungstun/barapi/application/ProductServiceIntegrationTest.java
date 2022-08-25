package com.tungstun.barapi.application;

import com.tungstun.barapi.application.product.ProductQueryHandler;
import com.tungstun.barapi.application.product.ProductService;
import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.directory.InvalidAttributesException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProductServiceIntegrationTest {
    @Autowired
    private SpringCategoryRepository categoryRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private ProductService service;
    @Autowired
    private ProductQueryHandler productQueryHandler;

    private Bar bar;
    private Category category;
    private Product product;

    @BeforeEach
    void setup() {
        bar = new BarBuilder("bar").build();
        category = categoryRepository.save(new Category(UUID.randomUUID(), "Drinks"));
        Category category2 = categoryRepository.save(new Category(UUID.randomUUID(), "Food"));
        Category category3 = categoryRepository.save(new Category(UUID.randomUUID(), "Other"));
        bar.addCategory(category);
        bar.addCategory(category2);
        bar.addCategory(category3);

        product = new ProductBuilder("product", category)
                .setPrice(1.0)
                .setSize(100)
                .build();
        Product product2 = new ProductBuilder("product2", category2)
                .setPrice(2.0)
                .setSize(200)
                .build();
        Product product3 = new ProductBuilder("product3", category3)
                .setPrice(3.0)
                .setSize(300)
                .build();
        bar.addProduct(product);
        bar.addProduct(product2);
        bar.addProduct(product3);
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Get product of bar")
    void getProductOfBar() throws EntityNotFoundException {
        Product resProduct = productQueryHandler.handle(new GetProduct(product.getId(), bar.getId()));

        assertEquals(product, resProduct);
    }

    @Test
    @DisplayName("Get not existing product of bar")
    void getNotExistingProductOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> productQueryHandler.handle(new GetProduct(UUID.randomUUID(), bar.getId()))
        );
    }

    @Test
    @DisplayName("create product")
    void createProduct() throws EntityNotFoundException, InvalidAttributesException {
        ProductRequest request = new ProductRequest();
        request.name = "testName";
        request.brand = "testBrand";
        request.isFavorite = true;
        request.price = 2.5;
        request.size = 250d;
        request.categoryId = category.getId();
        request.productType = "DRINK";

        UUID id = service.createProduct(bar.getId(), request);

//        assertEquals(request.name, resProduct.getName());
//        assertEquals(request.brand, resProduct.getBrand());
//        assertEquals(request.isFavorite, resProduct.isFavorite());
//        assertEquals(request.price, resProduct.getPrice().amount().doubleValue());
//        assertEquals(request.size, resProduct.getSize());
    }

    @Test
    @DisplayName("update product")
    void updateProduct() throws EntityNotFoundException, InvalidAttributesException {
        ProductRequest request = new ProductRequest();
        request.name = "testNameNew";
        request.brand = "testBrandNew";
        request.isFavorite = true;
        request.price = 5d;
        request.size = 500d;
        request.categoryId = category.getId();

        UUID id = service.updateProductOfBar(bar.getId(), product.getId(), request);

//        assertEquals(request.name, resProduct.getName());
//        assertEquals(request.brand, resProduct.getBrand());
//        assertEquals(request.isFavorite, resProduct.isFavorite());
//        assertEquals(request.price, resProduct.getPrice().amount().doubleValue());
//        assertEquals(request.size, resProduct.getSize());
    }

    @Test
    @DisplayName("delete product")
    void deleteProduct() {
        assertDoesNotThrow(() -> service.deleteProductOfBar(bar.getId(), product.getId()));
    }

//    @Test
//    @DisplayName("search all product of bar")
//    void getAllProductsOfBar() throws EntityNotFoundException {
//        List<Product> resProducts = service.searchProductsOfBar(bar.getId(), null, null, null);
//
//        assertEquals(3, resProducts.size());
//        assertTrue(resProducts.contains(product));
//        assertTrue(resProducts.contains(product2));
//        assertTrue(resProducts.contains(product3));
//    }

//    private List<Object[]> searchArgs() {
//        List<Product> productsAll = List.of(product, product2, product3);
//        List<Product> productsOne = List.of(product);
//        List<Product> productsTwo = List.of(product2);
//        List<Product> productsThree = List.of(product3);
//        return List.of(
//                new Object[]{null, null, null, productsAll},
//                new Object[]{ProductType.FOOD.toString(), null, null, productsOne},
//                new Object[]{ProductType.DRINK.toString(), null, null, productsTwo},
//                new Object[]{ProductType.OTHER.toString(), null, null, productsThree},
//                new Object[]{null, category.getId(), null, productsOne},
//                new Object[]{null, category2.getId(), null, productsTwo},
//                new Object[]{null, category3.getId(), null, productsThree},
//                new Object[]{null, null, true, List.of()},
//                new Object[]{null, null, false, productsAll}
//        );
//    }
//    @Test
//    @DisplayName("search all product of bar")
//    void searchProductsOfBar() throws EntityNotFoundException {
//        List<Object[]> argList = searchArgs();
//        for (Object[] arguments : argList) {
//            List<Product> resProducts = service.searchProductsOfBar(
//                    bar.getId(),
//                    (String) arguments[0],
//                    (Long) arguments[1],
//                    (Boolean) arguments[2]
//            );
//
//            assertEquals(((List<Product>) arguments[3]).size(), resProducts.size());
//        }
//    }
}