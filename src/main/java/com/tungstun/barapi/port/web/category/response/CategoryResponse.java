package com.tungstun.barapi.port.web.category.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response details about the category")
public record CategoryResponse(
        @ApiModelProperty(notes = "The category's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The category's name")
        String name) {
}
