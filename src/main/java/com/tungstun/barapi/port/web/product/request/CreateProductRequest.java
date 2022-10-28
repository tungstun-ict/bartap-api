package com.tungstun.barapi.port.web.product.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record CreateProductRequest(
        @NotBlank(message = "Name cannot be null or blank")
        String name,
        @NotBlank(message = "Brand cannot be null or blank")
        String brand,
        @Min(value = 0, message = "Size cannot be 0 or lower")
        Double size,
        @Min(value = 0, message = "Price cannot be 0 or lower")
        Double price,
        @NotNull
        Boolean isFavorite,
        @NotNull
        String productType,
        @NotNull
        UUID categoryId) {
}
