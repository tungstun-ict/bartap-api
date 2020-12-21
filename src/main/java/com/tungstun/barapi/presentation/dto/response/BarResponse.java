package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Product;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.User;

import java.util.ArrayList;
import java.util.List;

public class BarResponse {
    private Long id;
    private String adres;
    private String name;
    private String mail;
    private String phoneNumber;
    private List<String> users;
    private List<String> products;
    private List<String> sessions;

    public BarResponse() {}


//        Manual BarResponse constructor
    public BarResponse(Long id, String adres, String name, String mail, String phoneNumber, List<User> users, List<Product> products, List<Session> sessions) {
        this.id = id;
        this.adres = adres;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.users = new ArrayList<>();
        this.products = new ArrayList<>();
        this.sessions = new ArrayList<>();
        if (users != null){
            for (User user : users) this.users.add(user.toString());
        }
        if (products != null) {
            for (Product product : products) this.products.add(product.toString());
        }
        if (sessions != null) {
            for (Session session : sessions) this.sessions.add(session.toString());
        }
    }

    public void setId(Long id) { this.id = id; }

    public void setAdres(String adres) { this.adres = adres; }

    public void setName(String name) { this.name = name; }

    public void setMail(String mail) { this.mail = mail; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setUsers(List<String> users) { this.users = users; }

    public void setProducts(List<String> products) { this.products = products; }

    public void setSessions(List<String> sessions) { this.sessions = sessions; }


    public Long getId() { return id; }

    public String getAdres() { return adres; }

    public String getName() { return name; }

    public String getMail() { return mail; }

    public String getPhoneNumber() { return phoneNumber; }

    public List<String> getUsers() { return users; }

    public List<String> getProducts() { return products; }

    public List<String> getSessions() { return sessions; }
}
