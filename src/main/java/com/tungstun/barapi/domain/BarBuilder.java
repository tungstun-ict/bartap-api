package com.tungstun.barapi.domain;

import java.util.ArrayList;
import java.util.List;

public class BarBuilder {

    private String adres;
    private String name;
    private String mail;
    private String phoneNumber;
    private List<User> users;
    private List<Product> products;
    private List<Session> sessions;

    public BarBuilder() {
        this.users = new ArrayList<>();
        this.products = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }


    public BarBuilder setAdres(String adres) {
        this.adres = adres;
        return this;
    }

    public BarBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BarBuilder setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public BarBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public BarBuilder setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public BarBuilder setProducts(List<Product> products) {
        this.products = products;
        return this;
    }

    public BarBuilder setSessions(List<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public Bar build() {
        return new Bar(this.adres, this.name,
                this.mail, this.phoneNumber,
                this.users, this.products, this.sessions);
    }
}
