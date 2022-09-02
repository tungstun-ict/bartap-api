package com.tungstun.security.port.web.user.request;

import javax.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String refreshToken,
        @NotBlank
        String accessToken) {

}
