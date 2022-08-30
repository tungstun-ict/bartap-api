package com.tungstun.barapi.application.category;

import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.category.query.ListCategoriesOfBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class CategoryQueryHandlerIntegrationTest {
    @Autowired
    private SpringCategoryRepository repository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private CategoryQueryHandler categoryQueryHandler;

    private Bar bar;
    private Category category;

    private static Stream<Arguments> provideBarsWithCategories() {
        List<Category> bar2Categories = List.of(
                new Category(UUID.randomUUID(), "category")
        );
        List<Category> bar3Categories = List.of(
                new Category(UUID.randomUUID(), "category"),
                new Category(UUID.randomUUID(), "category2")
        );
        List<Category> bar4Categories = List.of(
                new Category(UUID.randomUUID(), "category"),
                new Category(UUID.randomUUID(), "category2"),
                new Category(UUID.randomUUID(), "category3")
        );

        return Stream.of(
                Arguments.of(new BarBuilder("bar").build()),
                Arguments.of(new BarBuilder("bar2").setCategories(bar2Categories).build()),
                Arguments.of(new BarBuilder("bar3").setCategories(bar3Categories).build()),
                Arguments.of(new BarBuilder("bar4").setCategories(bar4Categories).build())
        );
    }

    @BeforeEach
    void setup() {
        bar = new BarBuilder("test bar").build();
        category = repository.save(new Category(UUID.randomUUID(), "category"));
        bar = barRepository.save(bar);
        bar.addCategory(category);
        bar = barRepository.save(bar);
    }

    @ParameterizedTest
    @MethodSource("provideBarsWithCategories")
    @DisplayName("Get all categories of bar")
    void getCategoriesOfBar_ReturnsCategories(Bar bar) throws EntityNotFoundException {
        bar = barRepository.save(bar);
        List<Category> expectedCategories = bar.getCategories();

        List<Category> categories = categoryQueryHandler.handle(new ListCategoriesOfBar(bar.getId()));

        assertEquals(categories.size(), expectedCategories.size());
    }

    @Test
    @DisplayName("Get existing category of bar")
    void getCategoryOfBar() throws EntityNotFoundException {
        Category actualCategory = categoryQueryHandler.handle(new GetCategory(category.getId(), bar.getId()));

        assertNotNull(actualCategory);
        assertEquals(category.getName(), actualCategory.getName());
    }

    @Test
    @DisplayName("Get not existing category of bar")
    void getNotExistingCategoryOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> categoryQueryHandler.handle(new GetCategory(bar.getId(), UUID.randomUUID()))
        );
    }
}
