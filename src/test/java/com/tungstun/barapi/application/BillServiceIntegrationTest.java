package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.BillService;
import com.tungstun.barapi.application.bill.query.GetBill;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.bill.SpringBillRepository;
import com.tungstun.barapi.port.persistence.session.SpringSessionRepository;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BillServiceIntegrationTest {
    @Autowired
    private SpringBillRepository repository;
    @Autowired
    private SpringSessionRepository sessionRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private BillService service;
    @Autowired
    private BillQueryHandler billQueryHandler;

    private Bar bar;
    private Bill bill;
    private Session session;
    private Person person;

    @BeforeEach
    void setup() {
        bar = new BarBuilder("bar").build();
//        bar = barRepository.save(bar);
//        bar = barRepository.getById(bar.getId());
        person = bar.createPerson("name");
//        person = personRepository.save(new PersonBuilder("name").build());
        session = bar.newSession("test");
        bill = session.addCustomer(person);
//        session = sessionRepository.save(session);

//        bill = repository.save(new BillFactory(session, person).create());
//        person.addBill(bill);
//        session.addBill(bill);
//        session = sessionRepository.save(session);
//        bar.addSession(session);
//        bar.addPerson(person);
        bar = barRepository.save(bar);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        sessionRepository.deleteAll();
        barRepository.deleteAll();
    }
//    @Test
//    @DisplayName("Get All Bills")
//    void getAllBills() throws EntityNotFoundException {
//        List<Bill> resBills = billQueryHandler.handle(new ListBillsOfBar(bar.getId()));
//
//        assertEquals(1, resBills.size());
//        assertTrue(resBills.contains(bill));
//    }

//    @Test
//    @DisplayName("Get All Bills when none")
//    void getAllBillsWhenNone() throws EntityNotFoundException {
//        session.removeBill(bill);
//        sessionRepository.save(session);
//
//        List<Bill> resBills = billQueryHandler.handle(new ListBillsOfBar(bar.getId()));
//
//        assertEquals(0, resBills.size());
//    }

    @Test
    @DisplayName("Get Bill of bar")
    void getBillOfBar() throws EntityNotFoundException {
        Bill resBill = billQueryHandler.handle(new GetBill(bill.getId(), session.getId(), bar.getId()));

        assertEquals(bill.getId(), resBill.getId());
    }

    @Test
    @DisplayName("Get not existing Bill of bar")
    void getNotExistingBillOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> billQueryHandler.handle(new GetBill(UUID.randomUUID(), session.getId(), bar.getId()))
        );
    }

    @Test
    @DisplayName("Get not existing Bill of bar")
    void getBillOfNotExistingSessionOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> billQueryHandler.handle(new GetBill(bill.getId(), UUID.randomUUID(), bar.getId()))
        );
    }

//    @Test
//    @DisplayName("Get Bills of bar")
//    void getBillsOfBar() throws EntityNotFoundException {
//        List<Bill> bills = service.getAllBillsOfSession(bar.getId(), session.getId());
//
//        assertEquals(1, bills.size());
//        assertTrue(bills.contains(bill));
//    }

//    @Test
//    @DisplayName("Get Bill of customer from active session")
//    void getBillOfCustomerFromActiveSession() throws EntityNotFoundException {
//        Bill bill = service.getBillOfCustomerFromActiveSession(bar.getId(), person.getId());
//
//        assertEquals(bill.getId(), bill.getId());
//    }

//    @Test
//    @DisplayName("Get Bill of customer from active session when no session is active")
//    void getBillOfCustomerFromActiveSessionWhenNoActiveSession() {
//        session.lock();
//        session = sessionRepository.save(session);
//
//        assertThrows(
//                EntityNotFoundException.class,
//                () ->service.getBillOfCustomerFromActiveSession(bar.getId(), person.getId())
//        );
//    }

//    @Test
//    @DisplayName("Get Bill of customer from active session when person does not have bill in session")
//    void getBillOfCustomerFromActiveSessionWhenNoBillExists() {
//        assertThrows(
//                EntityNotFoundException.class,
//                () ->service.getBillOfCustomerFromActiveSession(bar.getId(), 999L)
//        );
//    }

//    @Test
//    @DisplayName("Get Bills of person")
//    void getBillsOfPerson() throws EntityNotFoundException {
//        List<Bill> bills =  billQueryHandler.handle(new ListBillsOfCustomer(bar.getId(), person.getId()));
//
//        assertEquals(1, bills.size());
//        assertTrue(bills.contains(bill));
//    }

//    @Test
//    @DisplayName("Get Bill of person")
//    void getBillOfPerson() throws EntityNotFoundException {
//        Bill resBill = service.getBillOfPerson(bar.getId(), person.getId(), bill.getId());
//
//        assertEquals(bill, resBill);
//    }

//    @Test
//    @DisplayName("Get Bill of person")
//    void getNotExistingBillOfPerson() {
//        assertThrows(
//                EntityNotFoundException.class,
//                () -> service.getBillOfPerson(bar.getId(), person.getId(), 999L)
//        );
//    }

    @Test
    @DisplayName("create Bill")
    void createBill() throws EntityNotFoundException {
//        Person person2 = personRepository.save(new PersonBuilder(123L, "name").build());
//        bar.addPerson(person2);
        Person person2 = bar.createPerson("name");
        barRepository.save(bar);
        BillRequest request = new BillRequest();
        request.customerId = person2.getId();

        assertDoesNotThrow(() -> service.addCustomerToSession(bar.getId(), session.getId(), request));
    }

    @Test
    @DisplayName("create Bill already existing")
    void createBillDuplicate() {
        BillRequest request = new BillRequest();
        request.customerId = person.getId();

        assertThrows(
                DuplicateRequestException.class,
                () -> service.addCustomerToSession(bar.getId(), session.getId(), request)
        );
    }

    @Test
    @DisplayName("Set is payed of bill")
    void setIsPayedOfBill() throws EntityNotFoundException {
        service.payBill(bar.getId(), session.getId(), bill.getId());

        Bill resBill = repository.findById(bill.getId()).orElseThrow();
        assertTrue(resBill.isPayed());
    }

//    @Test
//    @DisplayName("Set is payed of bill")
//    void setIsPayedFalseOfBill() throws EntityNotFoundException {
//        Bill resBill = service.payBill(bar.getId(), session.getId(), bill.getId(), false);
//
//        assertFalse(resBill.isPayed());
//    }
//
//    @Test
//    @DisplayName("Set is payed null of bill ")
//    void setIsPayedNullOfBill() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> service.payBill(bar.getId(), session.getId(), bill.getId(), null)
//        );
//    }

    @Test
    @DisplayName("Delete bill")
    void deleteBill() throws EntityNotFoundException {
        service.deleteBill(bar.getId(), session.getId(), bill.getId());

        assertTrue(repository.findById(bill.getId()).isEmpty());
    }
}