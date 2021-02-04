package com.tungstun.barapi.presentation.dto.request;

import javax.validation.constraints.NotNull;

public class PersonRequest {
    @NotNull
    public String name;

    public String phoneNumber;
}
