package com.tungstun.security.port.web.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(description = "Request details to register user")
public record UserRegistrationRequest(
    @ApiModelProperty(notes = "The user's username")
    @NotBlank
    String username,

    @ApiModelProperty(notes = "The user's password. Minimum length: 5")
    @Size(min = 5)
    String password,

    @ApiModelProperty(notes = "The user's first name")
    String firstName,

    @ApiModelProperty(notes = "The user's last name")
    String lastName,

    @ApiModelProperty(notes = "The user's mail address")
    @NotBlank
    String mail,

    @ApiModelProperty(notes = "The user's phone number")
    String phoneNumber) {
}
