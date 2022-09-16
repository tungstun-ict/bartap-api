package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.statistics.domain.statistics.model.BarStatistics;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BarStatisticsGenerator {
    private final Bar bar;

    private Predicate<Session> filters;

    private Bill mostExpensiveBill;

    private OrderProduct mostSoldProduct;
    private double totalSpent;
    private double totalNotYetPayed;

    public BarStatisticsGenerator(Bar bar) {
        this.bar = bar;
        this.filters = (b) -> true;
    }

    public void addFilter(Predicate<Session> predicate) {
        filters = filters.and(predicate);
    }

    public BarStatistics generate() {
        Stream<Session> sessions = bar.getSessions()
                .parallelStream()
                .filter(filters);


        mostExpensiveBill = sessions
                .map(Session::getBills)
                .flatMap(List::stream)
                .reduce((bill, bill2) -> bill.calculateTotalPrice() > bill2.calculateTotalPrice()
                        ? bill
                        : bill2
                )
                .orElse(null);

        return null;
    }
}
