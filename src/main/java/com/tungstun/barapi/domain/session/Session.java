package com.tungstun.barapi.domain.session;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.BillFactory;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "end")
    private LocalDateTime endDate;

    @Column(name = "locked", nullable = false)
    private boolean ended;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    @OneToMany(
            mappedBy = "session",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Bill> bills;

    public Session() {
    }

    public Session(UUID id, String name, List<Bill> bills) {
        this.id = id;
        this.creationDate = ZonedDateTime.now().toLocalDateTime();
        this.ended = false;
        this.name = name;
        this.bills = bills;
    }

    public void end() {
        if (this.endDate != null) throw new IllegalStateException("Session already ended");
        this.endDate = ZonedDateTime.now().toLocalDateTime();
        ended = true;
    }

    public boolean isActive() {
        return this.endDate == null && !this.ended;
    }

    public UUID getId() {
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

    public boolean isEnded() {
        return ended;
    }

    public void checkEditable() {
        if (endDate != null || ended) {
            throw new InvalidSessionStateException("Cannot make changes to session if session is not active");
        }
    }

    public Bill addCustomer(Person customer) {
        checkEditable();
        boolean alreadyHasBill = bills.stream()
                .anyMatch(bill -> bill.getCustomer().equals(customer));
        if (alreadyHasBill) {
            throw new DuplicateRequestException(String.format("Session already contains a bill for customer with categoryId %s", customer.getId()));
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
                .orElseThrow(() -> new EntityNotFoundException("No Bill found with id: " + billId));
    }

    public List<Order> getAllOrders() {
        return bills.stream()
                .flatMap(bill -> bill.getOrders().stream())
                .collect(Collectors.toList());
    }
}
