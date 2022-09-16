package com.tungstun.barapi.port.web.statistics;

import com.tungstun.barapi.application.statistics.StatisticsQueryHandler;
import com.tungstun.barapi.application.statistics.model.BarStatistics;
import com.tungstun.barapi.application.statistics.model.CustomerStatistics;
import com.tungstun.barapi.application.statistics.model.GlobalCustomerStatistics;
import com.tungstun.barapi.application.statistics.query.GetBarStatistics;
import com.tungstun.barapi.application.statistics.query.GetCustomerStatistics;
import com.tungstun.barapi.application.statistics.query.GetGlobalCustomerStatistics;
import com.tungstun.barapi.port.web.statistics.converter.BarStatisticsConverter;
import com.tungstun.barapi.port.web.statistics.converter.CustomerStatisticsConverter;
import com.tungstun.barapi.port.web.statistics.converter.GlobalCustomerStatisticsConverter;
import com.tungstun.barapi.port.web.statistics.response.BarStatisticsResponse;
import com.tungstun.barapi.port.web.statistics.response.CustomerStatisticsResponse;
import com.tungstun.barapi.port.web.statistics.response.GlobalCustomerStatisticsResponse;
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
    private final CustomerStatisticsConverter customerStatisticsConverter;
    private final GlobalCustomerStatisticsConverter globalCustomerStatisticsConverter;
    private final BarStatisticsConverter barStatisticsConverter;

    public StatisticsController(StatisticsQueryHandler statisticsQueryHandler, CustomerStatisticsConverter customerStatisticsConverter, GlobalCustomerStatisticsConverter globalCustomerStatisticsConverter, BarStatisticsConverter barStatisticsConverter) {
        this.statisticsQueryHandler = statisticsQueryHandler;
        this.customerStatisticsConverter = customerStatisticsConverter;
        this.globalCustomerStatisticsConverter = globalCustomerStatisticsConverter;
        this.barStatisticsConverter = barStatisticsConverter;
    }

    @GetMapping("/bars/{barId}/statistics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Finds bar statistics",
            description = "Find statistics of a bar like popular drinks and other statistics",
            tags = "Statistics"
    )
    public BarStatisticsResponse getBarStatistics(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId
    ) throws EntityNotFoundException {
        BarStatistics barStatistics = statisticsQueryHandler.handle(new GetBarStatistics(barId));
        return barStatisticsConverter.convert(barStatistics);
    }

    @GetMapping("/bars/{barId}/customer-statistics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER', 'CUSTOMER'})")
    @Operation(
            summary = "Finds customer statistics",
            description = "Find multiple customer statistics of a bar that the authenticated user is connected to",
            tags = "Statistics"
    )
    public CustomerStatisticsResponse getCustomerStatistics(
            @Parameter(description = "Id value of the bar") @PathVariable UUID barId,
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CustomerStatistics customerStatistics = statisticsQueryHandler.handle(new GetCustomerStatistics(barId, userDetails.getUsername()));
        return customerStatisticsConverter.convert(customerStatistics);
    }

    @GetMapping("/global-customer-statistics")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Finds global customer statistics",
            description = "Find multiple customer statistics that span all connected bars tha the authenticated user is connected to",
            tags = "Statistics"
    )
    public GlobalCustomerStatisticsResponse getGlobalCustomerStatistics(
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        GlobalCustomerStatistics globalCustomerStatistics = statisticsQueryHandler.handle(new GetGlobalCustomerStatistics(userDetails.getUsername()));
        return globalCustomerStatisticsConverter.convert(globalCustomerStatistics);
    }
}