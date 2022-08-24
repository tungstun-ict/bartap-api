package com.tungstun.barapi.domain.person;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.security.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Person {
    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JsonIgnore
    private User user;

//    @JsonBackReference
//    @OneToMany(
//            mappedBy = "customer",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Bill> bills;

    public Person() {
    }

    public Person(UUID id, String name, User user
//                  , List<Bill> bills
    ) {
        this.id = id;
        this.name = name;
        this.user = user;
//        this.bills = bills;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public List<Bill> getBills() {
//        return bills;
//    }
//
//    public boolean addBill(Bill bill) {
//        if (!this.bills.contains(bill)) return this.bills.add(bill);
//        return false;
//    }
//
//    public boolean removeBill(Bill bill) {
//        return this.bills.remove(bill);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(user, person.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, user);
    }
}
