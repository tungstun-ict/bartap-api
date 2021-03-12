package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.BarService;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.barapi.presentation.dto.response.BarResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import com.tungstun.security.data.model.UserProfile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bars")
public class BarController {
    private final BarService BAR_SERVICE;

    public BarController(BarService BAR_SERVICE) {this.BAR_SERVICE = BAR_SERVICE;}

    private BarResponse convertToBarResult(Bar bar){
        return new ResponseMapper().convert(bar, BarResponse.class);
    }

    @GetMapping("/all")
    @PreAuthorize("hasPermission(-1L, 'NO_ONE_ALLOWED')")
    @ApiOperation(
            value = "Finds all bars",
            notes = "Look up a all existing bars",
            response = BarResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<BarResponse>> getAllBars() throws NotFoundException {
        List<Bar> allBars = this.BAR_SERVICE.getAllBars();
        List<BarResponse> barResponses = new ArrayList<>();
        for (Bar bar : allBars) barResponses.add(convertToBarResult(bar));
        return new ResponseEntity<>(barResponses, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(
            value = "Finds all bars owned by user",
            notes = "Look up a all owned bars",
            response = BarResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<BarResponse>> getAllBarOwnerBars(
            @ApiIgnore Authentication authentication
    ) throws NotFoundException {
        UserProfile userProfile = (UserProfile) authentication.getPrincipal();
        List<Bar> allBars = this.BAR_SERVICE.getAllBarOwnerBars(userProfile.getUsername());
        List<BarResponse> barResponses = new ArrayList<>();
        for (Bar bar : allBars) barResponses.add(convertToBarResult(bar));
        return new ResponseEntity<>(barResponses, HttpStatus.OK);
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
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return new ResponseEntity<>(convertToBarResult(bar), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(
            value = "Creates a new bar",
            notes = "Provide bar information in the request body to create a new bar",
            response = BarResponse.class
    )
    public ResponseEntity<BarResponse> addBar(
            @Valid @RequestBody BarRequest barRequest,
            Authentication authentication
    ) {
        Bar bar = this.BAR_SERVICE.addBar(
                barRequest,
                ((UserProfile) authentication.getPrincipal()).getUsername()
        );
        return new ResponseEntity<>(convertToBarResult(bar), HttpStatus.CREATED);
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
            @Valid @RequestBody BarRequest barRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.updateBar(barId, barRequest);
        return new ResponseEntity<>(convertToBarResult(bar), HttpStatus.OK);
    }


    @DeleteMapping("/{barId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes a bar",
            notes = "Provide id to delete a specific bar"
    )
    public ResponseEntity<BarResponse> deleteBar(
            @ApiParam(value = "ID value for the bar you want to delete") @PathVariable("barId") Long barId
    ) throws NotFoundException {
        this.BAR_SERVICE.deleteBar(barId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
