package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel(description = "Request details about the order")
public class OrderRequest {
    @ApiModelProperty(notes = "The ID of the order's product")
    @NotNull
    public UUID productId;

    @ApiModelProperty(notes = "The order's amount of products")
    @Value("1")
    public Integer amount;
}
