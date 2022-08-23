package com.tungstun.barapi.application;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.domain.search.engine.product.ProductSearchEngine;
import com.tungstun.barapi.port.persistence.product.SpringProductRepository;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import com.tungstun.common.money.Money;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class ProductService {
    private final SpringProductRepository productRepository;
    private final BarService barService;
    private final CategoryService categoryService;
    private final ProductSearchEngine searchEngine;

    public ProductService(SpringProductRepository springProductRepository, BarService barService, CategoryService categoryService, ProductSearchEngine searchEngine) {
        this.productRepository = springProductRepository;
        this.barService = barService;
        this.categoryService = categoryService;
        this.searchEngine = searchEngine;
    }

    private final BiPredicate<Product, ProductType> isProductType = (product, productType) -> product.getCategory().getProductType().equals(productType);

    private Product saveProductForBar(Bar bar, Product product) {
        product = this.productRepository.save(product);
        bar.addProduct(product);
        this.barService.saveBar(bar);
        return product;
    }

    private final BiPredicate<Product, Long> hasCategoryId = (product, categoryId) -> product.getCategory() != null && product.getCategory().getId().equals(categoryId);
    private final BiPredicate<Product, Boolean> isFavorite = (product, onlyFavorites) -> product.isFavorite() == onlyFavorites;

    private Product findProductInProducts(List<Product> products, Long productId) throws EntityNotFoundException {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("No product found with id %s", productId)));
    }

    public List<Product> searchProductsOfBar(Long barId, String type, Long categoryId, Boolean onlyFavorites) throws EntityNotFoundException {
        Stream<Product> products = getAllProductsOfBar(barId).stream();
        if (type != null) {
            ProductType productType = convertStringToProductType(type);
            products = products.filter(p -> isProductType.test(p, productType));
        }
        if (categoryId != null) products = products.filter(p -> hasCategoryId.test(p, categoryId));
        if (onlyFavorites != null) products = products.filter(p -> isFavorite.test(p, onlyFavorites));
        return products.collect(Collectors.toList());
    }

    private List<Product> getAllProductsOfBar(Long barId) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        return bar.getProducts();
    }

    private ProductType convertStringToProductType(String type) {
        if (type == null) throw new IllegalArgumentException(String.format("Invalid product type '%s'", type));
        return ProductType.valueOf(type.toUpperCase());
    }

    public Product getProductOfBar(Long barId, Long productId) throws EntityNotFoundException {
        List<Product> allProducts = getAllProductsOfBar(barId);
        return findProductInProducts(allProducts, productId);
    }

    public Product addProductToBar(Long barId, ProductRequest productRequest) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        Product product = buildProduct(barId, productRequest);
        return saveProductForBar(bar, product);
    }

    private Product buildProduct(Long barId, ProductRequest productRequest) throws EntityNotFoundException {
        Category category = this.categoryService.getCategoryOfBar(barId, productRequest.categoryId);
        return new ProductBuilder(123L, productRequest.name, category)
                .setBrand(productRequest.brand)
                .setSize(productRequest.size)
                .setPrice(productRequest.price)
                .setFavorite(productRequest.isFavorite)
                .build();
    }

    public Product updateProductOfBar(Long barId, Long productId, ProductRequest productRequest) throws EntityNotFoundException {
        Product product = getProductOfBar(barId, productId);
        Category category = this.categoryService.getCategoryOfBar(barId, productRequest.categoryId);
        product.setName(productRequest.name);
        product.setBrand(productRequest.brand);
        product.setSize(productRequest.size);
        product.updatePrice(new Money(productRequest.price));
        product.setFavorite(productRequest.isFavorite);
        product.setCategory(category);
        return this.productRepository.save(product);
    }

    public void deleteProductOfBar(Long barId, Long productId) throws EntityNotFoundException {
        Product product = getProductOfBar(barId, productId);
        this.productRepository.delete(product);
    }

    public List<Product> searchProduct(Long barId, String searchString) throws EntityNotFoundException {
        List<Product> allProducts = getAllProductsOfBar(barId);
        return searchEngine.search(allProducts, searchString);
    }
}
