package com.tungstun.security.data.model;

public class UserProfile extends User{
    private final String username;

    public UserProfile(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
}