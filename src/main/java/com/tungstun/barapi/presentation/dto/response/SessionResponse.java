package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.bill.Bill;

import java.time.LocalDateTime;
import java.util.List;

public class SessionResponse {
    private Long id;
    private LocalDateTime date;
    private List<Bartender> bartenders;
    private List<Bill> bills;

    public SessionResponse() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) { this.date = date; }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Bartender> getBartenders() {
        return bartenders;
    }

    public void setBartenders(List<Bartender> bartenders) {
        this.bartenders = bartenders;
    }
}
