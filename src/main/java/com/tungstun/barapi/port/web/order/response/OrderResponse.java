package com.tungstun.barapi.port.web.order.response;

import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.port.web.person.response.PersonResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@ApiModel(description = "Response details about the order")
public record OrderResponse(
        @ApiModelProperty(notes = "The order's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The order's amount of products")
        int amount,

        @ApiModelProperty(notes = "The order's creation date")
        LocalDateTime creationDate,

        @ApiModelProperty(notes = "The order's product")
        OrderProduct product,

        @ApiModelProperty(notes = "The order's bartender")
        PersonResponse bartender) {
}
