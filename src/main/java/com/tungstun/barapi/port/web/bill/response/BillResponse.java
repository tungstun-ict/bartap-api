package com.tungstun.barapi.port.web.bill.response;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

@ApiModel(description = "Response details about the bill")
public record BillResponse(
        @ApiModelProperty(notes = "The bill's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The bill's payment state")
        boolean isPayed,

        @ApiModelProperty(notes = "The bill's customer")
        Person customer,

        @ApiModelProperty(notes = "The bill's total price")
        Double totalPrice,

        @ApiModelProperty(notes = "The bill's orders")
        List<OrderResponse> orders) {
}
