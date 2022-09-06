package com.tungstun.barapi.application.statistics;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.ListConnectedBars;
import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.query.ListBillsOfCustomer;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.query.GetPersonByUserUsername;
import com.tungstun.barapi.application.statistics.model.CustomerStatistics;
import com.tungstun.barapi.application.statistics.model.CustomerStatisticsUtil;
import com.tungstun.barapi.application.statistics.model.GlobalCustomerStatistics;
import com.tungstun.barapi.application.statistics.query.GetCustomerStatistics;
import com.tungstun.barapi.application.statistics.query.GetGlobalCustomerStatistics;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import org.springframework.stereotype.Service;

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

    public CustomerStatistics handle(GetCustomerStatistics query) {
        Person person = personQueryHandler.handle(new GetPersonByUserUsername(query.barId(), query.username()));
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
