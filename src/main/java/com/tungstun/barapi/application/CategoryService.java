package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringCategoryRepository;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final SpringCategoryRepository SPRING_CATEGORY_REPOSITORY;
    private final BarService BAR_SERVICE;

    public CategoryService(SpringCategoryRepository springCategoryRepository, BarService barService) {
        this.SPRING_CATEGORY_REPOSITORY = springCategoryRepository;
        this.BAR_SERVICE = barService;
    }

    private Category findCategoryInCategories(List<Category> categories, Long categoryId) throws NotFoundException {
        for (Category category : categories) {
            if (category.getId().equals(categoryId)) {
                return category;
            }
        }
        throw new NotFoundException(String.format("No category found with id %s", categoryId));
    }

    private List<Category> findCategoriesOfProductType(List<Category> categories, ProductType productType) throws NotFoundException {
        List<Category> resCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category.getProductType().equals(productType)) {
                resCategories.add(category);
            }
        }
        if (resCategories.isEmpty()) throw new NotFoundException(String.format("No categories found with product type %s", productType));
        return resCategories;
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

    private List<Category> getCategoriesOfProductTypeOfBar(Long barId, String type) throws NotFoundException {
        List<Category> allCategories = getAllCategoriesOfBar(barId);
        ProductType productType = convertStringToProductType(type);
        return findCategoriesOfProductType(allCategories, productType);
    }

    private List<Category> getAllCategoriesOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getCategories();
    }

    public List<Category> getCategoriesOfBar(Long barId, String productType) throws NotFoundException {
        if (productType != null) {
            return getCategoriesOfProductTypeOfBar(barId, productType);
        }else{
            return getAllCategoriesOfBar(barId);
        }
    }

    public Category getCategoryOfBar(Long barId, Long categoryId) throws NotFoundException {
        List<Category> allCategories = getAllCategoriesOfBar(barId);
        return findCategoryInCategories(allCategories, categoryId);
    }

    public Category addCategoryToBar(Long barId, CategoryRequest categoryRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        if (barHasCategoryWithName(bar, categoryRequest.name))
            throw new DuplicateRequestException(String.format("Bar already has category with name '%s'", categoryRequest.name));
        ProductType productType = convertStringToProductType(categoryRequest.productType);
        Category category = new Category(categoryRequest.name, productType);
        category = this.SPRING_CATEGORY_REPOSITORY.save(category);
        bar.addCategory(category);
        this.BAR_SERVICE.saveBar(bar);
        return category;
    }

    private boolean barHasCategoryWithName(Bar bar, String name) {
        for (Category categoryIteration : bar.getCategories()) {
            if (categoryIteration.getName().toLowerCase().equals(name.toLowerCase())) return true;
        }
        return false;
    }

    private boolean barHasCategoryWithNameAndIsNotItself(Bar bar, Category category, String name) {
        for (Category categoryIteration : bar.getCategories()) {
            if (categoryIteration.getName().toLowerCase().equals(name.toLowerCase())
                    && !categoryIteration.equals(category)) {
                return true;
            }
        }
        return false;
    }


    public Category updateCategoryOfBar(Long barId, Long categoryId, CategoryRequest categoryRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Category category = getCategoryOfBar(barId, categoryId);
        if (barHasCategoryWithNameAndIsNotItself(bar, category, categoryRequest.name))
            throw new DuplicateRequestException(String.format("Bar already has category with name '%s'", categoryRequest.name));
        ProductType productType = convertStringToProductType(categoryRequest.productType);
        category.setName(categoryRequest.name);
        category.setProductType(productType);
        return this.SPRING_CATEGORY_REPOSITORY.save(category);
    }

    public void deleteCategoryFromBar(Long barId, Long categoryId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Category category = findCategoryInCategories(bar.getCategories(), categoryId);
        for (Product product : bar.getProducts()) {
            if (product.getCategory().equals(category)) {
                product.setCategory(null);
            }
        }
        bar.removeCategory(category);
        this.BAR_SERVICE.saveBar(bar);
    }
}
