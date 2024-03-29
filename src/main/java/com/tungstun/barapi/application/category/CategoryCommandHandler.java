package com.tungstun.barapi.application.category;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.category.command.CreateCategory;
import com.tungstun.barapi.application.category.command.DeleteCategory;
import com.tungstun.barapi.application.category.command.UpdateCategory;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class CategoryCommandHandler {
    private final BarQueryHandler barQueryHandler;
    private final CategoryQueryHandler categoryQueryHandler;
    private final CategoryRepository categoryRepository;
    private final BarRepository barRepository;

    public CategoryCommandHandler(BarQueryHandler barQueryHandler, CategoryQueryHandler categoryQueryHandler, CategoryRepository categoryRepository, BarRepository barRepository) {
        this.barQueryHandler = barQueryHandler;
        this.categoryQueryHandler = categoryQueryHandler;
        this.categoryRepository = categoryRepository;
        this.barRepository = barRepository;
    }

    public UUID handle(CreateCategory command) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(command.barId()));
        Category category = bar.createCategory(command.name());
        barRepository.save(bar);
        return category.getId();
    }

    public UUID handle(UpdateCategory command) throws EntityNotFoundException {
        boolean exists = barQueryHandler.handle(new GetBar(command.barId()))
                .getCategories()
                .stream()
                .anyMatch(categoryIteration -> categoryIteration.getName().equalsIgnoreCase(command.name()));
        if (exists) {
            throw new DuplicateRequestException("Bar already has category with name '%s'" + command.name());
        }

        Category category = categoryQueryHandler.handle(new GetCategory(command.barId(), command.categoryId()));
        category.setName(command.name());
        categoryRepository.save(category);
        return command.categoryId();
    }

    public void handle(DeleteCategory command) throws EntityNotFoundException {
        categoryRepository.delete(command.categoryId());
    }
}
