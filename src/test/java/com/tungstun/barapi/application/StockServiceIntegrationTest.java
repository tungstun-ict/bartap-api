package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.domain.stock.Stock;
import com.tungstun.barapi.presentation.dto.request.StockRequest;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class StockServiceIntegrationTest{
    @Autowired
    private StockService stockService;
    @Autowired
    private SpringBarRepository barRepository;

    private Long barId;
    private Long productId;

    @BeforeEach
    void setUp() {
        Bar bar = new BarBuilder().build();
        Category category = new Category("Drinks", ProductType.DRINK);
        bar.addCategory(category);
        Product product = new ProductBuilder()
                .setName("product")
                .setPrice(1.0)
                .setSize(100)
                .setCategory(category)
                .build();
        bar.addProduct(product);
        bar = barRepository.save(bar);
        barId = bar.getId();
        productId = bar.getProducts().get(0).getId();

    }

    @Test
    @DisplayName("Increase stock")
    void increaseStockValid() throws NotFoundException {
        StockRequest request = new StockRequest();
        request.amount = 1;

        Stock stock = stockService.increaseStock(barId, productId, request);

        assertEquals(request.amount, stock.getAmount());
    }

    @Test
    @DisplayName("Increase stock invalid")
    void increaseStockInvalid()   {
        StockRequest request = new StockRequest();
        request.amount = -1;

        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.increaseStock(barId, productId, request)
        );
    }

    @Test
    @DisplayName("Decrease stock")
    void decreaseStockValid() throws NotFoundException {
        StockRequest request = new StockRequest();
        request.amount = 1;

        Stock stock = stockService.decreaseStock(barId, productId, request);

        assertEquals(request.amount, stock.getAmount());
    }

    @Test
    @DisplayName("Decrease stock invalid")
    void decreaseStockInvalid()   {
        StockRequest request = new StockRequest();
        request.amount = -1;

        assertThrows(
                IllegalArgumentException.class,
                () -> stockService.decreaseStock(barId, productId, request)
        );
    }

    @Test
    @DisplayName("Update stock")
    void updateStockValid() throws NotFoundException {
        StockRequest request = new StockRequest();
        request.amount = 1;

        Stock stock = stockService.updateStockAmount(barId, productId, request);

        assertEquals(request.amount, stock.getAmount());
    }
}