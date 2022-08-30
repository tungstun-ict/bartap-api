package com.tungstun.barapi.port.web.bill.response;

import com.tungstun.barapi.port.web.session.response.SessionSummaryResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response detail summary about the bill")
public record BillSummaryResponse(
    @ApiModelProperty(notes = "The bill's categoryId")
    UUID id,

    @ApiModelProperty(notes = "The bill's payment state")
    boolean isPayed,

    @ApiModelProperty(notes = "The bill's total price")
    Double totalPrice,

    @ApiModelProperty(notes = "The bill's session")
    SessionSummaryResponse session) {
}
