package com.tungstun.barapi.port.web.bill.converter;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.port.web.bill.response.BillResponse;
import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.barapi.port.web.order.converter.OrderConverter;
import com.tungstun.barapi.port.web.person.converter.PersonConverter;
import com.tungstun.barapi.port.web.session.converter.SessionConverter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillConverter {
    private final OrderConverter orderConverter;
    private final SessionConverter sessionConverter;
    private final PersonConverter personConverter;

    public BillConverter(OrderConverter orderConverter, @Lazy SessionConverter sessionConverter, PersonConverter personConverter) {
        this.orderConverter = orderConverter;
        this.sessionConverter = sessionConverter;
        this.personConverter = personConverter;
    }

    public BillResponse convert(Bill bill) {
        return new BillResponse(
                bill.getId(),
                bill.isPayed(),
                personConverter.convert(bill.getCustomer()),
                bill.calculateTotalPrice(),
                orderConverter.convertAll(bill.getOrders())
        );
    }

    public List<BillResponse> convertAll(List<Bill> bills) {
        return bills.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public BillSummaryResponse convertToSummary(Bill bill) {
        return new BillSummaryResponse(
                bill.getId(),
                bill.isPayed(),
                bill.calculateTotalPrice(),
                sessionConverter.convertToSummary(bill.getSession())
        );
    }

    public List<BillSummaryResponse> convertAllToSummary(List<Bill> bills) {
        return bills.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
}
