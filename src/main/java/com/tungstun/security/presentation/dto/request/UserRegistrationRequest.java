package com.tungstun.security.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(description = "Request details about the user registration")
public class UserRegistrationRequest {
    @ApiModelProperty(notes = "The user's username")
    @NotBlank
    public String username;

    @ApiModelProperty(notes = "The user's password. Minimum length: 5")
    @Size(min = 5)
    public String password;

    @ApiModelProperty(notes = "The user's first name")
    public String firstName;

    @ApiModelProperty(notes = "The user's last name")
    public String lastName;

    @ApiModelProperty(notes = "The user's mail address")
    @NotBlank
    public String mail;

    @ApiModelProperty(notes = "The user's phone number")
    public String phoneNumber;
}
