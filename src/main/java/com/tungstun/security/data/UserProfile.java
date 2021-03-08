package com.tungstun.security.data;

import java.util.HashMap;
import java.util.Map;

public class UserProfile {
    private String username;
    private Map<Long, String> barAuthorization;

    public UserProfile(String username) {
        this.username = username;
        this.barAuthorization = new HashMap<>();
    }
    public UserProfile(String username, Map<Long, String> barAuthorization) {
        this.username = username;
        this.barAuthorization = barAuthorization;
    }

    public String getUsername() { return username; }

    public Map<Long, String> getBarAuthorization() {
        return barAuthorization;
    }
}