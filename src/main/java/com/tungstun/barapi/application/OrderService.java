package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final SpringOrderRepository SPRING_ORDER_REPOSITORY;

    public OrderService(SpringOrderRepository springOrderRepository) {
        this.SPRING_ORDER_REPOSITORY = springOrderRepository;
    }
}
