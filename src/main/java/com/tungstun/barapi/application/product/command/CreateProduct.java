package com.tungstun.barapi.application.product.command;

import java.util.UUID;

public record CreateProduct(
        UUID barId,
        String name,
        String brand,
        Double size,
        Double price,
        boolean isFavorite,
        String productType,
        UUID categoryId) {
}
