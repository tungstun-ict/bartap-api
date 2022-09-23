package com.tungstun.statistics.application.statistics;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListConnectedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.statistics.application.statistics.query.GetBarStatistics;
import com.tungstun.statistics.application.statistics.query.GetCustomerStatistics;
import com.tungstun.statistics.application.statistics.query.GetGlobalCustomerStatistics;
import com.tungstun.statistics.domain.statistics.StatisticsGenerator;
import com.tungstun.statistics.domain.statistics.model.Statistics;
import org.springframework.stereotype.Service;

@Service
public class StatisticsQueryHandler {
    private final BarQueryHandler barQueryHandler;

    public StatisticsQueryHandler(BarQueryHandler barQueryHandler) {
        this.barQueryHandler = barQueryHandler;
    }

    public Statistics handle(GetBarStatistics query) {
        Bar bar = barQueryHandler.handle(new GetBar(query.barId()));

        return new StatisticsGenerator()
                .addBar(bar)
                .generate();
    }

    public Statistics handle(GetCustomerStatistics query) {
        Bar bar = barQueryHandler.handle(new GetBar(query.barId()));

        return new StatisticsGenerator()
                .addBar(bar)
                .addBillFilter(bill -> bill.getCustomer().getId().equals(query.customerId()))
                .generate();
    }

    public Statistics handle(GetGlobalCustomerStatistics query) {
        StatisticsGenerator statisticsGenerator = new StatisticsGenerator();

        barQueryHandler.handle(new ListConnectedBars(query.username()))
                .forEach(statisticsGenerator::addBar);

        return statisticsGenerator
                .addBillFilter(bill -> bill.getCustomer().getUser() != null)
                .addBillFilter(bill -> bill.getCustomer().getUser().getUsername().equals(query.username()))
                .generate();
    }
}
