package com.tungstun.barapi.port.web.category.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request details to create a category")
public record CreateCategoryRequest(
        @ApiModelProperty(notes = "The category's name")
        String name) {
}
