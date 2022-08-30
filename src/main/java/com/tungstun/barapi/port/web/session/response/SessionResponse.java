package com.tungstun.barapi.port.web.session.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.port.web.bill.response.BillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the session")
public record SessionResponse(
        @ApiModelProperty(notes = "The session's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The session's name")
        String name,

        @ApiModelProperty(notes = "The session's creation date")
        LocalDateTime creationDate,

        @ApiModelProperty(notes = "The date the session got close")
        LocalDateTime closedDate,

        @ApiModelProperty(notes = "Boolean if the session is locked")
        boolean isLocked,

        @ApiModelProperty(notes = "The session's bills")
        List<BillResponse> bills) {
}
