package com.tungstun.barapi.port.web.bar.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response details about the bar")
public record BarResponse(
        @ApiModelProperty(notes = "The bar's categoryId")
        UUID id,

        @ApiModelProperty(notes = "The bar's address")
        String address,

        @ApiModelProperty(notes = "The bar's name")
        String name,

        @ApiModelProperty(notes = "The bar's mail")
        String mail,

        @ApiModelProperty(notes = "The bar's phone number")
        String phoneNumber) {
}
