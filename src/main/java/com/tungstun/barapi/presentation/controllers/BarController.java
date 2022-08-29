package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.BarService;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.presentation.dto.converter.BarConverter;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.barapi.presentation.dto.response.BarResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bars")
public class BarController {
    private final BarService barService;
    private final BarConverter converter;

    public BarController(BarService barService, BarConverter converter) {
        this.barService = barService;
        this.converter = converter;
    }

    @GetMapping
    @ApiOperation(
            value = "Finds all bars owned by user",
            notes = "Look up a all owned bars",
            response = BarResponse.class
    )
    public ResponseEntity<List<BarResponse>> getAllBarOwnerBars(@ApiIgnore Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Bar> allBars = this.barService.getAllBarOwnerBars(userDetails.getUsername());
        return new ResponseEntity<>(converter.convertAll(allBars), HttpStatus.OK);
    }

    @GetMapping("/{barId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds bar by id",
            notes = "Provide id to look up a specific bar",
            response = BarResponse.class
    )
    public ResponseEntity<BarResponse> getBar(
            @ApiParam(value = "ID value for the bar you want to retrieve") @PathVariable("barId") Long barId
    ) throws NotFoundException {
        Bar bar = this.barService.getBar(barId);
        return new ResponseEntity<>(converter.convert(bar), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(
            value = "Creates a new bar",
            notes = "Provide bar information in the request body to create a new bar",
            response = BarResponse.class
    )
    public ResponseEntity<BarResponse> addBar(
            @Valid @RequestBody BarRequest barRequest,
            @ApiIgnore Authentication authentication
    ) {
        Bar bar = this.barService.addBar(barRequest, ((UserDetails) authentication.getPrincipal()).getUsername());
        return new ResponseEntity<>(converter.convert(bar), HttpStatus.CREATED);
    }

    @PutMapping("/{barId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Updates bar",
            notes = "Provide id of bar to update the bar with bar information in the request body",
            response = BarResponse.class
    )
    public ResponseEntity<BarResponse> updateBar(
            @ApiParam(value = "ID value for the bar you want to update") @PathVariable("barId") Long barId,
            @Valid @RequestBody BarRequest barRequest
    ) throws NotFoundException {
        Bar bar = this.barService.updateBar(barId, barRequest);
        return new ResponseEntity<>(converter.convert(bar), HttpStatus.OK);
    }


    @DeleteMapping("/{barId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes a bar",
            notes = "Provide id to delete a specific bar"
    )
    public ResponseEntity<BarResponse> deleteBar(
            @ApiParam(value = "ID value for the bar you want to delete") @PathVariable("barId") Long barId
    ) {
        this.barService.deleteBar(barId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
