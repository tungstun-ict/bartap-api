package com.tungstun.barapi.domain.person;

import com.tungstun.security.domain.user.Role;
import com.tungstun.security.domain.user.User;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "person")
public class Person {
    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToOne
    private User user;

    public Person() {
    }

    public Person(UUID id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void connectUser(User user, UUID barId) {
        if (this.user != null) {
            throw new IllegalStateException(String.format("Person with id %s is already connected to a user", id));
        }
        this.user = user;
        user.authorize(barId, Role.CUSTOMER, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(user, person.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, user);
    }
}
