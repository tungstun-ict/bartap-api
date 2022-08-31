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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    @ApiOperation(
            value = "Finds all bars owned by user",
            notes = "Look up a all owned bars",
            response = BarResponse.class
    )
    public List<BarResponse> getAllBarOwnerBars(@ApiIgnore Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Bar> allBars = barQueryHandler.handle(new ListOwnedBars(userDetails.getUsername()));
        return converter.convertAll(allBars);
    }

    @GetMapping("/{barId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @ApiOperation(
            value = "Finds bar by categoryId",
            notes = "Provide categoryId to look up a specific bar",
            response = BarResponse.class
    )
    public BarResponse getBar(
            @ApiParam(value = "ID value for the bar you want to retrieve") @PathVariable("barId") UUID barId
    ) throws EntityNotFoundException {
        Bar bar = barQueryHandler.handle(new GetBar(barId));
        return converter.convert(bar);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Creates a new bar",
            notes = "Provide bar information in the request body to create a new bar",
            response = BarResponse.class
    )
    public UuidResponse addBar(
            @Valid @RequestBody CreateBarRequest request,
            @ApiIgnore Authentication authentication
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
    @ApiOperation(
            value = "Updates bar",
            notes = "Provide categoryId of bar to update the bar with bar information in the request body",
            response = BarResponse.class
    )
    public UuidResponse updateBar(
            @ApiParam(value = "ID value for the bar you want to update") @PathVariable("barId") UUID barId,
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
    @ApiOperation(
            value = "Deletes a bar",
            notes = "Provide categoryId to delete a specific bar"
    )
    public void deleteBar(
            @ApiParam(value = "ID value for the bar you want to delete") @PathVariable("barId") UUID barId
    ) {
        DeleteBar command = new DeleteBar(barId);
        barCommandHandler.handle(command);
    }
}
