package com.tungstun.barapi.port.web.bill.request;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record CreateBillRequest(
        @NotNull
        UUID personId) {
}
