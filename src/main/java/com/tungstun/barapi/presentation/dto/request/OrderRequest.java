package com.tungstun.barapi.presentation.dto.request;

import javax.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull(message = "Customer ID cannot be empty")
    public Long customerId;

    @NotNull(message = "Customer ID cannot be empty")
    public Long bartenderId;
}
