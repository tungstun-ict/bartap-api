package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringCategoryRepository;
import com.tungstun.barapi.domain.Bar;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

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

    public List<Category> getAllCategoriesOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getCategories();
    }

    public Category getCategoryOfBar(Long barId, Long categoryId) throws NotFoundException {
        List<Category> allCategories = getAllCategoriesOfBar(barId);
        return findCategoryInCategories(allCategories, categoryId);
    }

    public Category addCategoryToBar(Long barId, CategoryRequest categoryRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Category category = new Category(categoryRequest.name);
        category = this.SPRING_CATEGORY_REPOSITORY.save(category);
        bar.addCategory(category);
        this.BAR_SERVICE.saveBar(bar);
        return category;
    }

    public Category updateCategoryOfBar(Long barId, Long categoryId, CategoryRequest categoryRequest) throws NotFoundException {
        Category category = getCategoryOfBar(barId, categoryId);
        category.setName(categoryRequest.name);
        return this.SPRING_CATEGORY_REPOSITORY.save(category);
    }

    public void deleteCategoryFromBar(Long barId, Long categoryId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Category category = findCategoryInCategories(bar.getCategories(), categoryId);
        bar.removeCategory(category);
        this.BAR_SERVICE.saveBar(bar);
    }
}