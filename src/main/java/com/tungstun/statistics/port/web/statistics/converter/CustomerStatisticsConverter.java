package com.tungstun.statistics.port.web.statistics.converter;

import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.order.converter.OrderProductConverter;
import com.tungstun.statistics.domain.statistics.CustomerStatistics;
import com.tungstun.statistics.port.web.statistics.response.CustomerStatisticsResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerStatisticsConverter {
    private final BillConverter billConverter;
    private final OrderProductConverter orderProductConverter;

    public CustomerStatisticsConverter(BillConverter billConverter, OrderProductConverter orderProductConverter) {
        this.billConverter = billConverter;
        this.orderProductConverter = orderProductConverter;
    }

    public CustomerStatisticsResponse convert(CustomerStatistics customerStatistics) {
        return new CustomerStatisticsResponse(
                customerStatistics.totalSpent(),
                customerStatistics.totalNotYetPayed(),
                billConverter.convertToSummary(customerStatistics.highestPricedBill()),
                orderProductConverter.convert(customerStatistics.favoriteProduct())
        );
    }

    public List<CustomerStatisticsResponse> convertAll(List<CustomerStatistics> customerStatisticsList) {
        return customerStatisticsList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
