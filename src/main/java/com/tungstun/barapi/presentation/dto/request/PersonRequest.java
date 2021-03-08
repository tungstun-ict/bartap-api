package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details about the person")
public class PersonRequest {
    @ApiModelProperty(notes = "The person's name")
    @NotNull
    public String name;

    @ApiModelProperty(notes = "The person's phone number")
    public String phoneNumber;
}
