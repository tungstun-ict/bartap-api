package com.tungstun.statistics.port.web.statistics;

import com.tungstun.security.domain.user.User;
import com.tungstun.statistics.application.statistics.StatisticsQueryHandler;
import com.tungstun.statistics.application.statistics.query.GetBarStatistics;
import com.tungstun.statistics.application.statistics.query.GetCustomerStatistics;
import com.tungstun.statistics.application.statistics.query.GetGlobalCustomerStatistics;
import com.tungstun.statistics.domain.statistics.Statistics;
import com.tungstun.statistics.port.web.statistics.converter.StatisticsConverter;
import com.tungstun.statistics.port.web.statistics.response.StatisticsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class StatisticsController {
    private final StatisticsQueryHandler statisticsQueryHandler;
    private final StatisticsConverter statisticsConverter;

    public StatisticsController(StatisticsQueryHandler statisticsQueryHandler, StatisticsConverter statisticsConverter) {
        this.statisticsQueryHandler = statisticsQueryHandler;
        this.statisticsConverter = statisticsConverter;
    }

    @GetMapping("/bars/{barId}/statistics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Finds bar statistics",
            description = "Find statistics of a bar like popular drinks and other statistics",
            tags = "Statistics"
    )
    public StatisticsResponse getBarStatistics(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId
    ) throws EntityNotFoundException {
        Statistics statistics = statisticsQueryHandler.handle(new GetBarStatistics(barId));
        return statisticsConverter.convert(statistics);
    }

    @GetMapping("/bars/{barId}/people/{personId}/statistics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Finds customer's statistics",
            description = "Find customer statistics of a bar that the authenticated user is owner of",
            tags = "Statistics"
    )
    public StatisticsResponse getCustomerStatisticsOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(description = "Id value of the customer") @PathVariable UUID personId
    ) throws EntityNotFoundException {
        Statistics statistics = statisticsQueryHandler.handle(new GetCustomerStatistics(barId, personId));
        return statisticsConverter.convert(statistics);
    }

    @GetMapping("/bars/{barId}/customer-statistics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER', 'CUSTOMER'})")
    @Operation(
            summary = "Finds customer statistics",
            description = "Find multiple customer statistics of a bar that the authenticated user is connected to",
            tags = "Statistics"
    )
    public StatisticsResponse getCustomerStatistics(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        User user = (User) authentication.getPrincipal();
        Statistics statistics = statisticsQueryHandler.handle(new GetCustomerStatistics(barId, user.getId()));
        return statisticsConverter.convert(statistics);
    }

    @GetMapping("/global-customer-statistics")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Finds global customer statistics",
            description = "Find multiple customer statistics that span all connected bars tha the authenticated user is connected to",
            tags = "Statistics"
    )
    public StatisticsResponse getGlobalCustomerStatistics(
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Statistics statistics = statisticsQueryHandler.handle(new GetGlobalCustomerStatistics(userDetails.getUsername()));
        return statisticsConverter.convert(statistics);
    }
}
