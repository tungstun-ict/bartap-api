package com.tungstun.barapi.application.bill;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bill.command.AddCustomerToSession;
import com.tungstun.barapi.application.bill.command.DeleteBill;
import com.tungstun.barapi.application.bill.command.PayBill;
import com.tungstun.barapi.application.bill.command.PayBills;
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
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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
    private Bill bill2;
    private Session session;
    private Person person;
    private Person person2;

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of(LocalDate.now().plusDays(1)),                  // Up to future data
                Arguments.of(LocalDate.now()),                              // During active session after ended sessions
                Arguments.of(LocalDate.now().minusDays(5)),                 // Between Active session and last ended session
                Arguments.of(LocalDate.now().minusDays(6))   // Halfway during ended session
        );
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

    @BeforeEach
    void setup() throws IllegalAccessException {
        session = new SessionFactory("session").create();
        Session session2 = new SessionFactory("session2").create();
        Session session3 = new SessionFactory("session3").create();

        person = new PersonBuilder("person").build();
        person2 = new PersonBuilder("person2").build();
        bar = new BarBuilder("bar")
                .setSessions(List.of(session3, session2, session))
                .setPeople(List.of(person2, person))
                .build();

        bill = session.addCustomer(person);
        bill2 = session2.addCustomer(person);
        session2.end();
        Bill bill3 = session3.addCustomer(person);
        bill3.pay();
        session3.end();

        //Set session end dates back
        FieldUtils.writeField(session2, "creationDate", LocalDateTime.now().minusDays(7), true);
        FieldUtils.writeField(session2, "endDate", LocalDateTime.now().minusDays(6), true);
        FieldUtils.writeField(session3, "creationDate", LocalDateTime.now().minusDays(14), true);
        FieldUtils.writeField(session3, "endDate", LocalDateTime.now().minusDays(13), true);

        bar = barRepository.save(bar);
    }

    // Helper function for payBills tests
    private void assertAllEndedBillsPaid(List<Bill> bills) {
        assertTrue(bills.stream()
                .filter(b -> b.getSession().isEnded())
                .allMatch(Bill::isPayed)
        );
    }

    private void assertActiveBillNotPaid(List<Bill> bills) {
        assertFalse(bills.stream()
                .filter(b -> b.getSession().isActive())
                .allMatch(Bill::isPayed)
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    @DisplayName("")
    void payUpToCorrectDatesSuccessful(LocalDate successfulDate) {
        PayBills command = new PayBills(bar.getId(), person.getId(), successfulDate);

        billCommandHandler.handle(command);

        var bills = repository.findByBarAndPerson(bar.getId(), person.getId());
        assertAllEndedBillsPaid(bills);
        assertActiveBillNotPaid(bills);
    }

    @Test
    @DisplayName("Pay up to date before last paid bill successful")
    void payUpToBeforeLastPaidSuccessful() {
        PayBills command = new PayBills(bar.getId(), person.getId(), LocalDate.now().minusDays(8));

        billCommandHandler.handle(command);

        var bills = repository.findByBarAndPerson(bar.getId(), person.getId());
        assertActiveBillNotPaid(bills);
        assertEquals(1, bills.stream().filter(Bill::isPayed).count());
    }

    @Test
    @DisplayName("Pay bills when only active session is not paid")
    void payWhenAllArePaidExceptActiveSession() {
        PayBills command = new PayBills(bar.getId(), person.getId(), LocalDate.now());
        repository.findById(bill2.getId())
                .map(b -> {
                    b.pay();
                    return b;
                })
                .ifPresent(repository::save);

        billCommandHandler.handle(command);

        var bills = repository.findByBarAndPerson(bar.getId(), person.getId());
        assertActiveBillNotPaid(bills);
        assertAllEndedBillsPaid(bills);
    }

    @Test
    @DisplayName("Pay bills all bills are paid")
    void payWhenAllArePaid() {
        PayBills command = new PayBills(bar.getId(), person.getId(), LocalDate.now());
        var barr = barRepository.findById(bar.getId()).orElseThrow();
        barr.getSessions()
                .stream()
                .peek(s -> {
                    if (s.isActive()) s.end();
                })
                .map(Session::getBills)
                .flatMap(List::stream)
                .forEach(Bill::pay);
        barRepository.save(bar);

        billCommandHandler.handle(command);

        var bills = repository.findByBarAndPerson(bar.getId(), person.getId());
        assertAllEndedBillsPaid(bills);
    }

    @Test
    @DisplayName("Pay bills when no bills are present for customer")
    void payWhenNoBillsExistSuccessful() {
        PayBills command = new PayBills(bar.getId(), person2.getId(), LocalDate.now());

        assertDoesNotThrow(() -> billCommandHandler.handle(command));
    }
}
