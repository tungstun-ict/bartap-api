package com.tungstun.barapi.port.web.person.request;

import javax.validation.constraints.NotBlank;

public record ConnectUserToPersonRequest(
        @NotBlank
        String token) {
}
