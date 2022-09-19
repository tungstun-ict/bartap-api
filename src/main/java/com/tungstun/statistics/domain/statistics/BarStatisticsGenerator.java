package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.statistics.domain.statistics.model.BarStatistics;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BarStatisticsGenerator {
    private final Bar bar;

    private Predicate<Session> filters;

    public BarStatisticsGenerator(Bar bar) {
        this.bar = bar;
        this.filters = (b) -> true;
    }

    public void addFilter(Predicate<Session> predicate) {
        filters = filters.and(predicate);
    }

    private OrderProduct mostSoldProduct;
    private Bill mostExpensiveBill;
    private double totalSpent;
    private double totalNotYetPayed;

    public BarStatistics generate() {
        List<Bill> bills = bar.getSessions()
                .parallelStream()
                .filter(filters)
                .map(Session::getBills)
                .flatMap(List::stream)
                .collect(Collectors.toList());


        mostSoldProduct(bills);
        mostExpensiveBill(bills);
        totalAmountSpent(bills);
        totalAmountNotYetPayed(bills);

        return new BarStatistics(
                mostSoldProduct,
                mostExpensiveBill,
                totalSpent,
                totalNotYetPayed
        );
    }

    // todo bills
    public void mostExpensiveBill(List<Bill> bills) {
        mostExpensiveBill = bills.parallelStream()
                .reduce((bill, bill2) -> bill.calculateTotalPrice() > bill2.calculateTotalPrice()
                        ? bill
                        : bill2
                )
                .orElse(null);
    }

    // TODO: 16-9-2022 bills
    public void totalAmountSpent(List<Bill> bills) {
        totalSpent = bills.parallelStream()
                .filter(Bill::isPayed)
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    // TODO: 16-9-2022 bills
    public void totalAmountNotYetPayed(List<Bill> bills) {
        totalNotYetPayed = bills.parallelStream()
                .filter(bill -> !bill.isPayed())
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    //    todo: Products
    public void mostSoldProduct(List<Bill> bills) {
        var products = bills.parallelStream()
                .map(Bill::getOrders)
                .flatMap(List::stream)
                .map(Order::getProduct)
                .toList();
        // Split due too exception when chained as single chain
        mostSoldProduct = products.stream()
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
