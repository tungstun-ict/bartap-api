package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details about the person")
public class PersonRequest {
    @ApiModelProperty(notes = "The person's name")
    @NotNull
    public String name;

    @ApiModelProperty(notes = "The person's phone number")
    @Value("")
    public String phoneNumber;
}
