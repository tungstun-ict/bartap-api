package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.person.Person;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIdentityReference(alwaysAsId = true)
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Column(name = "locked", nullable = false)
    private boolean isLocked;

    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "session",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Bill> bills;

    @JsonManagedReference
    @ManyToMany(mappedBy = "shifts")
    private List<Person> bartenders;

    public Session() {}
    public Session(String name, List<Bill> bills, List<Person> bartenders) {
        this.name = name;
        this.creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Amsterdam")).toLocalDateTime();
        this.bills = bills;
        this.bartenders = bartenders;
        this.isLocked = false;
    }

    public static Session create(String name) {
        return new Session(name, new ArrayList<>(), new ArrayList<>());
    }

    public void endSession() {
        this.closedDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Amsterdam")).toLocalDateTime();
    }

    public void lock() {
        if (this.closedDate == null) endSession();
        isLocked = true;
    }

    public boolean isActive() {
        return this.closedDate == null && !this.isLocked;
    }

    public Long getId() {
        return id;
    }

    public String getName() { return name; }

    public void setName(String title) { this.name = title; }

    public List<Bill> getBills() {
        return bills;
    }

    public LocalDateTime getCreationDate() { return creationDate; }

    public LocalDateTime getClosedDate() { return closedDate; }

    public boolean isLocked() { return isLocked; }

    public boolean addBill(Bill bill){
        if (!this.bills.contains(bill)) return this.bills.add(bill);
        return false;
    }

    public boolean removeBill(Bill bill){ return this.bills.remove(bill); }

    public List<Person> getBartenders() { return this.bartenders; }

    public boolean addBartender(Person bartender){
        if (this.bartenders.contains(bartender) && bartender.getShifts().contains(this)) return false;
        return this.bartenders.add(bartender);
    }

    public boolean removeBartender(Person bartender){
        if (bartender.getShifts().contains(this)) bartender.removeShift(this);
        return this.bartenders.remove(bartender);
    }
}
