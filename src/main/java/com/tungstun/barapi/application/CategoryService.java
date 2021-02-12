package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringCategoryRepository;
import com.tungstun.barapi.domain.Bar;
import com.tungstun.barapi.domain.Category;
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
}
