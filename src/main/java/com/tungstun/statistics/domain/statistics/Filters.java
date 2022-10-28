package com.tungstun.statistics.domain.statistics;

import java.time.LocalDate;

public record Filters(
        LocalDate from,
        LocalDate to){
}
