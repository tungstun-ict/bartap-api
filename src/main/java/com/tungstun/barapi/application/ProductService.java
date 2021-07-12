package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringProductRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class ProductService {
    private final SpringProductRepository SPRING_PRODUCT_REPOSITORY;
    private final BarService BAR_SERVICE;
    private final CategoryService CATEGORY_SERVICE;

    public ProductService(SpringProductRepository springProductRepository, BarService barService, CategoryService categoryService) {
        this.SPRING_PRODUCT_REPOSITORY = springProductRepository;
        this.BAR_SERVICE = barService;
        this.CATEGORY_SERVICE = categoryService;
    }

    private final BiPredicate<Product, ProductType> isProductType = (product, productType) -> product.getCategory().getProductType().equals(productType);

    private Product saveProductForBar(Bar bar, Product product) {
        product = this.SPRING_PRODUCT_REPOSITORY.save(product);
        bar.addProduct(product);
        this.BAR_SERVICE.saveBar(bar);
        return product;
    }

    private final BiPredicate<Product, Long> hasCategoryId = (product, categoryId) -> product.getCategory() != null && product.getCategory().getId().equals(categoryId);
    private final BiPredicate<Product, Boolean> isFavorite = (product, onlyFavorites) -> product.isFavorite() == onlyFavorites;

    private Product findProductInProducts(List<Product> products, Long productId) throws NotFoundException {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No product found with id %s", productId)));
    }

    public List<Product> searchProductsOfBar(Long barId, String type, Long categoryId, Boolean onlyFavorites) throws NotFoundException {
        Stream<Product> products = getAllProductsOfBar(barId).stream();
        if (type != null) {
            ProductType productType = convertStringToProductType(type);
            products = products.filter(p -> isProductType.test(p, productType));
        }
        if (categoryId != null) products = products.filter(p -> hasCategoryId.test(p, categoryId));
        if (onlyFavorites != null) products = products.filter(p -> isFavorite.test(p, onlyFavorites));
        return products.collect(Collectors.toList());
    }

    private List<Product> getAllProductsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getProducts();
    }

    private ProductType convertStringToProductType(String type) {
        if (type == null) throw new IllegalArgumentException(String.format("Invalid product type '%s'", type));
        return ProductType.valueOf(type.toUpperCase());
    }

    public Product getProductOfBar(Long barId, Long productId) throws NotFoundException {
        List<Product> allProducts = getAllProductsOfBar(barId);
        return findProductInProducts(allProducts, productId);
    }

    public Product addProductToBar(Long barId, ProductRequest productRequest) throws NotFoundException, InvalidAttributesException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Product product = buildProduct(barId, productRequest);
        return saveProductForBar(bar, product);
    }

    private Product buildProduct(Long barId, ProductRequest productRequest) throws NotFoundException, InvalidAttributesException {
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, productRequest.categoryId);
        return new ProductBuilder()
                .setName(productRequest.name)
                .setBrand(productRequest.brand)
                .setSize(productRequest.size)
                .setPrice(productRequest.price)
                .setFavorite(productRequest.isFavorite)
                .setCategory(category)
                .build();
    }

    public Product updateProductOfBar(Long barId, Long productId, ProductRequest productRequest) throws NotFoundException, InvalidAttributesException {
        Product product = getProductOfBar(barId, productId);
        Bar bar = this.BAR_SERVICE.getBar(barId);
        if (barHasProductWithNameAndIsNotItself(bar, product, productRequest.name))
            throw new DuplicateRequestException(String.format("Bar already has product with name '%s'", productRequest.name));
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, productRequest.categoryId);
        product.setName(productRequest.name);
        product.setBrand(productRequest.brand);
        product.setSize(productRequest.size);
        product.setPrice(productRequest.price);
        product.setFavorite(productRequest.isFavorite);
        product.setCategory(category);
        return this.SPRING_PRODUCT_REPOSITORY.save(product);
    }

    private boolean barHasProductWithNameAndIsNotItself(Bar bar, Product product, String name) {
        Predicate<Product> nameEquals = iterate -> iterate.getName().equalsIgnoreCase(name);
        Predicate<Product> productEquals = iterate -> !iterate.equals(product);
        return bar.getProducts()
                .stream()
                .anyMatch(nameEquals.and(productEquals));
    }

    public void deleteProductOfBar(Long barId, Long productId) throws NotFoundException {
        Product product = getProductOfBar(barId, productId);
        this.SPRING_PRODUCT_REPOSITORY.delete(product);
    }
}
