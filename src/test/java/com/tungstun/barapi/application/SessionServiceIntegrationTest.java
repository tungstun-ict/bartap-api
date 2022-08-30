package com.tungstun.barapi.application;

import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.SessionService;
import com.tungstun.barapi.application.session.query.GetActiveSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.bill.SpringBillRepository;
import com.tungstun.barapi.port.persistence.person.SpringPersonRepository;
import com.tungstun.barapi.port.persistence.session.SpringSessionRepository;
import com.tungstun.barapi.presentation.dto.request.SessionRequest;
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
    @Autowired
    private SessionQueryHandler serviceQueryHandler;

    private Bar bar;
    private Session session;

    @BeforeEach
    void setup() {
        Person person = new PersonBuilder("name").build();
        session = new SessionFactory("test").create();
        session.addCustomer(person);
        bar = new BarBuilder("bar")
                .setPeople(List.of(person))
                .setSessions(List.of(session))
                .build();
//        bar = barRepository.save(bar);
//        bar = barRepository.getById(bar.getId());
//        session = bar.newSession("test");
//        session = repository.save(session);
//        session.addCustomer(person);
//        person.addBill(bill);
//        session = repository.save(session);
//        bar.addSession(session);
//        bar.addPerson(person);
        bar = barRepository.save(bar);
    }

    @AfterEach
    void tearDown() {
        barRepository.deleteAll();
        repository.deleteAll();
        billRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @DisplayName("Get all sessions of bar")
    void getSessionsOfBar() throws EntityNotFoundException {
        List<Session> resSessions = serviceQueryHandler.handle(new ListSessionsOfBar(bar.getId()));

        assertEquals(1, resSessions.size());
        boolean idMatches = resSessions.stream().anyMatch(session1 -> session1.getId().equals(session.getId()));
        assertTrue(idMatches);
    }

    @Test
    @DisplayName("Get session of bar")
    void getSession() throws EntityNotFoundException {
        Session resSession = serviceQueryHandler.handle(new GetSession(session.getId(), bar.getId()));

        assertEquals(session.getId(), resSession.getId());
    }

    @Test
    @DisplayName("Get not existing session of bar")
    void getNotExistingSession() {
        assertThrows(
                EntityNotFoundException.class,
                () -> serviceQueryHandler.handle(new GetSession(UUID.randomUUID(), bar.getId()))
        );
    }

    @Test
    @DisplayName("Get active session of bar")
    void getActiveSession() throws EntityNotFoundException {
        Session resSession = serviceQueryHandler.handle(new GetActiveSession(bar.getId()));
        assertEquals(session.getId(), resSession.getId());
    }

    @Test
    @DisplayName("Get not existing active session of bar")
    void getNotExistingActiveSession() {
        session.end();
        repository.save(session);
        assertThrows(
                EntityNotFoundException.class,
                () -> serviceQueryHandler.handle(new GetActiveSession(bar.getId()))
        );
    }


    @Test
    @DisplayName("Create new session")
    void createSession() throws EntityNotFoundException {
        session.end();
        repository.save(session);
        SessionRequest request = new SessionRequest();
        request.name = "new";

        assertDoesNotThrow(() -> service.createNewSession(bar.getId(), request));
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
    void updateSession() throws EntityNotFoundException {
        SessionRequest request = new SessionRequest();
        request.name = "newTest";

        assertDoesNotThrow(() -> service.updateSession(bar.getId(), session.getId(), request));
    }

    //Commented because editing only name(now) isnt bad per se after end
//    @Test
//    @DisplayName("Update locked session throws")
//    void updateLockedSessionThrows() {
//        session.end();
//        repository.save(session);
//        SessionRequest request = new SessionRequest();
//        request.name = "newTest";
//
//        assertThrows(
//                InvalidSessionStateException.class,
//                () -> service.updateSession(bar.getId(), session.getId(), request)
//        );
//    }

    @Test
    @DisplayName("Delete session")
    void deleteSession() throws EntityNotFoundException {
        SessionRequest request = new SessionRequest();
        request.name = "newTest";

        service.deleteSession(session.getId());

        assertTrue(repository.findById(session.getId()).isEmpty());
    }

    @Test
    @DisplayName("End session")
    void endSession() throws EntityNotFoundException {
        service.endSession(bar.getId(), session.getId());

        assertNotNull(repository.getById(session.getId()).getEndDate());
    }

//    Commented because lock method ending session seems double in functionality
//    @Test
//    @DisplayName("End locked session throws")
//    void endLockedSessionThrows() {
//        session.lock();
//        repository.save(session);
//
//        assertThrows(
//                InvalidSessionStateException.class,
//                () -> service.endSession(bar.getId(), session.getId())
//        );
//    }

//    Commented because lock method ending session seems double in functionality
//    @Test
//    @DisplayName("Lock session")
//    void lockSession() throws EntityNotFoundException {
//        service.lockSession(bar.getId(), session.getId());
//
//        Session resSession = repository.getById(session.getId());
//        assertNotNull(resSession.getEndDate());
//        assertTrue(resSession.isLocked());
//        assertFalse(resSession.isActive());
//    }

    @Test
    @DisplayName("End ended session throws")
    void endEndedSessionThrows() {
        session.end();
        repository.save(session);

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.endSession(bar.getId(), session.getId())
        );
    }
}