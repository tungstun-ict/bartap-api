package com.tungstun.barapi.application.product;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.application.product.query.ListProductsOfBar;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.domain.search.ProductSearchAlgorithm;
import com.tungstun.barapi.domain.search.ProductSearchEngine;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductQueryHandler {
    private final BarQueryHandler barQueryHandler;
    private final ProductSearchAlgorithm productSearchAlgorithm;

    public ProductQueryHandler(BarQueryHandler barQueryHandler, ProductSearchAlgorithm productSearchAlgorithm) {
        this.barQueryHandler = barQueryHandler;
        this.productSearchAlgorithm = productSearchAlgorithm;
    }

    public Product handle(GetProduct query) {
        return barQueryHandler.handle(new GetBar(query.barId()))
                .getProducts()
                .stream()
                .filter(product -> product.getId().equals(query.productId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No product found with id: " + query.productId()));
    }

    public List<Product> handle(ListProductsOfBar query) {
        List<Product> products = barQueryHandler.handle(new GetBar(query.barId()))
                .getProducts();

        ProductType type = null;
        if (query.productType() != null) {
            type = ProductType.getProductType(query.productType());
        }

        return new ProductSearchEngine(productSearchAlgorithm)
                .addCategoryIdFilter(query.categoryId())
                .addIsFavoriteFilter(query.isFavorite())
                .addProductTypeFilter(type)
                .addSearchText(query.searchText())
                .search(products);
    }
}
