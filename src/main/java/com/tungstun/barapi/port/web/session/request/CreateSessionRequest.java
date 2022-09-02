package com.tungstun.barapi.port.web.session.request;

import javax.validation.constraints.NotNull;

public record CreateSessionRequest(
        @NotNull
        String name) {
}
