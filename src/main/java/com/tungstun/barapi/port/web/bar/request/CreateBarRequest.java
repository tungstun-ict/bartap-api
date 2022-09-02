package com.tungstun.barapi.port.web.bar.request;

import javax.validation.constraints.NotBlank;

public record CreateBarRequest(
        @NotBlank
        String address,
        @NotBlank
        String name,
        @NotBlank
        String mail,
        @NotBlank
        String phoneNumber) {
}
