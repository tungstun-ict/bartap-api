package com.tungstun.security.data.model;

import com.tungstun.barapi.domain.bar.Bar;

import javax.persistence.*;

@Entity
@Table(name = "user_bar_authorization")
public class UserBarAuthorization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bar_id")
    private Bar bar;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserBarAuthorization() { }
    public UserBarAuthorization(Bar bar, User user, UserRole role) {
        this.bar = bar;
        this.user = user;
        this.role = role;
    }

    public Long getId() { return id; }

    public Bar getBar() { return bar; }

    public User getUser() { return user; }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
