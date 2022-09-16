package com.tungstun.barapi.application.product.query;

import java.util.UUID;

public record GetProduct(
        UUID barId,
        UUID productId) {
}
