package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.*;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    private static final BarService barService = mock(BarService.class);
    private static final CategoryRepository repository = mock(CategoryRepository.class);
    private static final CategoryService service = new CategoryService(repository, barService);

    private static Bar bar;
    private static Category category;

    @BeforeEach
    void setup() throws EntityNotFoundException {
        bar = new BarBuilder().build();
        category = new Category("category", ProductType.FOOD);
        ReflectionTestUtils.setField(category, "id", 123L);
        bar.addCategory(category);
        when(barService.getBar(123L)).thenReturn(bar);
    }

    private static Stream<Arguments> provideBarsWithCategories() {
        Bar bar = new BarBuilder().build();
        List<Category> bar2Categories = List.of(new Category("category", ProductType.DRINK));
        Bar bar2 = new Bar(null, null, null, null, bar2Categories);
        List<Category> bar3Categories = List.of(
                new Category("category", ProductType.DRINK),
                new Category("category", ProductType.FOOD)
        );
        Bar bar3 = new Bar(null, null, null, null, bar3Categories);
        List<Category> bar4Categories = List.of(
                new Category("category", ProductType.DRINK),
                new Category("category", ProductType.FOOD),
                new Category("category", ProductType.OTHER)
        );
        Bar bar4 = new Bar(null, null, null, null, bar4Categories);
        return Stream.of(
                Arguments.of(bar, List.of()),
                Arguments.of(bar2, bar2Categories),
                Arguments.of(bar3, bar3Categories),
                Arguments.of(bar4, bar4Categories)
        );
    }

    private static Stream<Arguments> provideBarsWithCategoriesAndType() {
        Category drink = new Category("category", ProductType.DRINK);
        Category food = new Category("category", ProductType.FOOD);
        Category other = new Category("category", ProductType.OTHER);

        List<Category> barCategories = List.of(drink, food, other);
        Bar bar = new Bar(null, null, null, null, barCategories);
        return Stream.of(
                Arguments.of(bar, List.of(food), "FOOD"),
                Arguments.of(bar, List.of(drink), "DRINK"),
                Arguments.of(bar, List.of(other), "OTHER")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBarsWithCategories")
    @DisplayName("Get all categories of bar")
    void getCategoriesOfBar_ReturnsCategories(Bar bar, List<Category> expectedCategories) throws EntityNotFoundException {
        when(barService.getBar(any()))
                .thenReturn(bar);

        List<Category> categories = service.getCategoriesOfBar(any(), null);

        assertEquals(expectedCategories, categories);
    }

    @ParameterizedTest
    @MethodSource("provideBarsWithCategoriesAndType")
    @DisplayName("Get all categories of bar of type")
    void getCategoriesOfBarOfType_ReturnsCategories(Bar bar, List<Category> expectedCategories, String type) throws EntityNotFoundException {
        when(barService.getBar(any()))
                .thenReturn(bar);
        List<Category> categories = service.getCategoriesOfBar(any(), type);
        assertEquals(expectedCategories, categories);
    }

    @Test
    @DisplayName("Get category with not existing product type")
    void getNotExistingProductTypeCategoryOfBar() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getCategoriesOfBar(123L, "notExistingType")
        );
    }

    @Test
    @DisplayName("Get existing category of bar")
    void getCategoryOfBar() throws EntityNotFoundException {
        Category c = service.getCategoryOfBar(123L, category.getId());

        assertNotNull(c);
    }

    @Test
    @DisplayName("Get not existing category of bar")
    void getNotExistingCategoryOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> service.getCategoryOfBar(123L, 999L)
        );
    }

    @Test
    @DisplayName("Add category to bar")
    void addCategory()  {
        CategoryRequest request = new CategoryRequest();
        request.name = "category2";
        request.productType = ProductType.FOOD.toString();

        assertDoesNotThrow(() -> service.addCategoryToBar(123L, request));
    }

    @Test
    @DisplayName("add existing category name to bar")
    void addExistingCategoryName() {
        CategoryRequest request = new CategoryRequest();
        request.name = "category";

        assertThrows(
                DuplicateRequestException.class,
                () -> service.addCategoryToBar(123L, request)
        );
    }

    @Test
    @DisplayName("Update existing category name bar")
    void updateExistingCategory() throws EntityNotFoundException {
        when(repository.save(any())).thenReturn(new Category("categoryNew", ProductType.FOOD));
        CategoryRequest request = new CategoryRequest();
        request.name = "categoryNew";
        request.productType = ProductType.FOOD.toString();

        Category updatedCategory = service.updateCategoryOfBar(123L, 123L, request);

        assertEquals("categoryNew", updatedCategory.getName());
    }

    @Test
    @DisplayName("Update existing category name with already existing category in bar")
    void updateExistingDuplicateCategory() {
        Category category2 = new Category("categoryNew", ProductType.FOOD);
        ReflectionTestUtils.setField(category2, "id", 321L);
        bar.addCategory(category2);
        when(repository.save(any())).thenReturn(new Category("categoryNew", ProductType.FOOD));
        CategoryRequest request = new CategoryRequest();
        request.name = "categoryNew";
        request.productType = ProductType.FOOD.toString();

        assertThrows(
                DuplicateRequestException.class,
                () -> service.updateCategoryOfBar(123L, 123L, request)
        );
    }

    @Test
    @DisplayName("Delete existing category in bar")
    void deleteExistingCategory(){
        assertDoesNotThrow(() -> service.deleteCategoryFromBar(123L, 123L));
    }

    @Test
    @DisplayName("Delete existing category in bar sets products of it to null category")
    void deleteExistingCategory_SetsProductsCategoriesOfItToNull() throws EntityNotFoundException {
        Product product = new ProductBuilder(123L, "", category).setPrice(1.0).build();
        bar.addProduct(product);

        service.deleteCategoryFromBar(123L, 123L);

        assertNull(bar.getProducts().get(0).getCategory());
    }
}