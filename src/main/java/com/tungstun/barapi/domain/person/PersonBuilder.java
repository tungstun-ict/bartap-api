package com.tungstun.barapi.domain.person;

import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.security.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class PersonBuilder {
    private String name;
    private String phoneNumber;
    private User user;
    private List<Session> shifts;
    private List<Bill> bills;

    public PersonBuilder() {
        this.name = null;
        this.phoneNumber = "";
        this.user = null;
        this.shifts = new ArrayList<>();
        this.bills = new ArrayList<>();
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public PersonBuilder setShifts(List<Session> shifts) {
        this.shifts = shifts;
        return this;
    }

    public PersonBuilder setBills(List<Bill> bills) {
        this.bills = bills;
        return this;
    }

    public Person build() {
        return new Person(
                this.name,
                this.phoneNumber,
                this.user,
                this.shifts,
                this.bills
        );
    }
}
