package com.tungstun.barapi.application;

import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.stock.Stock;
import com.tungstun.barapi.presentation.dto.request.StockRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Transactional
@Service
public class StockService {
    private final ProductService productService;

    public StockService(ProductService productService) {
        this.productService = productService;
    }

    public Stock getStock(Long barId, Long productId) throws EntityNotFoundException {
        Product product = productService.getProductOfBar(barId, productId);
        return new Stock(); //todo remove??
//        return product.getStock();
    }

    public Stock increaseStock(Long barId, Long productId, StockRequest request) throws EntityNotFoundException {
        Stock stock = getStock(barId, productId);
        stock.increaseAmount(request.amount);
        return stock;
    }

    public Stock decreaseStock(Long barId, Long productId, StockRequest request) throws EntityNotFoundException {
        Stock stock = getStock(barId, productId);
        stock.decreaseAmount(request.amount);
        return stock;
    }

    public Stock updateStockAmount(Long barId, Long productId, StockRequest request) throws EntityNotFoundException {
        Stock stock = getStock(barId, productId);
        stock.setAmount(request.amount);
        return stock;
    }
}
