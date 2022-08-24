package com.tungstun.barapi.domain.person;

import com.tungstun.security.domain.user.User;

import java.util.UUID;

public class PersonBuilder {
    private String name;
    private User user;
//    private List<Bill> bills;

    public PersonBuilder(String name) {
        this.name = name;
        this.user = null;
//        this.bills = new ArrayList<>();
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setUser(User user) {
        this.user = user;
        return this;
    }

//    public PersonBuilder setBills(List<Bill> bills) {
//        this.bills = bills;
//        return this;
//    }

    public Person build() {
        return new Person(
                UUID.randomUUID(),
                name,
                user
        );
    }
}
