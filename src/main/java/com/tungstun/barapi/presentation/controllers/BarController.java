package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.BarService;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.barapi.presentation.dto.response.BarResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bars")
@RolesAllowed("ROLE_BAR_OWNER")
public class BarController {
    private final BarService BAR_SERVICE;

    public BarController(BarService BAR_SERVICE) {this.BAR_SERVICE = BAR_SERVICE;}

    /**
     * Converts Bar object to a BarResult object ready for request response
     * @param bar bar to be converted
     * @return BarResult
     */
    private BarResponse convertToBarResult(Bar bar){
        return new ResponseMapper().convert(bar, BarResponse.class);
    }

    @GetMapping
    public ResponseEntity<List<BarResponse>> getAllBars() throws NotFoundException {
        List<Bar> allBars = this.BAR_SERVICE.getAllBars();
        List<BarResponse> barResponses = new ArrayList<>();
        for (Bar bar : allBars) barResponses.add(convertToBarResult(bar));
        return new ResponseEntity<>(barResponses, HttpStatus.OK);
    }

    @GetMapping("/{barId}")
    public ResponseEntity<BarResponse> getBar(@PathVariable("barId") Long id ) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(id);
        return new ResponseEntity<>(convertToBarResult(bar), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<BarResponse> addBar(
            @Valid @RequestBody BarRequest barRequest) {
        Bar bar = this.BAR_SERVICE.addBar(
                barRequest.adres,
                barRequest.name,
                barRequest.mail,
                barRequest.phoneNumber
        );
        return new ResponseEntity<>(convertToBarResult(bar), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<BarResponse> editBar(
            @PathVariable("id") Long id,
            @RequestBody BarRequest barRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.editBar(
                id,
                barRequest.adres,
                barRequest.name,
                barRequest.mail,
                barRequest.phoneNumber
        );
        return new ResponseEntity<>(convertToBarResult(bar), HttpStatus.OK);
    }

    @DeleteMapping("/{barId}")
    public ResponseEntity<BarResponse> deleteBar(@PathVariable("barId") Long id ) throws NotFoundException {
        this.BAR_SERVICE.deleteBar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
