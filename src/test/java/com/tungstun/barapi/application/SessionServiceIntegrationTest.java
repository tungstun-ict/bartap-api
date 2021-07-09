package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.BillFactory;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
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
class SessionServiceIntegrationTest {
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringBillRepository billRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringSessionRepository repository;
    @Autowired
    private SessionService service;

    private Bar bar;
    private Bill bill;
    private Session session;
    private Person person;

    @BeforeEach
    void setup() {
        bar = new BarBuilder().build();
        session = Session.create("test");
        session = repository.save(session);
        person = personRepository.save(new PersonBuilder().build());
        bill = billRepository.save(new BillFactory(session, person).create());
        person.addBill(bill);
        session.addBill(bill);
        session = repository.save(session);
        bar.addSession(session);
        bar.addUser(person);
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Get all sessions of bar")
    void getSessionsOfbar() throws NotFoundException {
        List<Session> resSessions = service.getAllSessionsOfBar(bar.getId());

        assertEquals(1, resSessions.size());
        assertTrue(resSessions.contains(session));
    }

    @Test
    @DisplayName("Get session of bar")
    void getSession() throws NotFoundException {
        Session resSession = service.getSessionOfBar(bar.getId(), session.getId());

        assertEquals(session, resSession);
    }

    @Test
    @DisplayName("Get not existing session of bar")
    void getNotExistingSession() {
        assertThrows(
                NotFoundException.class,
                () -> service.getSessionOfBar(bar.getId(), 999L)
        );
    }

    @Test
    @DisplayName("Get active session of bar")
    void getActiveSession() throws NotFoundException {
        Session resSession = service.getActiveSessionOfBar(bar.getId());
        assertEquals(session, resSession);
    }

    @Test
    @DisplayName("Get not existing active session of bar")
    void getNotExistingActiveSession() {
        session.lock();
        repository.save(session);
        assertThrows(
                NotFoundException.class,
                () -> service.getActiveSessionOfBar(bar.getId())
        );
    }

    @Test
    @DisplayName("Create new session")
    void createSession() throws NotFoundException {
        session.lock();
        repository.save(session);
        SessionRequest request = new SessionRequest();
        request.name = "new";

        Session resSession = service.createNewSession(bar.getId(), request);

        assertEquals("new", resSession.getName());
    }

    @Test
    @DisplayName("Create new session while active")
    void createSessionWhileActive() {
        SessionRequest request = new SessionRequest();
        request.name = "new";

        assertThrows(
                DuplicateActiveSessionException.class,
                () -> service.createNewSession(bar.getId(), request)
        );
    }

    @Test
    @DisplayName("Update session")
    void updateSession() throws NotFoundException {
        SessionRequest request = new SessionRequest();
        request.name = "newTest";

        Session resSession = service.updateSession(bar.getId(), session.getId(), request);

        assertEquals("newTest", resSession.getName());
    }

    @Test
    @DisplayName("Update locked session throws")
    void updateLockedSessionThrows() {
        session.endSession();
        repository.save(session);
        SessionRequest request = new SessionRequest();
        request.name = "newTest";

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.updateSession(bar.getId(), session.getId(), request)
        );
    }

    @Test
    @DisplayName("End session")
    void endSession() throws NotFoundException {
        Session resSession = service.endSession(bar.getId(), session.getId());

        assertNotNull(resSession.getClosedDate());
    }

    @Test
    @DisplayName("End locked session throws")
    void endLockedSessionThrows() {
        session.lock();
        repository.save(session);

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.endSession(bar.getId(), session.getId())
        );
    }

    @Test
    @DisplayName("Lock session")
    void lockSession() throws NotFoundException {
        Session resSession = service.lockSession(bar.getId(), session.getId());

        assertNotNull(resSession.getClosedDate());
        assertTrue(resSession.isLocked());
        assertFalse(resSession.isActive());
    }

    @Test
    @DisplayName("Lock locked session throws")
    void lockLockedSessionThrows() {
        session.lock();
        repository.save(session);

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.lockSession(bar.getId(), session.getId())
        );
    }

    @Test
    @DisplayName("Session is editable")
    void sessionIsEditable() {
        assertDoesNotThrow(() -> service.checkEditable(session));
    }

    @Test
    @DisplayName("Session is active editable")
    void sessionIsActiveEditable() {
        session.endSession();
        repository.save(session);

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.checkEditable(session)
        );
    }

    @Test
    @DisplayName("Session is not editable")
    void sessionIsNotEditable() {
        session.lock();
        repository.save(session);

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.checkEditable(session)
        );
    }
}