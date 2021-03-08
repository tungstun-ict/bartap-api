package com.tungstun.security.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details about the user login")
public class LoginRequest {
    @ApiModelProperty(notes = "The user's username")
    @NotBlank
    public String username;

    @ApiModelProperty(notes = "The user's password")
    @NotBlank
    public String password;
}
