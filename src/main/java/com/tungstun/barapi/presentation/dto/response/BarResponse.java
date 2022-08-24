package com.tungstun.barapi.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response details about the bar")
public class BarResponse {
    @ApiModelProperty(notes = "The bar's categoryId")
    private UUID id;

    @ApiModelProperty(notes = "The bar's address")
    private String address;

    @ApiModelProperty(notes = "The bar's name")
    private String name;

    @ApiModelProperty(notes = "The bar's mail")
    private String mail;

    @ApiModelProperty(notes = "The bar's phone number")
    private String phoneNumber;

    public BarResponse() {}

    public UUID getId() { return id; }

    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }

    public void setMail(String mail) { this.mail = mail; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setId(UUID id) { this.id = id; }

    public void setAddress(String address) { this.address = address; }

    public String getName() { return name; }

    public String getMail() { return mail; }

    public String getPhoneNumber() { return phoneNumber; }

}
