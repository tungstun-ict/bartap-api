package com.tungstun.security.domain.user;

import com.tungstun.barapi.domain.person.Person;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "\"authorization\"")
public class Authorization {
    @Id
    private UUID id;

    @Column(name = "bar_id")
    private UUID barId;

    @Column(name = "role")
    @Enumerated(STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private Person person;

    public Authorization() {
    }
    public Authorization(UUID id, UUID barId, Role role, Person person) {
        this.id = id;
        this.barId = barId;
        this.role = role;
        this.person = person;
    }

    public UUID getId() {
        return id;
    }

    public UUID getBarId() {
        return barId;
    }

    public Role getRole() {
        return role;
    }

    public void updateRole(Role role) {
        if (this.role.equals(Role.OWNER)) {
            throw new IllegalArgumentException("Cannot change owner's role to a lower role");
        }
        this.role = role;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
