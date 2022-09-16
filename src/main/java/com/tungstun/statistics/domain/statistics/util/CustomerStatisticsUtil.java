package com.tungstun.statistics.domain.statistics.util;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.bill.OrderProduct;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomerStatisticsUtil {
    public static Double totalAmountSpent(List<Bill> bills) {
        return bills.parallelStream()
                .filter(Bill::isPayed)
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    public static Double totalAmountNotYetPayed(List<Bill> bills) {
        return bills.parallelStream()
                .filter(bill -> !bill.isPayed())
                .mapToDouble(Bill::calculateTotalPrice)
                .sum();
    }

    public static Bill highestPricedBill(List<Bill> bills) {
        return bills.parallelStream()
                .reduce((bill, bill2) -> bill.calculateTotalPrice() > bill2.calculateTotalPrice()
                        ? bill
                        : bill2
                )
                .orElse(null);
    }

    public static OrderProduct favoriteProduct(List<Bill> bills) {
        return bills.parallelStream()
                .map(Bill::getOrders)
                .flatMap(List::stream)
                .map(Order::getProduct)
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
