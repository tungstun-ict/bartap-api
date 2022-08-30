package com.tungstun.barapi.port.web.session.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the session")
public record SessionSummaryResponse(
        @ApiModelProperty(notes = "The session's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The session's name")
        String name) {
}
