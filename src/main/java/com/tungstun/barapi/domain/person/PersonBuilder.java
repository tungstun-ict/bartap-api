package com.tungstun.barapi.domain.person;

import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.security.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class PersonBuilder {
    private final Long barId;
    private String name;
    private User user;
    private List<Bill> bills;

    public PersonBuilder(Long barId, String name) {
        this.barId = barId;
        this.name = name;
        this.user = null;
        this.bills = new ArrayList<>();
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public PersonBuilder setBills(List<Bill> bills) {
        this.bills = bills;
        return this;
    }

    public Person build() {
        return new Person(
                barId,
                name,
                user,
                bills
        );
    }
}
