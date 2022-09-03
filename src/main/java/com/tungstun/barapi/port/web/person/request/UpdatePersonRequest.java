package com.tungstun.barapi.port.web.person.request;

import javax.validation.constraints.NotNull;

public record UpdatePersonRequest(
        @NotNull
        String name) {
}
