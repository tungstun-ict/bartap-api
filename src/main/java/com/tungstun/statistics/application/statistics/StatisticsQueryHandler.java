package com.tungstun.statistics.application.statistics;

import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListConnectedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.statistics.application.statistics.query.GetBarStatistics;
import com.tungstun.statistics.application.statistics.query.GetCustomerStatistics;
import com.tungstun.statistics.application.statistics.query.GetGlobalCustomerStatistics;
import com.tungstun.statistics.application.statistics.query.GetUserCustomerStatistics;
import com.tungstun.statistics.domain.statistics.Filters;
import com.tungstun.statistics.domain.statistics.Statistics;
import com.tungstun.statistics.domain.statistics.StatisticsGenerator;
import com.tungstun.statistics.domain.statistics.filter.BillFromUserWithIdFilter;
import com.tungstun.statistics.domain.statistics.filter.BillFromUserWithUsernameFilter;
import org.springframework.stereotype.Service;

@Service
public class StatisticsQueryHandler {
    private final BarQueryHandler barQueryHandler;

    public StatisticsQueryHandler(BarQueryHandler barQueryHandler) {
        this.barQueryHandler = barQueryHandler;
    }

    public Statistics handle(GetBarStatistics query, Filters filters) {
        StatisticsGenerator statisticsGenerator = new StatisticsGenerator(filters);
        Bar bar = barQueryHandler.handle(new GetBar(query.barId()));
        statisticsGenerator.addBar(bar);

        return statisticsGenerator.generate();
    }

    public Statistics handle(GetCustomerStatistics query, Filters filters) {
        Bar bar = barQueryHandler.handle(new GetBar(query.barId()));

        return new StatisticsGenerator(filters)
                .addBar(bar)
                .addBillFilter(bill -> bill.getCustomer().getId().equals(query.customerId()))
                .generate();
    }

    public Statistics handle(GetUserCustomerStatistics query, Filters filters) {
        Bar bar = barQueryHandler.handle(new GetBar(query.barId()));

        return new StatisticsGenerator(filters)
                .addBar(bar)
                .addBillFilter(new BillFromUserWithIdFilter(query.userId()))
                .generate();
    }

    public Statistics handle(GetGlobalCustomerStatistics query, Filters filters) {
        StatisticsGenerator statisticsGenerator = new StatisticsGenerator(filters);

        barQueryHandler.handle(new ListConnectedBars(query.username()))
                .forEach(statisticsGenerator::addBar);

        return statisticsGenerator
                .addBillFilter(new BillFromUserWithUsernameFilter(query.username()))
                .generate();
    }
}
