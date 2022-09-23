package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.statistics.domain.statistics.filter.SessionFromDateFilter;
import com.tungstun.statistics.domain.statistics.filter.SessionToDateFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StatisticsGenerator {
    private final List<Session> sessions;
    private Predicate<Session> sessionFilters;
    private Predicate<Bill> billFilters;

    public StatisticsGenerator(Filters filters) {
        sessions = new ArrayList<>();
        this.sessionFilters = (s) -> true;
        this.billFilters = (b) -> true;

        if (filters.from() != null) {
            sessionFilters = sessionFilters.and(new SessionFromDateFilter(filters.from()));
        }
        if (filters.to() != null) {
            sessionFilters = sessionFilters.and(new SessionToDateFilter(filters.to()));
        }
    }

    public StatisticsGenerator addBar(Bar bar) {
        sessions.addAll(bar.getSessions());
        return this;
    }

    public StatisticsGenerator addSessionFilter(Predicate<Session> predicate) {
        sessionFilters = sessionFilters.and(predicate);
        return this;
    }

    public StatisticsGenerator addBillFilter(Predicate<Bill> predicate) {
        billFilters = billFilters.and(predicate);
        return this;
    }

    public Statistics generate() {
        List<Bill> bills = sessions
                .parallelStream()
                .filter(sessionFilters)
                .map(Session::getBills)
                .flatMap(List::stream)
                .filter(billFilters)
                .collect(Collectors.toList());


        return new Statistics(
                mostSoldProduct(bills),
                mostExpensiveBill(bills),
                totalAmountSpent(bills),
                totalAmountNotYetPayed(bills)
        );
    }

    private Double totalAmountSpent(List<Bill> bills) {
        return bills.parallelStream()
                .filter(Bill::isPayed)
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    private Double totalAmountNotYetPayed(List<Bill> bills) {
        return bills.parallelStream()
                .filter(bill -> !bill.isPayed())
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    private Bill mostExpensiveBill(List<Bill> bills) {
        return bills.parallelStream()
                .reduce((bill, bill2) -> bill.calculateTotalPrice() > bill2.calculateTotalPrice()
                        ? bill
                        : bill2
                )
                .orElse(null);
    }

    private OrderProduct mostSoldProduct(List<Bill> bills) {
        List<OrderProduct> products = bills.parallelStream()
                .map(Bill::getOrders)
                .flatMap(List::stream)
                .map(Order::getProduct)
                .toList();
        // Split due too exception when chained as single chain
        return products.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .parallelStream()
                .reduce((entry, entry2) -> entry.getValue() > entry2.getValue()
                        ? entry
                        : entry2
                ).map(Map.Entry::getKey)
                .orElse(null);
    }
}
