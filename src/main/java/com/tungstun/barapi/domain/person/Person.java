package com.tungstun.barapi.domain.person;

import com.fasterxml.jackson.annotation.*;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.bill.Bill;
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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "session_bartender",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "id") }
    )
    private List<Session> shifts;

    @JsonBackReference
    @OneToMany(
            mappedBy = "customer",
            targetEntity = Bill.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Bill> bills;

    public Person() { }
    public Person(String name, String phoneNumber, User user, List<Session> shifts, List<Bill> bills) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.shifts = shifts;
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

    public List<Session> getShifts() { return shifts; }

    public boolean addShift(Session shift) {
        if (!this.shifts.contains(shift)) return this.shifts.add(shift);
        return false;
    }

    public boolean removeShift(Session shift) {
        return this.shifts.remove(shift);
    }

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
