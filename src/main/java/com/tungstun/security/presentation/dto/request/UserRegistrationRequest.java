package com.tungstun.security.presentation.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegistrationRequest {
    @NotBlank
    public String username;

    @Size(min = 5)
    public String password;
}
