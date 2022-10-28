package com.tungstun.barapi.port.web.bar.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateBarRequest(
        @NotBlank
        String address,
        @NotBlank
        String name,
        @NotNull
        @Email
        String mail,
        @NotBlank
        String phoneNumber) {
}
