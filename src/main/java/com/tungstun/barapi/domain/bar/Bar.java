package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bar")
@SQLDelete(sql = "UPDATE bar SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "deleted", columnDefinition = "BOOLEAN default false")
    private final boolean deleted = Boolean.FALSE;

    @Embedded
    private BarDetails details;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Person> people;

    @Where(clause = "deleted = false")
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
            fetch = FetchType.EAGER,
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

    public Session activeSession() {
        return sessions.stream()
                .filter(Session::isActive)
                .findFirst()
                .orElse(null);
    }

    public Session newSession(String name) {
        if (this.activeSession() != null)
            throw new DuplicateActiveSessionException("Bar already has an active session");
        Session session = Session.create(id, name);
        this.sessions.add(session);
        return session;
    }

    public Long getId() {
        return id;
    }

    public BarDetails getDetails() {
        return details;
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public boolean addPerson(Person person) {
        if (!this.people.contains(person)) return this.people.add(person);
        return false;
    }

    public boolean removePerson(Person person) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return id.equals(bar.id) &&
                details.equals(bar.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details);
    }
}
