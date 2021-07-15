package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details about the session")
public class SessionRequest {
    @ApiModelProperty(notes = "The session's name")
    @NotNull
    public String name;
}
