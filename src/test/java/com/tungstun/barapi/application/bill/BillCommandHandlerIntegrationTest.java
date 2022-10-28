package com.tungstun.barapi.application.bill;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bill.command.AddCustomerToSession;
import com.tungstun.barapi.application.bill.command.DeleteBill;
import com.tungstun.barapi.application.bill.command.PayBill;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.bill.SpringBillRepository;
import com.tungstun.barapi.port.persistence.session.SpringSessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BillCommandHandlerIntegrationTest {
    @Autowired
    private SpringBillRepository repository;
    @Autowired
    private SpringSessionRepository sessionRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private BillCommandHandler billCommandHandler;

    private Bar bar;
    private Bill bill;
    private Session session;
    private Person person;

    @BeforeEach
    void setup() {
        session = new SessionFactory("session").create();
        person = new PersonBuilder("person").build();
        bar = new BarBuilder("bar")
                .setSessions(List.of(session))
                .setPeople(List.of(person))
                .build();
        bill = session.addCustomer(person);
        bar = barRepository.save(bar);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        sessionRepository.deleteAll();
        barRepository.deleteAll();
    }

    @Test
    @DisplayName("create Bill")
    void createBill() throws EntityNotFoundException {
        Person person2 = bar.createPerson("name2");
        barRepository.save(bar);

        AddCustomerToSession command = new AddCustomerToSession(bar.getId(), session.getId(), person2.getId());

        assertDoesNotThrow(() -> billCommandHandler.handle(command));
    }

    @Test
    @DisplayName("create Bill already existing")
    void createBillDuplicate() {
        AddCustomerToSession command = new AddCustomerToSession(bar.getId(), session.getId(), person.getId());

        assertThrows(
                DuplicateRequestException.class,
                () -> billCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Set is payed of bill")
    void setIsPayedOfBill() throws EntityNotFoundException {
        PayBill command = new PayBill(bar.getId(), session.getId(), bill.getId());

        billCommandHandler.handle(command);

        Bill resBill = repository.findById(bill.getId()).orElseThrow();
        assertTrue(resBill.isPayed());
    }

    @Test
    @DisplayName("Delete bill")
    void deleteBill() throws EntityNotFoundException {
        DeleteBill command = new DeleteBill(bar.getId(), session.getId(), bill.getId());

        billCommandHandler.handle(command);

        assertTrue(repository.findById(bill.getId()).isEmpty());
    }
}