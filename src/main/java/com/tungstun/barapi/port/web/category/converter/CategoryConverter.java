package com.tungstun.barapi.port.web.category.converter;

import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.port.web.category.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {
    public CategoryResponse convert(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public List<CategoryResponse> convertAll(List<Category> categories) {
        return categories.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
