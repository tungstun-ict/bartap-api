package com.tungstun.barapi.domain.bar;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import com.tungstun.exception.DuplicateActiveSessionException;
import com.tungstun.security.domain.user.User;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bar")
@SQLDelete(sql = "UPDATE bar SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Bar {
    @Column(name = "deleted", columnDefinition = "BOOLEAN default false")
    private final boolean deleted = Boolean.FALSE;

    @Id
    private UUID id;

    @Embedded
    private BarDetails details;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Person> people;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Category> categories;

    public Bar() {
    }

    public Bar(UUID id,
               BarDetails details,
               List<Person> people,
               List<Product> products,
               List<Session> sessions,
               List<Category> categories
    ) {
        this.id = id;
        this.details = details;
        this.people = people;
        this.products = products;
        this.sessions = sessions;
        this.categories = categories;
    }

    public Session getActiveSession() {
        return sessions.stream()
                .filter(Session::isActive)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No active session found"));
    }

    public Session newSession(String name) {
        try {
            getActiveSession();
            throw new DuplicateActiveSessionException("Bar already has an active session");
        } catch (EntityNotFoundException ignored) {
        }
        Session session = new SessionFactory(name).create();
        sessions.add(session);
        return session;
    }

    public Category createCategory(String name) {
        boolean exists = categories.stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(name));
        if (exists) {
            throw new DuplicateRequestException("Bar already has category with name " + name);
        }
        Category category = new CategoryFactory(name).create();
        categories.add(category);
        return category;
    }

    public Person createPerson(String name) {
        return createPerson(name, null);
    }

    public Person createPerson(String name, User user) {
        boolean exists = people.stream()
                .anyMatch(person -> person.getName().equals(name));
        if (exists) {
            throw new DuplicateRequestException("Bar already has person with name " + name);
        }
        Person person = new PersonBuilder(name)
                .setUser(user)
                .build();
        people.add(person);
        return person;
    }

    public void connectUserToPerson(User user, UUID personId) {
        boolean isAlreadyConnected = people
                .stream()
                .filter(person -> person.getUser() != null)
                .anyMatch(person -> person.getUser().getId().equals(user.getId()));
        if (isAlreadyConnected) {
            throw new IllegalArgumentException("User is already connected to another person of the bar");
        }

        people.stream()
                .filter(p -> p.getId().equals(personId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No person found with id: " + personId))
                .connectUser(user, id);
    }

    public UUID getId() {
        return id;
    }

    public BarDetails getDetails() {
        return details;
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public boolean removePerson(Person person) {
        return this.people.remove(person);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public boolean addProduct(Product product) {
        return this.products.add(product);
    }

    public List<Session> getSessions() {
        return this.sessions;
    }

    public List<Category> getCategories() {
        return categories;
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
