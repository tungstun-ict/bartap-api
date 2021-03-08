package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details about the order")
public class OrderRequest {
    @ApiModelProperty(notes = "The ID of the order's bartender")
    @NotNull
    public Long bartenderId;

    @ApiModelProperty(notes = "The ID of the order's product")
    @NotNull
    public Long productId;

    @ApiModelProperty(notes = "The order's amount of products")
    public Integer amount;
}
