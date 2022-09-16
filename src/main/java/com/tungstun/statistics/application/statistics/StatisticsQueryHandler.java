package com.tungstun.statistics.application.statistics;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListConnectedBars;
import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.query.ListBillsOfCustomer;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.statistics.application.statistics.query.GetBarStatistics;
import com.tungstun.statistics.application.statistics.query.GetCustomerStatistics;
import com.tungstun.statistics.application.statistics.query.GetGlobalCustomerStatistics;
import com.tungstun.statistics.domain.statistics.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatisticsQueryHandler {
    private final BarQueryHandler barQueryHandler;
    private final PersonQueryHandler personQueryHandler;
    private final BillQueryHandler billQueryHandler;

    public StatisticsQueryHandler(BarQueryHandler barQueryHandler, PersonQueryHandler personQueryHandler, BillQueryHandler billQueryHandler) {
        this.barQueryHandler = barQueryHandler;
        this.personQueryHandler = personQueryHandler;
        this.billQueryHandler = billQueryHandler;
    }

    public BarStatistics handle(GetBarStatistics query) {
        Bar bar = barQueryHandler.handle(new GetBar(query.barId()));
        List<Session> sessions = bar.getSessions();

        LocalDateTime now = LocalDateTime.now();
        OrderProduct mostSoldProductOfLastMonth = BarStatisticsUtil.mostSoldProduct(sessions, s -> s.getCreationDate().isAfter(now.minusMonths(1)));
        OrderProduct mostSoldProductOfLastYear = BarStatisticsUtil.mostSoldProduct(sessions, s -> s.getCreationDate().isAfter(now.minusYears(1)));
        OrderProduct mostSoldProductOfAllTime = BarStatisticsUtil.mostSoldProduct(sessions);

        Bill mostExpensiveBillOfLastMonth = BarStatisticsUtil.mostExpensiveBill(sessions, s -> s.getCreationDate().isAfter(now.minusMonths(1)));
        Bill mostExpensiveBillOfLastYear = BarStatisticsUtil.mostExpensiveBill(sessions, s -> s.getCreationDate().isAfter(now.minusYears(1)));
        Bill mostExpensiveBillOfAllTime = BarStatisticsUtil.mostExpensiveBill(sessions);

        double totalSpentLastMonth = BarStatisticsUtil.totalAmountSpent(sessions, s -> s.getCreationDate().isAfter(now.minusMonths(1)));
        double totalSpentOfLastYear = BarStatisticsUtil.totalAmountSpent(sessions, s -> s.getCreationDate().isAfter(now.minusYears(1)));
        double totalSpentOfAllTime = BarStatisticsUtil.totalAmountSpent(sessions);

        double totalNotYetPayedLastMonth = BarStatisticsUtil.totalAmountNotYetPayed(sessions, s -> s.getCreationDate().isAfter(now.minusMonths(1)));
        double totalNotYetPayedLastYear = BarStatisticsUtil.totalAmountNotYetPayed(sessions, s -> s.getCreationDate().isAfter(now.minusYears(1)));
        double totalNotYetPayedAllTime = BarStatisticsUtil.totalAmountNotYetPayed(sessions);

        return new BarStatistics(
                mostSoldProductOfLastMonth,
                mostSoldProductOfLastYear,
                mostSoldProductOfAllTime,

                mostExpensiveBillOfLastMonth,
                mostExpensiveBillOfLastYear,
                mostExpensiveBillOfAllTime,

                totalSpentLastMonth,
                totalSpentOfLastYear,
                totalSpentOfAllTime,

                totalNotYetPayedLastMonth,
                totalNotYetPayedLastYear,
                totalNotYetPayedAllTime
        );
    }

    public CustomerStatistics handle(GetCustomerStatistics query) {
        Person person = personQueryHandler.handle(new GetPerson(query.barId(), query.userId()));
        List<Bill> bills = billQueryHandler.handle(new ListBillsOfCustomer(query.barId(), person.getId()));

        double totalSpent = CustomerStatisticsUtil.totalAmountSpent(bills);
        double totalNotYetPayed = CustomerStatisticsUtil.totalAmountNotYetPayed(bills);
        Bill highestPricedBill = CustomerStatisticsUtil.highestPricedBill(bills);
        OrderProduct favoriteProduct = CustomerStatisticsUtil.favoriteProduct(bills);

        return new CustomerStatistics(
                totalSpent,
                totalNotYetPayed,
                highestPricedBill,
                favoriteProduct
        );
    }

    public GlobalCustomerStatistics handle(GetGlobalCustomerStatistics query) {
        List<Bill> bills = barQueryHandler.handle(new ListConnectedBars(query.username()))
                .stream()
                .map(Bar::getSessions)
                .flatMap(List::stream)
                .map(Session::getBills)
                .flatMap(List::stream)
                .toList();

        double totalSpent = CustomerStatisticsUtil.totalAmountSpent(bills);
        double totalNotYetPayed = CustomerStatisticsUtil.totalAmountNotYetPayed(bills);
        Bill highestPricedBill = CustomerStatisticsUtil.highestPricedBill(bills);
        OrderProduct favoriteProduct = CustomerStatisticsUtil.favoriteProduct(bills);

        return new GlobalCustomerStatistics(
                totalSpent,
                totalNotYetPayed,
                highestPricedBill,
                favoriteProduct
        );
    }
}
