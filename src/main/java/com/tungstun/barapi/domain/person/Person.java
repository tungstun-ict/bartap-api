package com.tungstun.barapi.domain.person;

import com.fasterxml.jackson.annotation.*;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.security.domain.user.User;

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

    @Column(name = "bar_id")
    private Long barId;

    @Column(name = "name")
    private String name;

    @OneToOne
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

    public Person(Long barId, String name, User user, List<Bill> bills) {
        this.barId = barId;
        this.name = name;
        this.user = user;
        this.bills = bills;
    }

    public Long getId() {
        return id;
    }

    public Long getBarId() {
        return barId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

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
