package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bar")
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private BarDetails details;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Person> people;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> products;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Session> sessions;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Category> categories;

    public Bar() {
    }
    public Bar(BarDetails details,
               List<Person> people,
               List<Product> products,
               List<Session> sessions,
               List<Category> categories
    ) {
        this.details = details;
        this.people = people;
        this.products = products;
        this.sessions = sessions;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public BarDetails getDetails() {
        return details;
    }

    public List<Person> getUsers() {
        return this.people;
    }

    public boolean addUser(Person person) {
        if (!this.people.contains(person)) return this.people.add(person);
        return false;
    }

    public boolean removeUser(Person person) {
        return this.people.remove(person);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public boolean addProduct(Product product) {
        if (!this.products.contains(product)) return this.products.add(product);
        return false;
    }

    public boolean removeProduct(Product product) {
        return this.products.remove(product);
    }

    public List<Session> getSessions() {
        return this.sessions;
    }

    public boolean addSession(Session session) {
        if (!this.sessions.contains(session)) return this.sessions.add(session);
        return false;
    }

    public boolean removeSession(Session session) {
        return this.sessions.remove(session);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public boolean addCategory(Category category) {
        if (!this.categories.contains(category)) return this.categories.add(category);
        return false;
    }

    public boolean removeCategory(Category category) {
        return this.categories.remove(category);
    }
}
