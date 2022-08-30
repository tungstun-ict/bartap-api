package com.tungstun.barapi.port.web.session.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details to update a session")
public record UpdateSessionRequest(
        @ApiModelProperty(notes = "The session's name")
        @NotNull
        String name) {
}
