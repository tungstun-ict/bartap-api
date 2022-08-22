package com.tungstun.security.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ApiModel(description = "Response details of customer")
public class AccountResponse {
    @ApiModelProperty(notes = "Users id")
    private Long id;

    @ApiModelProperty(notes = "Users email adress")
    private String email;

    @ApiModelProperty(notes = "Users username")
    private String username;

    @ApiModelProperty(notes = "Users first name")
    private String firstName;

    @ApiModelProperty(notes = "Users last name")
    private String lastName;

    @ApiModelProperty(notes = "Users connected bars")
    private Set<Long> connectedBars;

    public AccountResponse() {}

    public AccountResponse(Long id, String email, String username, String firstName, String lastName, Set<Long> connectedBars) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.connectedBars = connectedBars;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Long> getConnectedBars() {
        return connectedBars;
    }

    public void setConnectedBars(Set<Long> connectedBars) {
        this.connectedBars = connectedBars;
    }
}
