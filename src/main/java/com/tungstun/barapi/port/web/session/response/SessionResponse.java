package com.tungstun.barapi.port.web.session.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionResponse(
        UUID id,
        String name,
        LocalDateTime creationDate,
        LocalDateTime closedDate,
        Boolean isLocked,
        List<BillSummaryResponse> bills) {
}
