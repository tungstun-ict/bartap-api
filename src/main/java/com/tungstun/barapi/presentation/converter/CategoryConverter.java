package com.tungstun.barapi.presentation.converter;

import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.presentation.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {
    public CategoryResponse convert(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getProductType().toString()
        );
    }

    public List<CategoryResponse> convertAll(List<Category> categories) {
        return categories.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
