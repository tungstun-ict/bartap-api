package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringCategoryRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.presentation.dto.request.CategoryRequest;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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
                Arguments.of(bar),
                Arguments.of(bar2),
                Arguments.of(bar3),
                Arguments.of(bar4)
        );
    }

    private static Stream<Arguments> provideBarsWithCategoriesAndType() {
        Category drink = new Category("category", ProductType.DRINK);
        Category food = new Category("category", ProductType.FOOD);
        Category other = new Category("category", ProductType.OTHER);

        List<Category> barCategories = new ArrayList<>(List.of(drink, food, other));
        Bar bar = new BarBuilder().setCategories(barCategories).build();
        return Stream.of(
                Arguments.of(bar, new ArrayList<>(List.of(food)), "FOOD"),
                Arguments.of(bar, new ArrayList<>(List.of(drink)), "DRINK"),
                Arguments.of(bar, new ArrayList<>(List.of(other)), "OTHER")
        );
    }

    @BeforeEach
    void setup() {
        bar = new BarBuilder().build();
        category =  repository.save(new Category("category", ProductType.FOOD));
        bar = barRepository.save(bar);
        bar.addCategory(category);
        bar = barRepository.save(bar);
    }

    @ParameterizedTest
    @MethodSource("provideBarsWithCategories")
    @DisplayName("Get all categories of bar")
    void getCategoriesOfBar_ReturnsCategories(Bar bar) throws NotFoundException {
        bar = barRepository.save(bar);
        List<Category> expectedCategories = bar.getCategories();

        List<Category> categories = service.getCategoriesOfBar(bar.getId(), null);

        assertEquals(categories.size(), expectedCategories.size());
    }

    @ParameterizedTest
    @MethodSource("provideBarsWithCategoriesAndType")
    @DisplayName("Get all categories of bar of type")
    void getCategoriesOfBarOfType_ReturnsCategories(Bar bar, List<Category> expectedCategories, String type) throws NotFoundException {
        bar = barRepository.save(bar);

        List<Category> categories = service.getCategoriesOfBar(bar.getId(), type);

        assertEquals(categories.size(), expectedCategories.size());
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
    void getCategoryOfBar() throws NotFoundException {
        Category c = service.getCategoryOfBar(bar.getId(), category.getId());

        assertNotNull(c);
    }

    @Test
    @DisplayName("Get not existing category of bar")
    void getNotExistingCategoryOfBar() {
        assertThrows(
                NotFoundException.class,
                () -> service.getCategoryOfBar(bar.getId(), 999L)
        );
    }

    @Test
    @DisplayName("Add category to bar")
    void addCategory()  {
        CategoryRequest request = new CategoryRequest();
        request.name = "category2";
        request.productType = ProductType.FOOD.toString();

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
    void updateExistingCategory() throws NotFoundException {
        CategoryRequest request = new CategoryRequest();
        request.name = "categoryNew";
        request.productType = ProductType.FOOD.toString();

        Category updatedCategory = service.updateCategoryOfBar(bar.getId(), category.getId(), request);

        assertEquals("categoryNew", updatedCategory.getName());
    }

    @Test
    @DisplayName("Update existing category name with already existing category in bar")
    void updateExistingDuplicateCategory() {
        Category category2 = new Category("categoryNew", ProductType.FOOD);
        ReflectionTestUtils.setField(category2, "id", 321L);
        bar.addCategory(category2);
        bar = barRepository.save(bar);

        CategoryRequest request = new CategoryRequest();
        request.name = "categoryNew";
        request.productType = ProductType.FOOD.toString();

        assertThrows(
                DuplicateRequestException.class,
                () -> service.updateCategoryOfBar(bar.getId(), category.getId(), request)
        );
    }


    @Test
    @DisplayName("Delete existing category in bar")
    void deleteExistingCategory(){
        assertDoesNotThrow(() -> service.deleteCategoryFromBar(bar.getId(), category.getId()));
    }

    @Test
    @DisplayName("Delete existing category in bar sets products of it to null category")
    void deleteExistingCategory_SetsProductsCategoriesOfItToNull() throws NotFoundException {
        Product product = new ProductBuilder()
                .setCategory(category)
                .build();
        bar.addProduct(product);

        service.deleteCategoryFromBar(bar.getId(), category.getId());

        Bar loadedBar = barRepository.findById(bar.getId()).get();
        assertNull(loadedBar.getProducts().get(0).getCategory());
    }
}
