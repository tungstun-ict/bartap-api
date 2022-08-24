package com.tungstun.security.domain.user;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "authorization")
public class Authorization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "bar_id")
    private UUID barId;

    @Column(name = "role")
    @Enumerated(STRING)
    private Role role;

    public Authorization() {
    }
    public Authorization(UUID barId, Role role) {
        this.barId = barId;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public UUID getBarId() {
        return barId;
    }

    public Role getRole() {
        return role;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
