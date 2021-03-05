package com.tungstun.barapi.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response details about the bar")
public class BarResponse {
    @ApiModelProperty(notes = "The bar's id")
    private Long id;

    @ApiModelProperty(notes = "The bar's address")
    private String address;

    @ApiModelProperty(notes = "The bar's name")
    private String name;

    @ApiModelProperty(notes = "The bar's mail")
    private String mail;

    @ApiModelProperty(notes = "The bar's phone number")
    private String phoneNumber;

    public BarResponse() {}

    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }

    public void setMail(String mail) { this.mail = mail; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Long getId() { return id; }

    public void setAddress(String address) { this.address = address; }

    public String getName() { return name; }

    public String getMail() { return mail; }

    public String getPhoneNumber() { return phoneNumber; }

}
