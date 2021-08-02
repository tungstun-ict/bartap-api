package com.tungstun.barapi.presentation.converter;

import com.tungstun.barapi.domain.stock.Stock;
import com.tungstun.barapi.presentation.dto.response.StockResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockConverter {
    public StockResponse convert(Stock stock) {
        StockResponse response =  new StockResponse();
        response.setAmount(stock.getAmount());
        return response;
    }

    public List<StockResponse> convertAll(List<Stock> stocks) {
        return stocks.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
