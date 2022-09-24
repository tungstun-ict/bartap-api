package com.tungstun.statistics.domain.statistics.filter;

import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionFromDateFilterTest {
    private Session session;

    @BeforeEach
    void setUp() {
        session = new SessionFactory("session")
                .create();
    }

    @Test
    @DisplayName("Session is before date tests false")
    void testOnTomorrowTrue() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        SessionFromDateFilter predicate = new SessionFromDateFilter(tomorrow);

        boolean result = predicate.test(session);

        assertFalse(result);
    }

    @Test
    @DisplayName("Session is on date tests true")
    void testOnTodayTrue() {
        LocalDate today = LocalDate.now();
        SessionFromDateFilter predicate = new SessionFromDateFilter(today);

        boolean result = predicate.test(session);

        assertTrue(result);
    }

    @Test
    @DisplayName("Session is after date tests true")
    void testAgainstYesterdayTrue() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        SessionFromDateFilter predicate = new SessionFromDateFilter(yesterday);

        boolean result = predicate.test(session);

        assertTrue(result);
    }
}
