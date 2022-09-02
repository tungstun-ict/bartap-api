package com.tungstun.barapi.port.web.person.request;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

public record CreatePersonRequest(
        @NotNull
        String name,
        @Value("")
        String phoneNumber) {
}
