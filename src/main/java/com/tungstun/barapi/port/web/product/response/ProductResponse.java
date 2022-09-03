package com.tungstun.barapi.port.web.product.response;

import com.tungstun.barapi.port.web.category.response.CategoryResponse;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String brand,
        Double size,
        Double price,
        Boolean isFavorite,
        CategoryResponse category) {
}
