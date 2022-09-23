package com.tungstun.statistics.application.statistics.query;

import java.time.LocalDate;

public record Filters(
        LocalDate from,
        LocalDate to){
}
