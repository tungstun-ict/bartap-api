package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderLineService;
import com.tungstun.barapi.domain.OrderLine;
import com.tungstun.barapi.presentation.dto.response.OrderLineResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("bars/{barId}/")
public class OrderLineController {
    private final ResponseMapper RESPONSE_MAPPER;
    private OrderLineService ORDER_LINE_SERVICE;

    public OrderLineController(OrderLineService orderLineService, ResponseMapper responseMapper) {
        this.ORDER_LINE_SERVICE = orderLineService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private OrderLineResponse convertToOrderLineResponse(OrderLine orderLine){
        return RESPONSE_MAPPER.convert(orderLine, OrderLineResponse.class);
    }

    @GetMapping("orderlines")
    public ResponseEntity<List<OrderLineResponse>> getAllBarOrderLines(
            @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<OrderLine> orderLines = this.ORDER_LINE_SERVICE.getAllOrderLinesOfBar(barId);
        List<OrderLineResponse> orderLineResponses = RESPONSE_MAPPER.convertList(orderLines, OrderLineResponse.class);
        return new ResponseEntity<>(orderLineResponses, HttpStatus.OK);
    }
}
