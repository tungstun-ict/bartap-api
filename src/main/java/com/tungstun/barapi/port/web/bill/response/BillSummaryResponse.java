package com.tungstun.barapi.port.web.bill.response;

import com.tungstun.barapi.port.web.session.response.SessionSummaryResponse;

import java.util.UUID;

public record BillSummaryResponse(
    UUID id,
    Boolean isPayed,
    Double totalPrice,
    SessionSummaryResponse session) {
}
