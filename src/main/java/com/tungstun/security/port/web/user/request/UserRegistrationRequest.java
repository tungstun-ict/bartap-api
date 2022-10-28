package com.tungstun.security.port.web.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserRegistrationRequest(
    @NotBlank
    String username,
    @Size(min = 5)
    String password,
    String firstName,
    String lastName,
    @Email
    String mail,
    String phoneNumber) {
}
