package com.tungstun.security.presentation.dto.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
