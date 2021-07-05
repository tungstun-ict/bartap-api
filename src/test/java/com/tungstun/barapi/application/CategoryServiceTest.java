package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringCategoryRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.ProductType;
import javassist.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    private static final BarService barService = mock(BarService.class);
    private static final SpringCategoryRepository repository = mock(SpringCategoryRepository.class);
    private static final CategoryService service = new CategoryService(repository, barService);

    private static Stream<Arguments> provideBarsWithCategories() {
        Bar bar = new BarBuilder().build();
        List<Category> bar2Categories = List.of(new Category("category", ProductType.DRINK));
        Bar bar2 = new Bar(null, null, null, null, bar2Categories);
        List<Category> bar3Categories = List.of(new Category("category", ProductType.DRINK),
                new Category("category", ProductType.FOOD));
        Bar bar3 = new Bar(null, null, null, null, bar3Categories);
        List<Category> bar4Categories = List.of(new Category("category", ProductType.DRINK),
                new Category("category", ProductType.FOOD),
                new Category("category", ProductType.OTHER));
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
    void getCategoriesOfBar_ReturnsCategories(Bar bar, List<Category> expectedCategories) throws NotFoundException {
        when(barService.getBar(any()))
                .thenReturn(bar);

        List<Category> categories = service.getCategoriesOfBar(any(), null);

        assertEquals(expectedCategories, categories);
    }

    @ParameterizedTest
    @MethodSource("provideBarsWithCategoriesAndType")
    @DisplayName("Get all categories of bar of type")
    void getCategoriesOfBarOfType_ReturnsCategories(Bar bar, List<Category> expectedCategories, String type) throws NotFoundException {
        when(barService.getBar(any()))
                .thenReturn(bar);
        List<Category> categories = service.getCategoriesOfBar(any(), type);
        assertEquals(expectedCategories, categories);
    }
}