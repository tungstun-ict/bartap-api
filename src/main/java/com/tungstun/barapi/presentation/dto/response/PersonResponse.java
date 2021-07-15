package com.tungstun.barapi.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the person")
public class PersonResponse {
    @ApiModelProperty(notes = "The person's id")
    private Long id;

    @ApiModelProperty(notes = "The person's name")
    private String name;

    @ApiModelProperty(notes = "The person's phone number")
    private String phoneNumber;

    @ApiModelProperty(notes = "The person's user account")
    private UserAccountSummary user;

    public PersonResponse() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phonenumber) { this.phoneNumber = phonenumber; }

    public UserAccountSummary getUser() { return user; }

    public void setUser(UserAccountSummary user) { this.user = user; }
}
