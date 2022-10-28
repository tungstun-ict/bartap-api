package com.tungstun.statistics.port.web.statistics.converter;

import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.barapi.port.web.order.converter.OrderProductConverter;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;
import com.tungstun.statistics.domain.statistics.Statistics;
import com.tungstun.statistics.port.web.statistics.response.StatisticsResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StatisticsConverter {
    private final BillConverter billConverter;
    private final OrderProductConverter orderProductConverter;

    public StatisticsConverter(BillConverter billConverter, OrderProductConverter orderProductConverter) {
        this.billConverter = billConverter;
        this.orderProductConverter = orderProductConverter;
    }

    public StatisticsResponse convert(Statistics statistics) {
        OrderProductResponse mostSoldProduct = Optional.ofNullable(statistics.mostSoldProduct())
                .map(orderProductConverter::convert)
                .orElse(null);
        BillSummaryResponse mostExpensiveBill = Optional.ofNullable(statistics.mostExpensiveBill())
                .map(billConverter::convertToSummary)
                .orElse(null);

        return new StatisticsResponse(
                mostSoldProduct,
                mostExpensiveBill,
                statistics.totalSpent(),
                statistics.totalNotYetPayed()
        );
    }

    public List<StatisticsResponse> convertAll(List<Statistics> statisticsList) {
        return statisticsList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
