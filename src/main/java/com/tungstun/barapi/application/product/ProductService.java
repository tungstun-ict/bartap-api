package com.tungstun.barapi.application.product;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.category.CategoryQueryHandler;
import com.tungstun.barapi.application.category.query.GetCategory;
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
public class ProductService {
    private final ProductQueryHandler productQueryHandler;
    private final SpringProductRepository productRepository;
    private final BarQueryHandler barQueryHandler;
    private final BarRepository barRepository;
    private final CategoryQueryHandler categoryQueryHandler;

    public ProductService(ProductQueryHandler productQueryHandler, SpringProductRepository springProductRepository, BarQueryHandler barQueryHandler, BarRepository barRepository, CategoryQueryHandler categoryQueryHandler) {
        this.productQueryHandler = productQueryHandler;
        this.productRepository = springProductRepository;
        this.barQueryHandler = barQueryHandler;
        this.barRepository = barRepository;
        this.categoryQueryHandler = categoryQueryHandler;
    }
//
//    private final BiPredicate<Product, ProductType> isProductType = (product, productType) -> product.getCategory().getProductType().equals(productType);
//
//    private Product saveProductForBar(Bar bar, Product product) {
//        product = this.productRepository.save(product);
//        bar.addProduct(product);
//        this.barService.saveBar(bar);
//        return product;
//    }
//
//    private final BiPredicate<Product, Long> hasCategoryId = (product, categoryId) -> product.getCategory() != null && product.getCategory().getId().equals(categoryId);
//    private final BiPredicate<Product, Boolean> isFavorite = (product, onlyFavorites) -> product.isFavorite() == onlyFavorites;
//
//    private Product findProductInProducts(List<Product> products, Long productId) throws EntityNotFoundException {
//        return products.stream()
//                .filter(product -> product.getId().equals(productId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException(String.format("No product found with categoryId %s", productId)));
//    }
//
//    public List<Product> searchProductsOfBar(Long barId, String type, Long categoryId, Boolean onlyFavorites) throws EntityNotFoundException {
//        Stream<Product> products = getAllProductsOfBar(barId).stream();
//        if (type != null) {
//            ProductType productType = convertStringToProductType(type);
//            products = products.filter(p -> isProductType.test(p, productType));
//        }
//        if (categoryId != null) products = products.filter(p -> hasCategoryId.test(p, categoryId));
//        if (onlyFavorites != null) products = products.filter(p -> isFavorite.test(p, onlyFavorites));
//        return products.collect(Collectors.toList());
//    }
//
//    private List<Product> getAllProductsOfBar(Long barId) throws EntityNotFoundException {
//        return productRepository.findAllByBarId(barId);
//    }
//
//    private ProductType convertStringToProductType(String type) {
//        if (type == null) throw new IllegalArgumentException(String.format("Invalid product type '%s'", type));
//        return ProductType.valueOf(type.toUpperCase());
//    }
//
////    public Product getProductOfBar(Long barId, Long productId) throws EntityNotFoundException {
//        List<Product> allProducts = getAllProductsOfBar(barId);
//        return findProductInProducts(allProducts, productId);
//    }

    public UUID createProduct(UUID barId, ProductRequest productRequest) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        Category category = categoryQueryHandler.handle(new GetCategory(productRequest.categoryId, barId));
        Product product = new ProductBuilder(productRequest.name, category)
                .setBrand(productRequest.brand)
                .setSize(productRequest.size)
                .setPrice(productRequest.price)
                .setType(ProductType.getProductType(productRequest.productType))
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
