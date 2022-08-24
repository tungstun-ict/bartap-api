package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.category.CategoryQueryHandler;
import com.tungstun.barapi.application.category.CategoryService;
import com.tungstun.barapi.application.category.query.GetCategory;
import com.tungstun.barapi.application.category.query.ListCategoriesOfBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
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
public class CategoryServiceIntegrationTest {
    private static Bar bar;
    private static Category category;
    @Autowired
    private SpringCategoryRepository repository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private CategoryService service;
    @Autowired
    private CategoryQueryHandler categoryQueryHandler;

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
//    private static Stream<Arguments> provideBarsWithCategoriesAndType() {
//        Category drink = new Category(UUID.randomUUID(), "category");
//        Category food = new Category(UUID.randomUUID(), "category");
//        Category other = new Category(UUID.randomUUID(), "category");
//
//        List<Category> barCategories = new ArrayList<>(List.of(drink, food, other));
//        Bar bar = new BarBuilder("bar").setCategories(barCategories).build();
//        return Stream.of(
//                Arguments.of(bar, new ArrayList<>(List.of(food)), "FOOD"),
//                Arguments.of(bar, new ArrayList<>(List.of(drink)), "DRINK"),
//                Arguments.of(bar, new ArrayList<>(List.of(other)), "OTHER")
//        );
//    }
//    @ParameterizedTest
//    @MethodSource("provideBarsWithCategoriesAndType")
//    @DisplayName("Get all categories of bar of type")
//    void getCategoriesOfBarOfType_ReturnsCategories(Bar bar, List<Category> expectedCategories, String type) throws EntityNotFoundException {
//        bar = barRepository.save(bar);
//
//        List<Category> categories = service.getCategoriesOfBar(bar.getId(), type);
//
//        assertEquals(categories.size(), expectedCategories.size());
//    }
//    @Test
//    @DisplayName("Get category with not existing product type")
//    void getNotExistingProductTypeCategoryOfBar() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> service.getCategoriesOfBar(123L, "notExistingType")
//        );
//    }

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

    @Test
    @DisplayName("Add category to bar")
    void addCategory() {
        CategoryRequest request = new CategoryRequest();
        request.name = "testCategory";

        assertDoesNotThrow(() -> service.addCategoryToBar(bar.getId(), request));
    }

    @Test
    @DisplayName("add existing category name to bar")
    void addExistingCategoryName() {
        CategoryRequest request = new CategoryRequest();
        request.name = "category";

        assertThrows(
                DuplicateRequestException.class,
                () -> service.addCategoryToBar(bar.getId(), request)
        );
    }

    @Test
    @DisplayName("Update existing category name bar")
    void updateExistingCategory() throws EntityNotFoundException {
        CategoryRequest request = new CategoryRequest();
        request.name = "categoryNew";

        UUID id = service.updateCategoryOfBar(bar.getId(), category.getId(), request);

//        assertEquals("categoryNew", updatedCategory.getName());
    }

    @Test
    @DisplayName("Update existing category name with already existing category in bar")
    void updateExistingDuplicateCategory() {
        String sameName = "categoryNew";

        Category category2 = new Category(UUID.randomUUID(), sameName);
        bar.addCategory(category2);
        bar = barRepository.save(bar);

        CategoryRequest request = new CategoryRequest();
        request.name = sameName;

        assertThrows(
                DuplicateRequestException.class,
                () -> service.updateCategoryOfBar(bar.getId(), category.getId(), request)
        );
    }


    @Test
    @DisplayName("Delete existing category in bar")
    void deleteExistingCategory() {
        assertDoesNotThrow(() -> service.deleteCategoryFromBar(bar.getId(), category.getId()));
    }

    @Test
    @DisplayName("Delete existing category in bar sets products of it to null category")
    void deleteExistingCategory_SetsProductsCategoriesOfItToNull() throws EntityNotFoundException {
        Product product = new ProductBuilder("name", category)
                .build();
        bar.addProduct(product);

        service.deleteCategoryFromBar(bar.getId(), category.getId());

        Bar loadedBar = barRepository.findById(bar.getId())
                .orElseThrow();
        assertNull(loadedBar.getProducts().get(0).getCategory());
    }
}
