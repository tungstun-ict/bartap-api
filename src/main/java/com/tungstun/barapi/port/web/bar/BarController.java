package com.tungstun.barapi.port.web.bar;

import com.tungstun.barapi.application.bar.BarCommandHandler;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.command.CreateBar;
import com.tungstun.barapi.application.bar.command.DeleteBar;
import com.tungstun.barapi.application.bar.command.UpdateBar;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListOwnedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.port.web.bar.converter.BarConverter;
import com.tungstun.barapi.port.web.bar.request.CreateBarRequest;
import com.tungstun.barapi.port.web.bar.request.UpdateBarRequest;
import com.tungstun.barapi.port.web.bar.response.BarResponse;
import com.tungstun.common.response.UuidResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bars")
public class BarController {
    private final BarQueryHandler barQueryHandler;
    private final BarCommandHandler barCommandHandler;
    private final BarConverter converter;

    public BarController(BarQueryHandler barQueryHandler, BarCommandHandler barCommandHandler, BarConverter converter) {
        this.barQueryHandler = barQueryHandler;
        this.barCommandHandler = barCommandHandler;
        this.converter = converter;
    }

    @GetMapping("owned")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Finds all owned bars",
            description = "Finds all bars that are owned by the logged in user"
    )
    public List<BarResponse> getAllBarOwnerBars(@Parameter(hidden = true) Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Bar> allBars = barQueryHandler.handle(new ListOwnedBars(userDetails.getUsername()));
        return converter.convertAll(allBars);
    }

    @GetMapping("/{barId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Finds bar by id",
            description = "Finds bar with the given id"
    )
    public BarResponse getBar(
            @Parameter(description = "Id value of the bar you want to retrieve") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        return converter.convert(bar);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates a bar",
            description = "Creates a new bar with the given information"
    )
    public UuidResponse addBar(
            @Valid @RequestBody CreateBarRequest request,
            @Parameter(hidden = true) Authentication authentication
    ) {
        CreateBar command = new CreateBar(
                request.address(),
                request.name(),
                request.mail(),
                request.phoneNumber(),
                ((UserDetails) authentication.getPrincipal()).getUsername()
        );
        return new UuidResponse(barCommandHandler.handle(command));
    }

    @PutMapping("/{barId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Updates bar",
            description = "Update bar with id's details with the given information"
    )
    public UuidResponse updateBar(
            @Parameter(description = "Id value of the bar you want to update") @PathVariable("barId") UUID barId,
            @Valid @RequestBody UpdateBarRequest request
    ) throws EntityNotFoundException {
        UpdateBar command = new UpdateBar(
                barId,
                request.address(),
                request.name(),
                request.mail(),
                request.phoneNumber()
        );
        return new UuidResponse(barCommandHandler.handle(command));
    }


    @DeleteMapping("/{barId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Deletes a bar",
            description = "Delete bar with th given id"
    )
    public void deleteBar(
            @Parameter(description = "Id value for the bar you want to delete") @PathVariable("barId") UUID barId
    ) {
        DeleteBar command = new DeleteBar(barId);
        barCommandHandler.handle(command);
    }
}
