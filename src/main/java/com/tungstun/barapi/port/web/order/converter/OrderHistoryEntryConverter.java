package com.tungstun.barapi.port.web.order.converter;

import com.tungstun.barapi.domain.bill.OrderHistoryEntry;
import com.tungstun.barapi.port.web.order.response.OrderHistoryEntryResponse;
import com.tungstun.barapi.port.web.person.converter.PersonConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHistoryEntryConverter {
    private final PersonConverter personConverter;

    public OrderHistoryEntryConverter(PersonConverter personConverter) {
        this.personConverter = personConverter;
    }

    public OrderHistoryEntryResponse convert(OrderHistoryEntry entry) {
        return new OrderHistoryEntryResponse(
                entry.getId(),
                entry.getType().toString(),
                entry.getDate(),
                entry.getProductId(),
                entry.getProductName(),
                entry.getAmount(),
                personConverter.convert(entry.getCustomer()),
                personConverter.convert(entry.getBartender())
        );
    }
    public List<OrderHistoryEntryResponse> convertAll(List<OrderHistoryEntry> orders) {
        return orders.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
