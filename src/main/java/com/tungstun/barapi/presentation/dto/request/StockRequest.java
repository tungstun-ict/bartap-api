package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

@ApiModel(description = "Request details for the product's stock")
public class StockRequest {
    @ApiModelProperty(notes = "The stock's amount")
    @Min(value = 0, message = " cannot be 0 or lower")
    public double amount;
}
