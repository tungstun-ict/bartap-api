package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel(description = "Request details about the bill")
public class BillRequest {
    @ApiModelProperty(notes = "The ID of the bill's customer")
    @NotNull
    public UUID customerId;
}
