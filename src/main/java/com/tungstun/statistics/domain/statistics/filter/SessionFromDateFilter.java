package com.tungstun.statistics.domain.statistics.filter;

import com.tungstun.barapi.domain.session.Session;

import java.time.LocalDate;
import java.util.function.Predicate;

public record SessionFromDateFilter(LocalDate date) implements Predicate<Session> {
    @Override
    public boolean test(Session session) {
        LocalDate sessionDate = session.getCreationDate().toLocalDate();
        return sessionDate.isAfter(date) || sessionDate.isEqual(date);
    }
}
