package com.tungstun.barapi.presentation.dto.response;

public class BarResponse {
    private Long id;
    private String adres;
    private String name;
    private String mail;
    private String phoneNumber;

    public BarResponse() {}

    public void setId(Long id) { this.id = id; }

    public void setAdres(String adres) { this.adres = adres; }

    public void setName(String name) { this.name = name; }

    public void setMail(String mail) { this.mail = mail; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Long getId() { return id; }

    public String getAdres() { return adres; }

    public String getName() { return name; }

    public String getMail() { return mail; }

    public String getPhoneNumber() { return phoneNumber; }

}
