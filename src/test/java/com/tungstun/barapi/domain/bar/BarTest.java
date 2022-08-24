package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BarTest {
    private Bar bar;

    @BeforeEach
    void setUp() {
        bar = new BarBuilder("bar")
                .setPhoneNumber("+31612345678")
                .setMail("mail@testmail.com")
                .setAddress("adressBar 1")
                .build();
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
        Session session =  bar.newSession("test");
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
}