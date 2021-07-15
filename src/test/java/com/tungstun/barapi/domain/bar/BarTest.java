package com.tungstun.barapi.domain.bar;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.DuplicateActiveSessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarTest {
    private Bar bar;

    @BeforeEach
    void setup() {
        bar = new BarBuilder()
                .setName("bar")
                .setPhoneNumber("0600000000")
                .setMail("mail@testmail.com")
                .setAddress("adressBar 1")
                .build();
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
        Session session = bar.activeSession();

        assertNull(session);
    }

    @Test
    @DisplayName("bar active session")
    void activeSession() {
        Session session = bar.newSession("test");

        Session resSession = bar.activeSession();

        assertEquals(session, resSession);
    }
}