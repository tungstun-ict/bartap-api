package com.tungstun.security.port.web.user.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;
import java.util.UUID;

@ApiModel(description = "Response details of customer")
public record AccountResponse(
        @ApiModelProperty(notes = "Users categoryId")
        Long id,

        @ApiModelProperty(notes = "Users username")
        String username,

        @ApiModelProperty(notes = "Users first name")
        String firstName,

        @ApiModelProperty(notes = "Users last name")
        String lastName,

        @ApiModelProperty(notes = "Users email address")
        String email,

        @ApiModelProperty(notes = "Users last phone number")
        String phoneNumber,

        @ApiModelProperty(notes = "Users connected bars")
        Set<UUID> connectedBars) {
}
