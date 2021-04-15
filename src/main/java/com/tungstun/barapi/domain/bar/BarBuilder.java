package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;

import java.util.ArrayList;
import java.util.List;

public class BarBuilder {
    private BarDetails details;
    private List<Person> people;
    private List<Product> products;
    private List<Session> sessions;
    private List<Category> categories;

    public BarBuilder() {
        this.details = new BarDetails();
        this.people = new ArrayList<>();
        this.products = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public BarBuilder setAdres(String adres) {
        this.details.setAddress(adres);
        return this;
    }

    public BarBuilder setName(String name) {
        this.details.setName(name);
        return this;
    }

    public BarBuilder setMail(String mail) {
        this.details.setMail(mail);
        return this;
    }

    public BarBuilder setPhoneNumber(String phoneNumber) {
        this.details.setPhoneNumber(phoneNumber);
        return this;
    }

    public BarBuilder setUsers(List<Person> people) {
        this.people = people;
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

    public BarBuilder setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Bar build() {
        return new Bar(
                this.details,
                this.people,
                this.products,
                this.sessions,
                this.categories
        );
    }
}
