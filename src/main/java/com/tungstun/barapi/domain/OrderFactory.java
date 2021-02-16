package com.tungstun.barapi.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderFactory {
    private LocalDateTime date;
    private Bartender bartender;

    public OrderFactory(LocalDateTime date, Bartender bartender) {
        this.date = date;
        this.bartender = bartender;
    }

    public Order create(){
        return new Order(
                this.date,
                0,
                this.bartender,
                new ArrayList<>()
        );
    }

}
