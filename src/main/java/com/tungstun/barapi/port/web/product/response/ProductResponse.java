package com.tungstun.barapi.port.web.product.response;

import com.tungstun.barapi.domain.product.Category;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String brand,
        double size,
        double price,
        boolean isFavorite,
        Category category) {
}
