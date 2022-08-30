package com.tungstun.barapi.application.bill;

import com.tungstun.barapi.application.bill.query.GetBill;
import com.tungstun.barapi.application.bill.query.ListBillsOfCustomer;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class BillQueryHandlerIntegrationTest {
    @Autowired
    private SpringBillRepository repository;
    @Autowired
    private SpringSessionRepository sessionRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private BillQueryHandler billQueryHandler;

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
    @DisplayName("Get Bills of customer")
    void getBillsOfCustomer() throws EntityNotFoundException {
        bar = barRepository.findById(bar.getId()).orElseThrow();
        bar.getActiveSession().end();
        session = bar.newSession("test2");
        session.addCustomer(person);
        bar = barRepository.save(bar);

        List<Bill> resBill = billQueryHandler.handle(new ListBillsOfCustomer(bar.getId(), person.getId()));

        assertEquals(2, resBill.size());
    }

    @Test
    @DisplayName("Get Bill of bar")
    void getBillOfBar() throws EntityNotFoundException {
        Bill resBill = billQueryHandler.handle(new GetBill(bar.getId(), session.getId(), bill.getId()));

        assertEquals(bill.getId(), resBill.getId());
    }

    @Test
    @DisplayName("Get not existing Bill of bar")
    void getNotExistingBillOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> billQueryHandler.handle(new GetBill(bar.getId(), session.getId(), UUID.randomUUID()))
        );
    }

    @Test
    @DisplayName("Get not existing Bill of bar")
    void getBillOfNotExistingSessionOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> billQueryHandler.handle(new GetBill(bar.getId(), UUID.randomUUID(), bill.getId()))
        );
    }
}
