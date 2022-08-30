package com.tungstun.barapi.port.web.order.response;

import com.tungstun.barapi.port.web.person.response.PersonResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@ApiModel(description = "Response details about the order history entry")
public record OrderHistoryEntryResponse(
        @ApiModelProperty(notes = "The order entry's id")
        UUID id,

        @ApiModelProperty(notes = "The order entry's type")
        String type,

        @ApiModelProperty(notes = "The order's date")
        LocalDateTime date,

        @ApiModelProperty(notes = "The order entry's product's id")
        UUID productId,

        @ApiModelProperty(notes = "The order entry's product's name")
        String productName,

        @ApiModelProperty(notes = "The order entry's amount of products")
        Integer amount,

        @ApiModelProperty(notes = "The order entry's customer")
        PersonResponse customer,

        @ApiModelProperty(notes = "The order entry's bartender")
        PersonResponse bartender) {
}
