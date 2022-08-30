package com.tungstun.barapi.port.web.bar.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details for updating a bar")
public record UpdateBarRequest(
        @ApiModelProperty(notes = "The bar's address")
        @NotBlank
        String address,

        @ApiModelProperty(notes = "The bar's name")
        @NotBlank
        String name,

        @ApiModelProperty(notes = "The bar's e-mail address")
        @NotBlank
        String mail,

        @ApiModelProperty(notes = "The bar's phone number")
        @NotBlank
        String phoneNumber) {
}
