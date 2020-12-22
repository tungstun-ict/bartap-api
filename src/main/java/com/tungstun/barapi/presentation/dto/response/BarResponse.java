package com.tungstun.barapi.presentation.dto.response;

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
