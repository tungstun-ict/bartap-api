package com.tungstun.barapi.domain.person;

import com.fasterxml.jackson.annotation.*;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.security.data.model.User;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;

    @JsonBackReference
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Bill> bills;

    public Person() { }
    public Person(String name, String phoneNumber, User user, List<Bill> bills) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.bills = bills;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<Bill> getBills() {
        return bills;
    }

    public boolean addBill(Bill bill) {
        if (!this.bills.contains(bill)) return this.bills.add(bill);
        return false;
    }

    public boolean removeBill(Bill bill) {
        return this.bills.remove(bill);
    }
}
