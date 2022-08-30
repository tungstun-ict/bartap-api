package com.tungstun.barapi.port.web.category.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request details to update a category")
public record UpdateCategoryRequest(
        @ApiModelProperty(notes = "The category's name")
        String name) {
}
