package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request details about the bar")
public class BarRequest {
    @ApiModelProperty(notes = "The bar's address")
    @NotBlank
    public String address;

    @ApiModelProperty(notes = "The bar's name")
    @NotBlank
    public String name;

    @ApiModelProperty(notes = "The bar's e-mail address")
    @NotBlank
    public String mail;

    @ApiModelProperty(notes = "The bar's phone number")
    @NotBlank
    public String phoneNumber;

    public BarRequest() {
    }

    public BarRequest(@NotBlank String address, @NotBlank String name, @NotBlank String mail, @NotBlank String phoneNumber) {
        this.address = address;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
    }
}
