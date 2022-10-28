package com.tungstun.barapi.application.product;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.category.CategoryQueryHandler;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.product.command.CreateProduct;
import com.tungstun.barapi.application.product.command.DeleteProduct;
import com.tungstun.barapi.application.product.command.UpdateProduct;
import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.port.persistence.product.SpringProductRepository;
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

    public UUID handle(CreateProduct command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        Category category = categoryQueryHandler.handle(new GetCategory(command.barId(), command.categoryId()));
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

    public UUID handle(UpdateProduct command) throws EntityNotFoundException {
        Product product = productQueryHandler.handle(new GetProduct(command.barId(), command.productId()))  ;
        Category category = categoryQueryHandler.handle(new GetCategory(command.barId(), command.categoryId()));
        product.setCategory(category);
        product.setName(command.name());
        product.setBrand(command.brand());
        product.setSize(command.size());
        product.setFavorite(command.isFavorite());
        product.updatePrice(new Money(command.price()));
        product.setType(ProductType.getProductType(command.productType()));
        return productRepository.save(product).getId();
    }

    public void handle(DeleteProduct command) throws EntityNotFoundException {
        productRepository.deleteById(command.productId());
    }
}
