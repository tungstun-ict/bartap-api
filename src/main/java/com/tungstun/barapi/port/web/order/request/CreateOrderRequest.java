package com.tungstun.barapi.port.web.order.request;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull
        UUID productId,
        @Value("1")
        Integer amount) {
}
