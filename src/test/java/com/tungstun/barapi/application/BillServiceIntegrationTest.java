package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.BillFactory;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BillServiceIntegrationTest {
    @Autowired
    private SpringBillRepository repository;
    @Autowired
    private SpringSessionRepository sessionRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private BillService service;

    private Bar bar;
    private Bill bill;
    private Session session;
    private Person person;

    @BeforeEach
    void setup() {
        bar = new BarBuilder().build();
        session = Session.create("test");
        session = sessionRepository.save(session);
        person = personRepository.save(new PersonBuilder().build());
        bill = repository.save(new BillFactory(session, person).create());
        person.addBill(bill);
        session.addBill(bill);
        session = sessionRepository.save(session);
        bar.addSession(session);
        bar.addUser(person);
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Get All Bills")
    void getAllBills() throws NotFoundException {
        List<Bill> resBills = service.getAllBills(bar.getId());

        assertEquals(1, resBills.size());
        assertTrue(resBills.contains(bill));
    }

    @Test
    @DisplayName("Get All Bills when none")
    void getAllBillsWhenNone() throws NotFoundException {
        session.removeBill(bill);
        sessionRepository.save(session);

        List<Bill> resBills = service.getAllBills(bar.getId());

        assertEquals(0, resBills.size());
    }

    @Test
    @DisplayName("Get Bill of bar")
    void getBillOfBar() throws NotFoundException {
        Bill resBill = service.getBillOfBar(bar.getId(), session.getId(), bill.getId());

        assertEquals(bill, resBill);
    }

    @Test
    @DisplayName("Get not existing Bill of bar")
    void getNotExistingBillOfBar() {
       assertThrows(
               NotFoundException.class,
               () -> service.getBillOfBar(bar.getId(), session.getId(), 999L)
       );
    }

    @Test
    @DisplayName("Get not existing Bill of bar")
    void getBillOfNotExistingSessionOfBar() {
        assertThrows(
                NotFoundException.class,
                () -> service.getBillOfBar(bar.getId(), 999L, bill.getId())
        );
    }

    @Test
    @DisplayName("Get Bills of bar")
    void getBillsOfBar() throws NotFoundException {
        List<Bill> bills = service.getAllBillsOfSession(bar.getId(), session.getId());

        assertEquals(1, bills.size());
        assertTrue(bills.contains(bill));
    }

    @Test
    @DisplayName("Get Bill of session empty")
    void getBillsOfSessionEmpty() {
        assertThrows(
                NotFoundException.class,
                () -> service.getBillOfBar(bar.getId(), session.getId(), session.getId())
        );
    }

    @Test
    @DisplayName("Get Bills of person")
    void getBillsOfPerson() throws NotFoundException {
        List<Bill> bills = service.getBillsOfPerson(bar.getId(), person.getId());

        assertEquals(1, bills.size());
        assertTrue(bills.contains(bill));
    }

    @Test
    @DisplayName("Get Bill of person")
    void getBillOfPerson() throws NotFoundException {
        Bill resBill = service.getBillOfPerson(bar.getId(), person.getId(), bill.getId());

        assertEquals(bill, resBill);
    }

    @Test
    @DisplayName("Get Bill of person")
    void getNotExistingBillOfPerson() {
        assertThrows(
                NotFoundException.class,
                () -> service.getBillOfPerson(bar.getId(), person.getId(), 999L)
        );
    }

    @Test
    @DisplayName("create Bill")
    void createBill() throws NotFoundException {
        Person person2 = personRepository.save(new PersonBuilder().build());
        bar.addUser(person2);
        barRepository.save(bar);
        BillRequest request = new BillRequest();
        request.customerId = person2.getId();

        Bill resBill = service.createNewBillForSession(bar.getId(), session.getId(), request);

        assertEquals(person2, resBill.getCustomer());
    }

    @Test
    @DisplayName("create Bill already existing")
    void createBillDuplicate() {
        BillRequest request = new BillRequest();
        request.customerId = person.getId();

        assertThrows(
            DuplicateRequestException.class,
            () -> service.createNewBillForSession(bar.getId(), session.getId(), request)
        );
    }

    @Test
    @DisplayName("Set is payed of bill")
    void setIsPayedOfBill() throws NotFoundException {
        Bill resBill = service.setIsPayedOfBillOfSession(bar.getId(), session.getId(), bill.getId(), true);

        assertTrue(resBill.isPayed());
    }

    @Test
    @DisplayName("Set is payed of bill")
    void setIsPayedFalseOfBill() throws NotFoundException {
        Bill resBill = service.setIsPayedOfBillOfSession(bar.getId(), session.getId(), bill.getId(), false);

        assertFalse(resBill.isPayed());
    }

    @Test
    @DisplayName("Set is payed null of bill ")
    void setIsPayedNullOfBill(){
        assertThrows(
            IllegalArgumentException.class,
            () -> service.setIsPayedOfBillOfSession(bar.getId(), session.getId(), bill.getId(), null)
        );
    }

    @Test
    @DisplayName("Delete bill")
    void deleteBill() throws NotFoundException {
        service.deleteBillFromSessionOfBar(bar.getId(), session.getId(), bill.getId());

        assertTrue(repository.findById(bill.getId()).isEmpty());
    }

    @Test
    @DisplayName("Remove order from bill")
    void removeOrderFromBill() throws NotFoundException {
        bill.addOrder(new ProductBuilder().build(), 1, person);
        bill = repository.save(bill);
        Order order = bill.getOrders().get(0);

        service.removeOrderFromBill(bill, order);

        assertFalse(repository.getOne(bill.getId()).getOrders().contains(order));
    }

}