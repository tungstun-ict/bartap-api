package com.tungstun.barapi.port.web.bill.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel(description = "Request details for creating a bill for a person")
public record CreateBillRequest(
        @ApiModelProperty(notes = "The ID of the bill's customer")
        @NotNull
        UUID customerId) {
}
