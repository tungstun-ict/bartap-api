package com.tungstun.security.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details for an access token refresh request")
public class RefreshTokenRequest {
    @ApiModelProperty(notes = "The user's refresh token")
    @NotBlank
    public String refreshToken;

    @ApiModelProperty(notes = "The user's access token")
    @NotBlank
    public String accessToken;

}
