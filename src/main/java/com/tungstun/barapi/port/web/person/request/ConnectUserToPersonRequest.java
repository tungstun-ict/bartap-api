package com.tungstun.barapi.port.web.person.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details to connect a user to a person")
public record ConnectUserToPersonRequest(
        @ApiModelProperty(notes = "The connection token for the person to be connected to")
        @NotBlank
        String token) {
}
