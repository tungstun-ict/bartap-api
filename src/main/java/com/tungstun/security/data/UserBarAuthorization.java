package com.tungstun.security.data;

import javax.persistence.*;

@Entity
@Table(name = "user_bar_authorization")
public class UserBarAuthorization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "bar_id")
    private Long barId;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserBarAuthorization() { }
    public UserBarAuthorization(Long barId, Long userId, UserRole role) {
        this.barId = barId;
        this.userId = userId;
        this.role = role;
    }

    public Long getId() { return id; }

    public Long getBarId() { return barId; }

    public Long getUserId() { return userId; }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
