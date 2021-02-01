package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "session",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Bill> bills;

    @ManyToMany(mappedBy = "shifts")
    private List<Bartender> bartenders;

    public Session() {}
    public Session(LocalDateTime date, List<Bill> bills
            , List<Bartender> bartenders) {
        this.date = date;
        this.bills = bills;
        this.bartenders = bartenders;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Bill> getBills() {
        return bills;
    }

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
