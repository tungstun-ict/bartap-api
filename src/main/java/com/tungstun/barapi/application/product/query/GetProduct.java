package com.tungstun.barapi.application.product.query;

import java.util.UUID;

public record GetProduct(
        UUID productId,
        UUID barId) {
}
