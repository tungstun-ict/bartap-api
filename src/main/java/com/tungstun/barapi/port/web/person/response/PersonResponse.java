package com.tungstun.barapi.port.web.person.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the person")
public record PersonResponse(
    @ApiModelProperty(notes = "The person's categoryId")
    UUID id,

    @ApiModelProperty(notes = "The person's name")
    String name,

    @ApiModelProperty(notes = "The person's user id")
    UUID userId) {
}
