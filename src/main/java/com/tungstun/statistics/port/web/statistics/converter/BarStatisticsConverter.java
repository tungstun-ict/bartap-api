package com.tungstun.statistics.port.web.statistics.converter;

import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.order.converter.OrderProductConverter;
import com.tungstun.statistics.domain.statistics.model.BarStatistics;
import com.tungstun.statistics.port.web.statistics.response.BarStatisticsResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarStatisticsConverter {
    private final BillConverter billConverter;
    private final OrderProductConverter orderProductConverter;

    public BarStatisticsConverter(BillConverter billConverter, OrderProductConverter orderProductConverter) {
        this.billConverter = billConverter;
        this.orderProductConverter = orderProductConverter;
    }

    public BarStatisticsResponse convert(BarStatistics barStatistics) {
        return new BarStatisticsResponse(
                orderProductConverter.convert(barStatistics.mostSoldProduct()),
                billConverter.convertToSummary(barStatistics.mostExpensiveBill()),
                barStatistics.totalSpent(),
                barStatistics.totalNotYetPayed()
        );
    }

    public List<BarStatisticsResponse> convertAll(List<BarStatistics> barStatisticsList) {
        return barStatisticsList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
