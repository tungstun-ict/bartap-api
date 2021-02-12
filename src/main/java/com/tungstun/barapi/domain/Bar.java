package com.tungstun.barapi.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bar")
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "adres")
    private String adres;

    @Column(name = "name")
    private String name;

    @Column(name = "mail")
    private String mail;

    @Column(name = "phone_number")
    private String phoneNumber;

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
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Session> sessions;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Category> categories;

    public Bar() {}
    public Bar(String adres, String name, String mail, String phoneNumber,
               List<Person> people, List<Product> products, List<Session> sessions, List<Category> categories) {
        this.adres = adres;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.people = people;
        this.products = products;
        this.sessions = sessions;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getAdres() { return adres; }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Person> getUsers() {
        return this.people;
    }

    public boolean addUser(Person person){
        if ( !this.people.contains(person) ) return this.people.add(person);
        return false;
    }

    public boolean removeUser(Person person){
        return this.people.remove(person);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public boolean addProduct(Product product){
        if ( !this.products.contains(product) ) return this.products.add(product);
        return false;
    }

    public boolean removeProduct(Product product){
        return this.products.remove(product);
    }

    public List<Session> getSessions() {
        return this.sessions;
    }

    public boolean addSession(Session session){
        if ( !this.sessions.contains(session) ) return this.sessions.add(session);
        return false;
    }

    public boolean removeSession(Session session){
        return this.sessions.remove(session);
    }

    public List<Category> getCategories() { return categories; }

    public boolean addCategory(Category category){
        if ( !this.categories.contains(category) ) return this.categories.add(category);
        return false;
    }

    public boolean removeCategory(Category category){
        return this.categories.remove(category);
    }
}
