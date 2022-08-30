package com.tungstun.barapi.port.web.person.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details to update a person")
public record UpdatePersonRequest(
        @ApiModelProperty(notes = "The person's name")
        @NotNull
        String name,

        @ApiModelProperty(notes = "The person's phone number")
        @Value("")
        String phoneNumber) {
}
