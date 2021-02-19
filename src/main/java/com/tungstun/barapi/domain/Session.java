package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.bill.Bill;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @OneToMany(mappedBy = "session",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Bill> bills;

    @JsonManagedReference
    @ManyToMany(mappedBy = "shifts")
    private List<Bartender> bartenders;

    public Session() {}
    public Session(List<Bill> bills, List<Bartender> bartenders
    ) {
        this.creationDate = LocalDateTime.now();
        this.bills = bills;
        this.bartenders = bartenders;
    }

    public static Session create() {
        return new Session(new ArrayList<>(), new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public LocalDateTime getCreationDate() { return creationDate; }

    public LocalDateTime getClosedDate() { return closedDate; }

    public void setClosedDate(LocalDateTime closedDate) { this.closedDate = closedDate; }

    public boolean addBill(Bill bill){
        if ( !this.bills.contains(bill) ) return this.bills.add(bill);
        return false;
    }

    public boolean removeBill(Bill bill){ return this.bills.remove(bill); }

    public List<Bartender> getBartenders() { return this.bartenders; }

    public boolean addBartender(Bartender bartender){
        if ( !this.bartenders.contains(bartender) ) return this.bartenders.add(bartender);
        return false;
    }

    public boolean removeBartender(Bartender bartender){ return this.bartenders.remove(bartender); }
}
