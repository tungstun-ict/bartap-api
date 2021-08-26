package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringCategoryRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CategoryService {
    private final SpringCategoryRepository springCategoryRepository;
    private final BarService barService;

    public CategoryService(SpringCategoryRepository springCategoryRepository, BarService barService) {
        this.springCategoryRepository = springCategoryRepository;
        this.barService = barService;
    }

    public List<Category> getCategoriesOfBar(Long barId, String productType) throws NotFoundException {
        return productType != null ?
                getCategoriesOfProductTypeOfBar(barId, productType)
                : getAllCategoriesOfBar(barId);
    }

    private List<Category> getCategoriesOfProductTypeOfBar(Long barId, String type) throws NotFoundException {
        ProductType productType = convertStringToProductType(type);
        List<Category> allCategories = getAllCategoriesOfBar(barId);
        return findCategoriesOfProductType(allCategories, productType);
    }

    private ProductType convertStringToProductType(String type) {
        if (type == null) throw new IllegalArgumentException(String.format("Invalid product type '%s'", type));
        return ProductType.valueOf(type.toUpperCase());
    }

    private List<Category> getAllCategoriesOfBar(Long barId) throws NotFoundException {
        Bar bar = this.barService.getBar(barId);
        return bar.getCategories();
    }

    private List<Category> findCategoriesOfProductType(List<Category> categories, ProductType productType) throws NotFoundException {
        return categories.stream()
                .filter(category -> category.getProductType().equals(productType))
                .collect(Collectors.toList());

    }

    public Category getCategoryOfBar(Long barId, Long categoryId) throws NotFoundException {
        List<Category> allCategories = getAllCategoriesOfBar(barId);
        return findCategoryInCategories(allCategories, categoryId);
    }

    private Category findCategoryInCategories(List<Category> categories, Long categoryId) throws NotFoundException {
        return categories.stream()
                .filter(category -> category.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No category found with id %s", categoryId)));
    }

    public Category addCategoryToBar(Long barId, CategoryRequest categoryRequest) throws NotFoundException {
        Bar bar = this.barService.getBar(barId);
        checkIfCategoryNameIsAvailable(bar, categoryRequest.name);
        ProductType productType = convertStringToProductType(categoryRequest.productType);
        Category category = new Category(categoryRequest.name, productType);
        return saveCategoryToBar(bar, category);
    }

    private void checkIfCategoryNameIsAvailable(Bar bar, String categoryName) {
        if (barHasCategoryWithName(bar, categoryName)) {
            throw new DuplicateRequestException(String.format("Bar already has category with name '%s'", categoryName));
        }
    }

    private boolean barHasCategoryWithName(Bar bar, String name) {
        return barHasCategoryWithNameAndIsNotItself(bar, null, name);

    }

    private Category saveCategoryToBar(Bar bar, Category category) {
        category = this.springCategoryRepository.save(category);
        bar.addCategory(category);
        this.barService.saveBar(bar);
        return category;
    }

    private boolean barHasCategoryWithNameAndIsNotItself(Bar bar, Category category, String name) {
        return bar.getCategories().stream()
                .anyMatch(categoryIteration -> categoryIteration.getName().equalsIgnoreCase(name)
                        && (category == null || !categoryIteration.getId().equals(category.getId()))
        );
    }

    public Category updateCategoryOfBar(Long barId, Long categoryId, CategoryRequest categoryRequest) throws NotFoundException {
        Bar bar = this.barService.getBar(barId);
        Category category = getCategoryOfBar(barId, categoryId);
        checkIfCategoryNameIsOccupied(bar, category, categoryRequest.name);
        ProductType productType = convertStringToProductType(categoryRequest.productType);
        category.setName(categoryRequest.name);
        category.setProductType(productType);
        return this.springCategoryRepository.save(category);
    }

    private void checkIfCategoryNameIsOccupied(Bar bar, Category category, String categoryName) {
        if (barHasCategoryWithNameAndIsNotItself(bar, category, categoryName))
            throw new DuplicateRequestException(String.format("Bar already has category with name '%s'", categoryName));
    }

    public void deleteCategoryFromBar(Long barId, Long categoryId) throws NotFoundException {
        Bar bar = this.barService.getBar(barId);
        Category category = findCategoryInCategories(bar.getCategories(), categoryId);
        removeCategoryFromProducts(bar.getProducts(), category);
        bar.removeCategory(category);
        this.barService.saveBar(bar);
    }

    private void removeCategoryFromProducts(List<Product> products, Category category) {
        products.stream()
                .filter(product -> product.getCategory() != null && product.getCategory().equals(category))
                .forEach(product -> product.setCategory(null));
    }
}
