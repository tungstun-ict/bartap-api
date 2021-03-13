package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.bill.Bill;

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
//@JsonIdentityReference(alwaysAsId = true)
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
    @JsonIgnore
    @OneToMany(mappedBy = "session",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Bill> bills;

    public Session() {}
    public Session(String name, LocalDateTime creationDate, List<Bill> bills) {
        this.name = name;
        this.creationDate = creationDate;
        this.bills = bills;
        this.isLocked = false;
    }

    public static Session create(String name) {
        return new Session(name,
                ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Amsterdam")).toLocalDateTime(),
                new ArrayList<>()
        );
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
}
