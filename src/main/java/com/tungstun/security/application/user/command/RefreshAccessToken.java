package com.tungstun.security.application.user.command;

public record RefreshAccessToken(
        String accessToken,
        String refreshToken) {
}
