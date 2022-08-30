package com.tungstun.barapi.application;

import com.tungstun.barapi.application.session.SessionCommandHandler;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.command.CreateSession;
import com.tungstun.barapi.application.session.command.UpdateSession;
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
class SessionCommandHandlerIntegrationTest {
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringBillRepository billRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringSessionRepository repository;
    @Autowired
    private SessionCommandHandler service;
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
        CreateSession command = new CreateSession(bar.getId(), "new");

        assertDoesNotThrow(() -> service.createNewSession(command));
    }

    @Test
    @DisplayName("Create new session while active")
    void createSessionWhileActive() {
        CreateSession command = new CreateSession(bar.getId(), "new");

        assertThrows(
                DuplicateActiveSessionException.class,
                () -> service.createNewSession(command)
        );
    }

    @Test
    @DisplayName("Update session")
    void updateSession() throws EntityNotFoundException {
        UpdateSession command = new UpdateSession(bar.getId(), session.getId(), "newTest");

        assertDoesNotThrow(() -> service.updateSession(command));
    }

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
