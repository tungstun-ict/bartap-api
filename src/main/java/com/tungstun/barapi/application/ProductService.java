package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringProductRepository;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;

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

    private Product findProductInProducts(List<Product> products, Long productId) throws NotFoundException {
        return products.stream()
                .filter( product -> product.getId().equals(productId) )
                .findFirst()
                .orElseThrow( () -> new NotFoundException(String.format("No product found with id %s", productId)) );
    }

    private ProductType convertStringToProductType(String type) {
        ProductType productType = ProductType.OTHER;
        if (type != null) {
            try {
                productType = ProductType.valueOf(type.toUpperCase());
            }catch (Exception e) {
                throw new IllegalArgumentException(String.format("Invalid product type '%s'", type));
            }
        }
        return productType;
    }

    private Product saveProductForBar(Bar bar, Product product) {
        product = this.SPRING_PRODUCT_REPOSITORY.save(product);
        bar.addProduct(product);
        this.BAR_SERVICE.saveBar(bar);
        return product;
    }

    private void filterProductsByCategoryId(List<Product> products, Long categoryId) throws NotFoundException {
        products.removeIf( product -> product.getCategory() == null || !product.getCategory().getId().equals(categoryId) );
        if (products.isEmpty()) throw new NotFoundException(String.format("No products found of category with id %s", categoryId));
    }

    private void filterProductsByTypeString(List<Product> products, String type) throws NotFoundException {
        ProductType productType = convertStringToProductType(type);
        products.removeIf( product -> !product.getProductType().equals(productType) );
        if (products.isEmpty()) throw new NotFoundException(String.format("No products found of type %s", productType));
    }

    private void filterProductsOnFavorites(List<Product> products, Boolean onlyFavorites) throws NotFoundException {
        products.removeIf( product -> product.isFavorite() != onlyFavorites );
        if (products.isEmpty()) throw new NotFoundException("No favorite products found");
    }

    private List<Product> getAllProductsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getProducts();
    }

    private void validateCategory(Category category, String type) throws InvalidAttributesException {
        ProductType productType = convertStringToProductType(type);
        if (!category.getProductType().equals(productType))
            throw new InvalidAttributesException(String.format(
                    "Category is of different product type '%s', then given product type '%s'",
                    category.getProductType(),
                    productType)
            );
    }

    public List<Product> getProductsOfBar(Long barId, String type, Long categoryId, Boolean onlyFavorites) throws NotFoundException {
        List<Product> products = getAllProductsOfBar(barId);
        if (type != null) filterProductsByTypeString(products, type);
        if (categoryId != null) filterProductsByCategoryId(products, categoryId);
        if (onlyFavorites != null) filterProductsOnFavorites(products, onlyFavorites);
        if (products.isEmpty()) throw new NotFoundException(String.format("No products found for bar with id %s", barId));
        return products;
    }

    public Product getProductOfBar(Long barId, Long productId) throws NotFoundException {
        List<Product> allProducts = getAllProductsOfBar(barId);
        return findProductInProducts(allProducts, productId);
    }
    private Product buildProduct(Long barId, ProductRequest productRequest) throws NotFoundException, InvalidAttributesException {
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, productRequest.categoryId);
        validateCategory(category, productRequest.productType);
        ProductType productType = convertStringToProductType(productRequest.productType);
        return new ProductBuilder()
                .setName(productRequest.name)
                .setBrand(productRequest.brand)
                .setSize(productRequest.size)
                .setPrice(productRequest.price)
                .setFavorite(productRequest.isFavorite)
                .setCategory(category)
                .setProductType(productType)
                .build();
    }

    public Product addProductToBar(Long barId, ProductRequest productRequest) throws NotFoundException, InvalidAttributesException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        if (barHasProductWithName(bar, productRequest.name)) throw new DuplicateRequestException(String.format("Bar already has product with name '%s'", productRequest.name));
        Product product = buildProduct(barId, productRequest);
        return saveProductForBar(bar, product);
    }

    private boolean barHasProductWithName(Bar bar, String name) {
        for (Product product : bar.getProducts()) {
            if (product.getName().toLowerCase().equals(name.toLowerCase())) return true;
        }
        return false;
    }

    private boolean barHasProductWithNameAndIsntItself(Bar bar, Product product, String name) {
        for (Product productIteration : bar.getProducts()) {
            if (productIteration.getName().toLowerCase().equals(name.toLowerCase()) && !productIteration.equals(product)) return true;
        }
        return false;
    }

    public Product updateProductOfBar(Long barId, Long productId, ProductRequest productRequest) throws NotFoundException, InvalidAttributesException {
        Product product = getProductOfBar(barId, productId);
        Bar bar = this.BAR_SERVICE.getBar(barId);
        if (barHasProductWithNameAndIsntItself(bar, product, productRequest.name))
            throw new DuplicateRequestException(String.format("Bar already has product with name '%s'", productRequest.name));

        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, productRequest.categoryId);
        validateCategory(category, productRequest.productType);
        ProductType productType = convertStringToProductType(productRequest.productType);
        product.setName(productRequest.name);
        product.setBrand(productRequest.brand);
        product.setSize(productRequest.size);
        product.setPrice(productRequest.price);
        product.setFavorite(productRequest.isFavorite);
        product.setCategory(category);
        product.setProductType(productType);
        return this.SPRING_PRODUCT_REPOSITORY.save(product);
    }

    public void deleteProductOfBar(Long barId, Long productId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Product product = getProductOfBar(barId, productId);
        bar.removeProduct(product);
        this.BAR_SERVICE.saveBar(bar);
    }
}
