package com.tungstun.barapi.port.web.order.response;

import java.util.UUID;

public record OrderProductResponse(
        UUID id,
        String name,
        String brand,
        Double price) {
}
