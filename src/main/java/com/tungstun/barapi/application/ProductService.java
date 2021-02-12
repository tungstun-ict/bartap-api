package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringProductRepository;
import com.tungstun.barapi.domain.Bar;
import com.tungstun.barapi.domain.Product;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final SpringProductRepository SPRING_PRODUCT_REPOSITORY;
    private final BarService BAR_SERVICE;

    public ProductService(SpringProductRepository springProductRepository, BarService barService) {
        this.SPRING_PRODUCT_REPOSITORY = springProductRepository;
        this.BAR_SERVICE = barService;
    }

    public List<Product> getAllProductsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        List<Product> products = bar.getProducts();
        if (products.isEmpty()) throw new NotFoundException(String.format("No products found for bar with id %s", barId));
        return products;
    }
}
