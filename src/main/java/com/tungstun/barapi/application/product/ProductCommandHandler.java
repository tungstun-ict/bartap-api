package com.tungstun.barapi.application.product;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.category.CategoryQueryHandler;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.product.command.CreateProduct;
import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.port.persistence.product.SpringProductRepository;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import com.tungstun.common.money.Money;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class ProductCommandHandler {
    private final ProductQueryHandler productQueryHandler;
    private final SpringProductRepository productRepository;
    private final BarQueryHandler barQueryHandler;
    private final BarRepository barRepository;
    private final CategoryQueryHandler categoryQueryHandler;

    public ProductCommandHandler(ProductQueryHandler productQueryHandler, SpringProductRepository springProductRepository, BarQueryHandler barQueryHandler, BarRepository barRepository, CategoryQueryHandler categoryQueryHandler) {
        this.productQueryHandler = productQueryHandler;
        this.productRepository = springProductRepository;
        this.barQueryHandler = barQueryHandler;
        this.barRepository = barRepository;
        this.categoryQueryHandler = categoryQueryHandler;
    }

    public UUID createProduct(CreateProduct command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        Category category = categoryQueryHandler.handle(new GetCategory(command.categoryId(), command.barId()));
        Product product = new ProductBuilder(command.name(), category)
                .setBrand(command.brand())
                .setSize(command.size())
                .setPrice(command.price())
                .setFavorite(command.isFavorite())
                .setType(ProductType.getProductType(command.productType()))
                .build();
        bar.addProduct(product);
        barRepository.save(bar);
        return product.getId();
    }

    public UUID updateProductOfBar(UUID barId, UUID productId, ProductRequest productRequest) throws EntityNotFoundException {
        Product product = productQueryHandler.handle(new GetProduct(productId, barId))  ;
        Category category = categoryQueryHandler.handle(new GetCategory(productRequest.categoryId, barId));
        product.setCategory(category);
        product.setName(productRequest.name);
        product.setBrand(productRequest.brand);
        product.setSize(productRequest.size);
        product.updatePrice(new Money(productRequest.price));
        product.setFavorite(productRequest.isFavorite);
        return productRepository.save(product).getId();
    }

    public void deleteProductOfBar(UUID barId, UUID productId) throws EntityNotFoundException {
        productRepository.deleteById(productId);
//        Product product = getProductOfBar(barId, productId);
//        this.productRepository.delete(product);
    }
}
