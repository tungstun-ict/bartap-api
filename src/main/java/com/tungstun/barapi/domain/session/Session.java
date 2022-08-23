package com.tungstun.barapi.domain.session;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.BillFactory;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Column(name = "bar_id")
    private Long barId;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "end")
    private LocalDateTime endDate;

    @Column(name = "locked", nullable = false)
    private boolean isLocked;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    @OneToMany(
            mappedBy = "session",
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<Bill> bills;

    public Session() {
    }

    public Session(Long barId, String name, List<Bill> bills) {
        this.creationDate = ZonedDateTime.now().toLocalDateTime();
        this.isLocked = false;
        this.barId = barId;
        this.name = name;
        this.bills = bills;
    }

    public static Session create(Long barId, String name) {
        return new Session(barId, name, new ArrayList<>());
    }

    public boolean end() {
        if (this.endDate != null) throw new IllegalStateException("Session already ended");
        this.endDate = ZonedDateTime.now().toLocalDateTime();
        return true;
    }

    public void lock() {
//        if (session.isLocked()) throw new InvalidSessionStateException("Cannot lock an already locked session");
        if (
//                endDate == null ||
                isLocked)
            throw new InvalidSessionStateException("Cannot lock session that is still ongoing or is already locked");
        isLocked = true;
    }

    public boolean isActive() {
        return this.endDate == null && !this.isLocked;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void checkEditable() {
        if (endDate != null || isLocked) {
            throw new InvalidSessionStateException("Cannot make changes to session if session is not active");
        }
    }

    public Bill addCustomer(Person customer) {
        checkEditable();
        boolean alreadyHasBill = bills.stream()
                .anyMatch(bill -> bill.getCustomer().equals(customer));
        if (alreadyHasBill) {
            throw new DuplicateRequestException(String.format("Session already contains a bill for customer with id %s", customer.getId()));
        }
        Bill bill = new BillFactory(this, customer).create();
        bills.add(bill);
        return bill;
    }

    public boolean removeBill(UUID billId) {
        checkEditable();
        return bills.removeIf(bill -> bill.getId().equals(billId));
    }

    public Bill getBill(UUID billId) {
        return bills.stream()
                .filter(bill -> bill.getId().equals(billId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No Bill found with id " + billId));
    }

    public List<Order> getAllOrders() {
        return bills.stream()
                .flatMap(bill -> bill.getOrders().stream())
                .collect(Collectors.toList());
    }
}
