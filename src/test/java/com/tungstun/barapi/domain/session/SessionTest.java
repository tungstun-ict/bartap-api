package com.tungstun.barapi.domain.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private static LocalDateTime getTruncatedCurrentDateTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    static Stream<Arguments> provideSessionsToCheckIsActive() {
        Session session = Session.create(123L, "session");
        Session session2 = Session.create(123L, "session");
        session2.end();
        Session session3 = Session.create(123L, "session");
        session3.end();
        session3.lock();
        return Stream.of(
                Arguments.of(session, true),
                Arguments.of(session2, false),
                Arguments.of(session3, false)
        );
    }

//    static Stream<Arguments> provideSessionsToLock() {
//        Session session = Session.create(123L, "session");
//        Session session2 = Session.create(123L, "session");
//        session2.end();
//        return Stream.of(
//                Arguments.of(session),
//                Arguments.of(session2)
//        );
//    }
//    @ParameterizedTest
//    @MethodSource("provideSessionsToLock")
//    @DisplayName("Lock session")
//    void lockSession(Session session) {
//        session.lock();
//
//        assertTrue(session.isLocked());
//        assertNotNull(session.getEndDate());
//    }

    @Test
    @DisplayName("End session ends session")
    void endSession_EndsSession() {
        Session session = Session.create(123L, "session");
        LocalDateTime currentDateTime = getTruncatedCurrentDateTime();

        boolean ended = session.end();

        assertTrue(ended);
        LocalDateTime sessionEndDateTime = session.getEndDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(currentDateTime, sessionEndDateTime);
    }

    @Test
    @DisplayName("End session when session is already ended")
    void endSession_WhenSessionIsEndedAlready() {
        Session session = Session.create(123L, "session");
        session.end();

        assertThrows(
                IllegalStateException.class,
                () -> session.end()
        );
    }
    @ParameterizedTest
    @MethodSource("provideSessionsToCheckIsActive")
    @DisplayName("Check if session is active session")
    void sessionIsActive(Session session, boolean expectedIsActive) {
        boolean isActive = session.isActive();

        assertEquals(isActive, expectedIsActive);
    }
}