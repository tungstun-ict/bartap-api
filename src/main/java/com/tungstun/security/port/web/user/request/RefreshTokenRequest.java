package com.tungstun.security.port.web.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details to refresh access token")
public record RefreshTokenRequest(
        @ApiModelProperty(notes = "The user's refresh token")
        @NotBlank
        String refreshToken,

        @ApiModelProperty(notes = "The user's access token")
        @NotBlank
        String accessToken) {

}
