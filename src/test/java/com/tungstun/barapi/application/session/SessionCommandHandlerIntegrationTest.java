package com.tungstun.barapi.application.session;

import com.tungstun.barapi.application.session.command.CreateSession;
import com.tungstun.barapi.application.session.command.DeleteSession;
import com.tungstun.barapi.application.session.command.EndSession;
import com.tungstun.barapi.application.session.command.UpdateSession;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.bill.SpringBillRepository;
import com.tungstun.barapi.port.persistence.person.SpringPersonRepository;
import com.tungstun.barapi.port.persistence.session.SpringSessionRepository;
import com.tungstun.exception.DuplicateActiveSessionException;
import com.tungstun.exception.InvalidSessionStateException;
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
    private SessionCommandHandler serviceCommandHandler;

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
    @DisplayName("Create new session")
    void createSession() throws EntityNotFoundException {
        session.end();
        repository.save(session);
        CreateSession command = new CreateSession(bar.getId(), "new");

        assertDoesNotThrow(() -> serviceCommandHandler.handle(command));
    }

    @Test
    @DisplayName("Create new session while active")
    void createSessionWhileActive() {
        CreateSession command = new CreateSession(bar.getId(), "new");

        assertThrows(
                DuplicateActiveSessionException.class,
                () -> serviceCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Update session")
    void updateSession() throws EntityNotFoundException {
        UpdateSession command = new UpdateSession(bar.getId(), session.getId(), "newTest");

        assertDoesNotThrow(() -> serviceCommandHandler.handle(command));
    }

    @Test
    @DisplayName("Delete session")
    void deleteSession() throws EntityNotFoundException {
        DeleteSession command = new DeleteSession(session.getId());

        serviceCommandHandler.handle(command);

        assertTrue(repository.findById(session.getId()).isEmpty());
    }

    @Test
    @DisplayName("End session")
    void endSession() throws EntityNotFoundException {
        EndSession command = new EndSession(bar.getId(), session.getId());

        serviceCommandHandler.handle(command);

        assertNotNull(repository.getById(session.getId()).getEndDate());
    }

    @Test
    @DisplayName("End ended session throws")
    void endEndedSessionThrows() {
        session.end();
        repository.save(session);
        EndSession command = new EndSession(bar.getId(), session.getId());

        assertThrows(
                InvalidSessionStateException.class,
                () -> serviceCommandHandler.handle(command)
        );
    }
}
