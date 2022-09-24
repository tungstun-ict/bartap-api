package com.tungstun.statistics.domain.statistics.filter;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionToDateFilterTest {
    private Session session;

    @BeforeEach
    void setUp() {
        session = new SessionFactory("session")
                .create();
    }

    @Test
    @DisplayName("Session is before date tests true")
    void testOnTomorrowTrue() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        SessionToDateFilter predicate = new SessionToDateFilter(tomorrow);

        boolean result = predicate.test(session);

        assertTrue(result);
    }

    @Test
    @DisplayName("Session is on date tests true")
    void testOnTodayTrue() {
        LocalDate today = LocalDate.now();
        SessionToDateFilter predicate = new SessionToDateFilter(today);

        boolean result = predicate.test(session);

        assertTrue(result);
    }

    @Test
    @DisplayName("Session is after date tests false")
    void testAgainstYesterdayTrue() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        SessionToDateFilter predicate = new SessionToDateFilter(yesterday);

        boolean result = predicate.test(session);

        assertFalse(result);
    }
}