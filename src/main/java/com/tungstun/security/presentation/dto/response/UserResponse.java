package com.tungstun.security.presentation.dto.response;

import com.tungstun.security.data.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request details about the user")
public class UserResponse {
    @ApiModelProperty(notes = "The user's id")
    private Long id;

    @ApiModelProperty(notes = "The user's username")
    private String username;

    @ApiModelProperty(notes = "The user's role")
    private UserRole role;

    public UserResponse() { }
    public UserResponse(Long id, String username,UserRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) { this.role = role; }
}
