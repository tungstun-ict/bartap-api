package com.tungstun.barapi.port.web.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel(description = "Request details about the order")
public record CreateOrderRequest(
        @ApiModelProperty(notes = "The ID of the order's product")
        @NotNull
        UUID productId,

        @ApiModelProperty(notes = "The order's amount of products")
        @Value("1")
        Integer amount) {
}
