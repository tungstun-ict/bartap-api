package com.tungstun.barapi.application.category;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryCommandHandlerTest {
    private static final BarRepository barRepository = mock(BarRepository.class);
    private static final BarQueryHandler barQueryHandler = mock(BarQueryHandler.class);
    private static final CategoryRepository repository = mock(CategoryRepository.class);
    private static final CategoryQueryHandler categoryQueryHandler = mock(CategoryQueryHandler.class);
    private static final CategoryCommandHandler service = new CategoryCommandHandler(barQueryHandler, categoryQueryHandler, repository, barRepository);

    private static Bar bar;
    private static Category category;

    private static Stream<Arguments> provideBarsWithCategories() {
        Bar bar = new BarBuilder("bar")
                .build();

        List<Category> bar2Categories = new ArrayList<>(List.of(new CategoryFactory("category").create()));
        Bar bar2 = new BarBuilder("bar2")
                .setCategories(bar2Categories)
                .build();

        List<Category> bar3Categories = new ArrayList<>(List.of(
                new CategoryFactory("category").create(),
                new CategoryFactory("category").create()
        ));
        Bar bar3 = new BarBuilder("bar3")
                .setCategories(bar3Categories)
                .build();


        List<Category> bar4Categories = new ArrayList<>(List.of(
                new CategoryFactory("category").create(),
                new CategoryFactory("category").create(),
                new CategoryFactory("category").create()
        ));
        Bar bar4 = new BarBuilder("bar4")
                .setCategories(bar4Categories)
                .build();

        return Stream.of(
                Arguments.of(bar, List.of()),
                Arguments.of(bar2, bar2Categories),
                Arguments.of(bar3, bar3Categories),
                Arguments.of(bar4, bar4Categories)
        );
    }

    private static Stream<Arguments> provideBarsWithCategoriesAndType() {
        Category drink = new CategoryFactory("category").create();
        Category food = new CategoryFactory("category").create();
        Category other = new CategoryFactory("category").create();

        List<Category> barCategories = List.of(drink, food, other);
        Bar bar = new BarBuilder("bar")
                .setCategories(barCategories)
                .build();

        return Stream.of(
                Arguments.of(bar, List.of(food), "FOOD"),
                Arguments.of(bar, List.of(drink), "DRINK"),
                Arguments.of(bar, List.of(other), "OTHER")
        );
    }

    @BeforeEach
    void setup() throws EntityNotFoundException {
        bar = new BarBuilder("bar").build();
        category = bar.createCategory("category");
//        category = new CategoryFactory("category").create();
//        ReflectionTestUtils.setField(category, "id", 123L);
//        bar.addCategory(category);
        when(barQueryHandler.handle(any(GetBar.class))).thenReturn(bar);
    }

//    @ParameterizedTest
//    @MethodSource("provideBarsWithCategories")
//    @DisplayName("Get all categories of bar")
//    void getCategoriesOfBar_ReturnsCategories(Bar bar, List<Category> expectedCategories) throws EntityNotFoundException {
//        when(barQueryHandler.handle(any(GetBar.class)))
//                .thenReturn(bar);
//
//        List<Category> categories = service.getCategoriesOfBar(any(), null);
//
//        assertEquals(expectedCategories, categories);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideBarsWithCategoriesAndType")
//    @DisplayName("Get all categories of bar of type")
//    void getCategoriesOfBarOfType_ReturnsCategories(Bar bar, List<Category> expectedCategories, String type) throws EntityNotFoundException {
//        when(barQueryHandler.handle(any(GetBar.class)))
//                .thenReturn(bar);
//        List<Category> categories = service.getCategoriesOfBar(any(), type);
//        assertEquals(expectedCategories, categories);
//    }
//
//    @Test
//    @DisplayName("Get category with not existing product type")
//    void getNotExistingProductTypeCategoryOfBar() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> service.getCategoriesOfBar(123L, "notExistingType")
//        );
//    }
//
//    @Test
//    @DisplayName("Get existing category of bar")
//    void getCategoryOfBar() throws EntityNotFoundException {
//        Category c = service.getCategoryOfBar(123L, category.getId());
//
//        assertNotNull(c);
//    }
//
//    @Test
//    @DisplayName("Get not existing category of bar")
//    void getNotExistingCategoryOfBar() {
//        assertThrows(
//                EntityNotFoundException.class,
//                () -> service.getCategoryOfBar(123L, 999L)
//        );
//    }
//
//    @Test
//    @DisplayName("Add category to bar")
//    void addCategory() {
//        CategoryRequest request = new CategoryRequest();
//        request.name = "category2";
//        request.productType = ProductType.FOOD.toString();
//
//        assertDoesNotThrow(() -> service.addCategoryToBar(123L, request));
//    }
//
//    @Test
//    @DisplayName("add existing category name to bar")
//    void addExistingCategoryName() {
//        CategoryRequest request = new CategoryRequest();
//        request.name = "category";
//
//        assertThrows(
//                DuplicateRequestException.class,
//                () -> service.addCategoryToBar(123L, request)
//        );
//    }
//
//    @Test
//    @DisplayName("Update existing category name bar")
//    void updateExistingCategory() throws EntityNotFoundException {
//        when(repository.save(any())).thenReturn(new Category(123L, "categoryNew", ProductType.FOOD));
//        CategoryRequest request = new CategoryRequest();
//        request.name = "categoryNew";
//        request.productType = ProductType.FOOD.toString();
//
//        Category updatedCategory = service.updateCategoryOfBar(123L, 123L, request);
//
//        assertEquals("categoryNew", updatedCategory.getName());
//    }
//
//    @Test
//    @DisplayName("Update existing category name with already existing category in bar")
//    void updateExistingDuplicateCategory() {
//        Category category2 = new Category(123L, "categoryNew", ProductType.FOOD);
//        ReflectionTestUtils.setField(category2, "id", 321L);
//        bar.addCategory(category2);
//        when(repository.save(any())).thenReturn(new Category(123L, "categoryNew", ProductType.FOOD));
//        CategoryRequest request = new CategoryRequest();
//        request.name = "categoryNew";
//        request.productType = ProductType.FOOD.toString();
//
//        assertThrows(
//                DuplicateRequestException.class,
//                () -> service.updateCategoryOfBar(123L, 123L, request)
//        );
//    }
//
//    @Test
//    @DisplayName("Delete existing category in bar")
//    void deleteExistingCategory() {
//        assertDoesNotThrow(() -> service.deleteCategoryFromBar(123L, 123L));
//    }
//
//    @Test
//    @DisplayName("Delete existing category in bar sets products of it to null category")
//    void deleteExistingCategory_SetsProductsCategoriesOfItToNull() throws EntityNotFoundException {
//        Product product = new ProductBuilder(123L, "", category).setPrice(1.0).build();
//        bar.addProduct(product);
//
//        service.deleteCategoryFromBar(123L, 123L);
//
//        assertNull(bar.getProducts().get(0).getCategory());
//    }
}