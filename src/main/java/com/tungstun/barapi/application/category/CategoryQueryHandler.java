package com.tungstun.barapi.application.category;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.category.query.ListCategoriesOfBar;
import com.tungstun.barapi.domain.product.Category;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CategoryQueryHandler {
    private final BarQueryHandler barQueryHandler;

    public CategoryQueryHandler(BarQueryHandler barQueryHandler) {
        this.barQueryHandler = barQueryHandler;
    }

    public Category handle(GetCategory command) {
        return barQueryHandler.handle(new GetBar(command.barId()))
                .getCategories()
                .stream()
                .filter(category -> category.getId().equals(command.categoryId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No category found with productId: " + command.categoryId()));
    }

    public List<Category> handle(ListCategoriesOfBar command) {
        return barQueryHandler.handle(new GetBar(command.barId()))
                .getCategories();
    }
}
