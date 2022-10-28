package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.exception.DuplicateActiveSessionException;
import com.tungstun.security.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BarTest {
    private Bar bar;
    private User user;

    @BeforeEach
    void setUp() {
        bar = new BarBuilder("bar")
                .setPhoneNumber("+31612345678")
                .setMail("mail@testmail.com")
                .setAddress("adressBar 1")
                .build();
        user = new User(
                UUID.randomUUID(),
                "name",
                "",
                "",
                "",
                "mail@mail.m",
                "+31612345876",
                new ArrayList<>()
        );

    }

    @Test
    @DisplayName("new session")
    void createBar_Successfully() {
        assertDoesNotThrow(
                () -> new BarBuilder("bar")
                        .setPhoneNumber("+31612345678")
                        .setMail("mail@testmail.com")
                        .setAddress("adressBar 1")
                        .build()
        );
    }


    @Test
    @DisplayName("new session")
    void newSession() {
        assertDoesNotThrow(() -> bar.newSession("test"));
    }

    @Test
    @DisplayName("new session")
    void duplicateNewSession() {
        bar.newSession("test");

        assertThrows(
                DuplicateActiveSessionException.class,
                () -> bar.newSession("test")
        );
    }

    @Test
    @DisplayName("new session returns session")
    void newSessionReturnsSession() {
        Session session = bar.newSession("test");
        assertNotNull(session);
    }

    @Test
    @DisplayName("bar active session")
    void noActiveSession() {
        assertThrows(
                EntityNotFoundException.class,
                bar::getActiveSession
        );
    }

    @Test
    @DisplayName("bar active session")
    void activeSession() {
        Session session = bar.newSession("test");

        Session resSession = bar.getActiveSession();

        assertEquals(session, resSession);
    }

    @Test
    @DisplayName("Connect user to person")
    void connectUserToPerson() {
        Person person = bar.createPerson("person");

        assertDoesNotThrow(() -> bar.connectUserToPerson(user, person.getId()));

        assertEquals(user, person.getUser());
    }

    @Test
    @DisplayName("Connect already connected to other person user throws")
    void connectUserToPerson_WhenUserIsAlreadyConnected_Throws() {
        bar.createPerson("person")
                .connectUser(user, bar.getId());
        Person person = bar.createPerson("person2");

        assertThrows(
                IllegalArgumentException.class,
                () -> bar.connectUserToPerson(user, person.getId())
        );
    }

    @Test
    @DisplayName("Connect user to person already connected to same user throws")
    void connectUserToPersonAlreadyConnectedToUser_Throws() {
        Person person = bar.createPerson("person");
        person.connectUser(user, bar.getId());

        assertThrows(
                IllegalArgumentException.class,
                () -> bar.connectUserToPerson(user, person.getId())
        );
    }

    @Test
    @DisplayName("Connect user to person already connected to other user throws")
    void connectUserToPersonAlreadyConnectedToOtherUser_Throws() {
        Person person = bar.createPerson("person");
        person.connectUser(user, bar.getId());
        User user2 = new User(UUID.randomUUID(),"name2", "", "", "", "mail@mail.mm", "+31612345876", new ArrayList<>());


        assertThrows(
                IllegalStateException.class,
                () -> bar.connectUserToPerson(user2, person.getId())
        );
    }

    @Test
    @DisplayName("Connect user to not existing person throws")
    void connectUserToNotExistingPerson_Throws() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bar.connectUserToPerson(user, UUID.randomUUID())
        );
    }
}