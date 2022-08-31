package com.tungstun.security.port.web.user.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response detail summary about the user account")
public record UserAccountSummaryResponse(
        @ApiModelProperty(notes = "The user's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The user's username")
        String username,

        @ApiModelProperty(notes = "The user's first name")
        String firstName,

        @ApiModelProperty(notes = "The user's last name")
        String lastName,

        @ApiModelProperty(notes = "The user's mail")
        String mail) {
}
