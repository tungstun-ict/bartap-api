package com.tungstun.barapi.domain;

import javax.persistence.*;
import java.util.List;

@Entity
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

    @Transient
    private List<User> users;

    @Transient
    private List<Product> products;

    @Transient
    private List<Session> sessions;

    public Bar() {}

    public Bar(String adres, String name, String mail, String phoneNumber, List<User> users, List<Product> products, List<Session> sessions) {
        this.adres = adres;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.users = users;
        this.products = products;
        this.sessions = sessions;
    }



    public Long getId() {
        return id;
    }

    public String getAdres() {
        return adres;
    }

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

    public List<User> getUsers() {
        return this.users;
    }

    public boolean addUser(User user){
        if ( !this.users.contains(user) ){
            return this.users.add(user);
        }
        return false;

    }
    public boolean removeUser(User user){
        return this.users.remove(user);
    }


    public List<Product> getProducts() {
        return this.products;
    }

    public boolean addProduct(Product product){
        if ( !this.products.contains(product) ){
            return this.products.add(product);
        }
        return false;

    }
    public boolean removeProduct(Product product){
        return this.products.remove(product);
    }


    public List<Session> getSessions() {
        return this.sessions;
    }

    public boolean addSession(Session session){
        if ( !this.sessions.contains(session) ){
            return this.sessions.add(session);
        }
        return false;

    }
    public boolean removeSession(Session session){
        return this.sessions.remove(session);
    }
}
