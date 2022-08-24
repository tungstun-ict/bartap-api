package com.tungstun.security.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response detail summary about the user account")
public class UserAccountSummaryResponse {
    @ApiModelProperty(notes = "The user's categoryId")
    private Long id;

    @ApiModelProperty(notes = "The user's username")
    private String username;

    @ApiModelProperty(notes = "The user's mail")
    private String mail;

    @ApiModelProperty(notes = "The user's first name")
    private String firstName;

    @ApiModelProperty(notes = "The user's last name")
    private String lastName;

    public UserAccountSummaryResponse() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }
}
