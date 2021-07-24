package com.tungstun.barapi.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response details about the product's stock")
public class StockResponse {
    @ApiModelProperty(notes = "The stock's amount")
    private long amount;

    public StockResponse() {
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
