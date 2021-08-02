package com.tungstun.barapi.presentation.converter;

import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.dto.response.BillSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillConverter {
    private final OrderConverter orderConverter;

    public BillConverter(OrderConverter orderConverter) {
        this.orderConverter = orderConverter;
    }

    public BillResponse convert(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setPayed(bill.isPayed());
        response.setTotalPrice(bill.calculateTotalPrice());
        response.setOrders(orderConverter.convertAll(bill.getOrders()));
        response.setCustomer(bill.getCustomer());
        response.setSession(bill.getSession());
        return response;
    }
    public List<BillResponse> convertAll(List<Bill> bills) {
        return bills.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public BillSummaryResponse convertToSummary(Bill bill) {
        BillSummaryResponse response = new BillSummaryResponse();
        response.setId(bill.getId());
        response.setPayed(bill.isPayed());
        response.setTotalPrice(bill.calculateTotalPrice());
        response.setSession(bill.getSession());
        return response;
    }
    public List<BillSummaryResponse> convertAllToSummary(List<Bill> bills) {
        return bills.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
}
