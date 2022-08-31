package com.tungstun.barapi.application.person;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.person.command.*;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.person.SpringPersonRepository;
import com.tungstun.exception.NotAuthenticatedException;
import com.tungstun.security.domain.jwt.JwtCredentials;
import com.tungstun.security.domain.jwt.JwtValidator;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.persistence.user.SpringUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PersonCommandHandlerIntegrationTest {
    @Autowired
    private SpringPersonRepository repository;
    @Autowired
    private SpringUserRepository userRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private PersonCommandHandler personCommandHandler;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private JwtCredentials jwtCredentials;

    private Bar bar;
    private Person person;

    @BeforeEach
    void setup() {
        bar = new BarBuilder("bar").build();
        person = bar.createPerson("person");
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Create person")
    void createPerson() {
        CreatePerson command = new CreatePerson(bar.getId(), "newName");

        assertDoesNotThrow(() -> personCommandHandler.handle(command));
    }

    @Test
    @DisplayName("Create existing person")
    void createExistingPerson() {
        CreatePerson command = new CreatePerson(bar.getId(), person.getName());

        assertThrows(
                DuplicateRequestException.class,
                () -> personCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Update person")
    void updatePerson() throws EntityNotFoundException {
        UpdatePerson command = new UpdatePerson(bar.getId(), person.getId(), "personUpdated");

        UUID id = personCommandHandler.handle(command);

        Person actualPerson = repository.findById(id).orElseThrow();
        assertEquals(command.name(), actualPerson.getName());
    }

    @Test
    @DisplayName("Delete person")
    void deletePerson() throws EntityNotFoundException {
        DeletePerson command = new DeletePerson(person.getId());

        personCommandHandler.handle(command);

        assertTrue(repository.findById(person.getId()).isEmpty());
    }

    @Test
    @DisplayName("Create person's connection token")
    void createPersonConnectionToken() throws EntityNotFoundException {
        CreatePersonConnectionToken command = new CreatePersonConnectionToken(bar.getId(), person.getId());

        String token = personCommandHandler.handle(command);

        DecodedJWT decodedJWT = jwtValidator.verifyToken(token);
        assertEquals(bar.getId(), UUID.fromString(decodedJWT.getClaim("bar_id").asString()));
        assertEquals(person.getId(), UUID.fromString(decodedJWT.getClaim("person_id").asString()));
    }

    @Test
    @DisplayName("Create person's connection token for person with user throws")
    void createPersonConnectionToken_ForPersonWithUser_Throws() throws EntityNotFoundException {
        User user = userRepository.save(new User(UUID.randomUUID(), "name2", "", "", "", "mail@mail.mm", "+31612345876", new ArrayList<>()));
        person.connectUser(user, bar.getId());
        person = repository.save(person);

        CreatePersonConnectionToken command = new CreatePersonConnectionToken(bar.getId(), person.getId());

        assertThrows(
                IllegalStateException.class,
                () -> personCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Connect person to user")
    void connectPersonToUser() throws EntityNotFoundException {
        User user = userRepository.save(new User(UUID.randomUUID(), "name2", "", "", "", "mail@mail.mm", "+31612345876", new ArrayList<>()));
        String token = personCommandHandler.handle(new CreatePersonConnectionToken(bar.getId(), person.getId()));
        ConnectUserToPerson command = new ConnectUserToPerson(user.getUsername(), token);

        personCommandHandler.handle(command);

        person = repository.findById(person.getId()).orElseThrow();
        assertEquals(user, person.getUser());
    }

    @Test
    @DisplayName("Connect person to user with expired token throws")
    void connectPersonToUser_WithExpiredToken_Throws() throws EntityNotFoundException {
        jwtCredentials.setJwtPersonConnectExpirationInMs(-2000L);

        User user = userRepository.save(new User(UUID.randomUUID(), "name2", "", "", "", "mail@mail.mm", "+31612345876", new ArrayList<>()));
        String token = personCommandHandler.handle(new CreatePersonConnectionToken(bar.getId(), person.getId()));
        ConnectUserToPerson command = new ConnectUserToPerson(user.getUsername(), token);

        assertThrows(
                NotAuthenticatedException.class,
                () -> personCommandHandler.handle(command)
        );
    }
}
