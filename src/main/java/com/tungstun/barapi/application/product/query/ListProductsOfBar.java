package com.tungstun.barapi.application.product.query;

import java.util.UUID;

public record ListProductsOfBar(
        UUID barId,
        UUID categoryId,
        Boolean isFavorite,
        String productType,
        String searchText) {
}
