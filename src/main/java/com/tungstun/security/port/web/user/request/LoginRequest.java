package com.tungstun.security.port.web.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details to log in")
public record LoginRequest(
        @ApiModelProperty(notes = "The user's username or email address")
        @NotBlank
        String userIdentification,

        @ApiModelProperty(notes = "The user's password")
        @NotBlank
        String password) {
}
