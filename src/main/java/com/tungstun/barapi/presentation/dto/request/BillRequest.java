package com.tungstun.barapi.presentation.dto.request;

import javax.validation.constraints.NotNull;

public class BillRequest {
    @NotNull
    public Long sessionId;

    @NotNull
    public Long customerId;
}
