package com.tungstun.barapi.application.category;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.category.command.CreateCategory;
import com.tungstun.barapi.application.category.command.DeleteCategory;
import com.tungstun.barapi.application.category.command.UpdateCategory;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class CategoryCommandHandlerIntegrationTest {
    @Autowired
    private SpringCategoryRepository repository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringCategoryRepository categoryRepository;
    @Autowired
    private CategoryCommandHandler categoryCommandHandler;

    private Bar bar;
    private Category category;

    @BeforeEach
    void setup() {
        bar = new BarBuilder("test bar").build();
        category = repository.save(new Category(UUID.randomUUID(), "category"));
        bar = barRepository.save(bar);
        bar.addCategory(category);
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Add category to bar")
    void addCategory() {
        CreateCategory command = new CreateCategory(bar.getId(), "testCategory");

        assertDoesNotThrow(() -> categoryCommandHandler.handle(command));
    }

    @Test
    @DisplayName("add existing category name to bar")
    void addExistingCategoryName() {
        CreateCategory command = new CreateCategory(bar.getId(), "category");

        assertThrows(
                DuplicateRequestException.class,
                () -> categoryCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Update existing category name bar")
    void updateExistingCategory() throws EntityNotFoundException {
        UpdateCategory command = new UpdateCategory(bar.getId(), category.getId(), "categoryNew");

        UUID id = categoryCommandHandler.handle(command);

        assertEquals(command.name(), repository.findById(id).orElseThrow().getName());
    }

    @Test
    @DisplayName("Update existing category name with already existing category in bar")
    void updateExistingDuplicateCategory() {
        String sameName = "categoryNew";
        bar.createCategory(sameName);
        bar = barRepository.save(bar);
        UpdateCategory command = new UpdateCategory(bar.getId(), category.getId(), sameName);

        assertThrows(
                DuplicateRequestException.class,
                () -> categoryCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Delete existing category in bar")
    void deleteExistingCategory() {
        DeleteCategory command = new DeleteCategory(category.getId());

        assertDoesNotThrow(() -> categoryCommandHandler.handle(command));
    }

    @Test
    @DisplayName("Delete existing category in bar sets products of it to null category")
    void deleteExistingCategory_SetsProductsCategoriesOfItToNull() throws EntityNotFoundException {
        Product product = new ProductBuilder("name", category)
                .build();
        bar.addProduct(product);
        DeleteCategory command = new DeleteCategory(category.getId());

        categoryCommandHandler.handle(command);

        assertTrue(categoryRepository.findById(category.getId()).isEmpty());
    }
}
