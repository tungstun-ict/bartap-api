package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.domain.session.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BarStatisticsUtil {

    private static Stream<Bill> filterSessions(List<Session> sessions, Predicate<Session> predicate) {
        predicate = Optional.ofNullable(predicate)
                .orElse(s -> true);
        return sessions.parallelStream()
                .filter(predicate)
                .map(Session::getBills)
                .flatMap(List::stream);
    }

    public static OrderProduct mostSoldProduct(List<Session> sessions) {
        return BarStatisticsUtil.mostSoldProduct(sessions, s -> true);
    }

    public static OrderProduct mostSoldProduct(List<Session> sessions, Predicate<Session> predicate) {
        var products = filterSessions(sessions, predicate)
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

    public static Bill mostExpensiveBill(List<Session> sessions) {
        return BarStatisticsUtil.mostExpensiveBill(sessions, s -> true);
    }

    public static Bill mostExpensiveBill(List<Session> sessions, Predicate<Session> predicate) {
        return filterSessions(sessions, predicate)
                .reduce((bill, bill2) -> bill.calculateTotalPrice() > bill2.calculateTotalPrice()
                        ? bill
                        : bill2
                )
                .orElse(null);
    }

    public static Double totalAmountSpent(List<Session> sessions) {
        return BarStatisticsUtil.totalAmountSpent(sessions, s -> true);
    }

    public static Double totalAmountSpent(List<Session> sessions, Predicate<Session> predicate) {
        return filterSessions(sessions, predicate)
                .filter(Bill::isPayed)
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    public static Double totalAmountNotYetPayed(List<Session> sessions) {
        return BarStatisticsUtil.totalAmountNotYetPayed(sessions, s -> true);
    }

    public static Double totalAmountNotYetPayed(List<Session> sessions, Predicate<Session> predicate) {
        return filterSessions(sessions, predicate)
                .filter(bill -> !bill.isPayed())
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }
}
