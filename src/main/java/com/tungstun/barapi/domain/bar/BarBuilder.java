package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BarBuilder {
    private final BarDetails details;
    private List<Person> people;
    private List<Product> products;
    private List<Session> sessions;
    private List<Category> categories;

    public BarBuilder(String name) {
        this.details = new BarDetails(name, null, null, null);
        this.people = new ArrayList<>();
        this.products = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public BarBuilder setAddress(String adres) {
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

    public BarBuilder setPeople(List<Person> people) {
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
                UUID.randomUUID(),
                details,
                people,
                products,
                sessions,
                categories
        );
    }
}
