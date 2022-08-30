package com.tungstun.barapi.application.category;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryRepository;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {
    private final BarQueryHandler barQueryHandler;
    private final CategoryQueryHandler categoryQueryHandler;
    private final CategoryRepository categoryRepository;
    private final BarRepository barRepository;

    public CategoryService(BarQueryHandler barQueryHandler, CategoryQueryHandler categoryQueryHandler, CategoryRepository categoryRepository, BarRepository barRepository) {
        this.barQueryHandler = barQueryHandler;
        this.categoryQueryHandler = categoryQueryHandler;
        this.categoryRepository = categoryRepository;
        this.barRepository = barRepository;
    }

    public UUID addCategoryToBar(UUID barId, CategoryRequest categoryRequest) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        Category category = bar.createCategory(categoryRequest.name);
        barRepository.save(bar);
        return category.getId();
    }

    public UUID updateCategoryOfBar(UUID barId, UUID categoryId, CategoryRequest categoryRequest) throws EntityNotFoundException {
        boolean exists = barQueryHandler.handle(new GetBar(barId))
                .getCategories()
                .stream()
                .anyMatch(categoryIteration -> categoryIteration.getName().equalsIgnoreCase(categoryRequest.name));
        if (exists) {
            throw new DuplicateRequestException("Bar already has category with name '%s'" + categoryRequest.name);
        }
        Category category = categoryQueryHandler.handle(new GetCategory(categoryId, barId));
        category.setName(categoryRequest.name);
        categoryRepository.save(category);
        return categoryId;
    }

    public void deleteCategoryFromBar(UUID barId, UUID categoryId) throws EntityNotFoundException {
        categoryRepository.delete(categoryId);
//        Bar bar = this.barService.getBar(barId);
//        Category category = findCategoryInCategories(bar.getCategories(), categoryId);
//        removeCategoryFromProducts(bar.getProducts(), category);
//        bar.removeCategory(category);
//        this.barService.saveBar(bar);
    }
}
