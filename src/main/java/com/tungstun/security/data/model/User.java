package com.tungstun.security.data.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserBarAuthorization> userBarAuthorizations;

    public User() { }
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) { this.role = role; }


    public List<UserBarAuthorization> getUserBarAuthorizations() { return userBarAuthorizations; }

    public void setUserBarAuthorizations(List<UserBarAuthorization> userBarAuthorization) {
        this.userBarAuthorizations = userBarAuthorization;
    }

    public Map<Long, String> getAuthoritiesMap() {
        Map<Long, String> barAuth = new HashMap<>();
        for (UserBarAuthorization userBarAuthorization : userBarAuthorizations) {
            barAuth.put(userBarAuthorization.getBarId(), userBarAuthorization.getRole().toString());
        }
        return barAuth;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }
}
