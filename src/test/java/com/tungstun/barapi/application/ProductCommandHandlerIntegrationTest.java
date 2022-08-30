package com.tungstun.barapi.application;

import com.tungstun.barapi.application.product.ProductCommandHandler;
import com.tungstun.barapi.application.product.ProductQueryHandler;
import com.tungstun.barapi.application.product.command.CreateProduct;
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
import com.tungstun.barapi.port.persistence.product.SpringProductRepository;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
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
    private ProductCommandHandler service;
    @Autowired
    private ProductQueryHandler productQueryHandler;

    private Bar bar;
    private Category category;
    private Product product;
    private Product product2;
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
        product2 = new ProductBuilder("product2", category2)
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
        Product resProduct = productQueryHandler.handle(new GetProduct(product.getId(), bar.getId()));

        assertEquals(product.getId(), resProduct.getId());
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

        UUID id = service.createProduct(command);

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