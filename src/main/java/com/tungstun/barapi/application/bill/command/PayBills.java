package com.tungstun.barapi.application.bill.command;

import java.time.LocalDate;
import java.util.UUID;

public record PayBills(
        UUID barId,
        UUID personId,
        LocalDate date) {
}
