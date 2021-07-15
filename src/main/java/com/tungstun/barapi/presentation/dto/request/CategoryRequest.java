package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request details about the category")
public class CategoryRequest {
    @ApiModelProperty(notes = "The category's name")
    public String name;

    @ApiModelProperty(notes = "The category's product type")
    public String productType;
}
