package com.tungstun.barapi.port.web.statistics.converter;

import com.tungstun.barapi.application.statistics.model.GlobalCustomerStatistics;
import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.order.converter.OrderProductConverter;
import com.tungstun.barapi.port.web.statistics.response.GlobalCustomerStatisticsResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GlobalCustomerStatisticsConverter {
    private final BillConverter billConverter;
    private final OrderProductConverter orderProductConverter;

    public GlobalCustomerStatisticsConverter(BillConverter billConverter, OrderProductConverter orderProductConverter) {
        this.billConverter = billConverter;
        this.orderProductConverter = orderProductConverter;
    }

    public GlobalCustomerStatisticsResponse convert(GlobalCustomerStatistics globalCustomerStatistics) {
        return new GlobalCustomerStatisticsResponse(
                globalCustomerStatistics.totalSpent(),
                globalCustomerStatistics.totalNotYetPayed(),
                billConverter.convertToSummary(globalCustomerStatistics.highestPricedBill()),
                orderProductConverter.convert(globalCustomerStatistics.favoriteProduct())
        );
    }

    public List<GlobalCustomerStatisticsResponse> convertAll(List<GlobalCustomerStatistics> globalCustomerStatisticsList) {
        return globalCustomerStatisticsList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
