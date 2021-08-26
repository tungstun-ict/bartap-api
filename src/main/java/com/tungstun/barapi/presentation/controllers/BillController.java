package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.BillService;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.presentation.dto.converter.BillConverter;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.dto.response.BillSummaryResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/")
public class BillController {
    private final BillService billService;
    private final BillConverter converter;

    public BillController(BillService billService, BillConverter converter) {
        this.billService = billService;
        this.converter = converter;
    }

    @GetMapping("bills")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds all bills of bar",
            notes = "Provide id of bar to look up all bills that are linked to the bar",
            response = BillResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<BillResponse>> getAllBills(
            @ApiParam(value = "ID value for the bar you want to retrieve bills from") @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<Bill> allBills = this.billService.getAllBills(barId);
        return new ResponseEntity<>(converter.convertAll(allBills), HttpStatus.OK);
    }

    @GetMapping("/sessions/{sessionId}/bills")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds bills of session of bar",
            notes = "Provide id of bar and session to look up bills from the session from the bar",
            response = BillResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<BillResponse>> getAllBillsOfSession(
            @ApiParam(value = "ID value for the bar you want to retrieve bills from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve bills from") @PathVariable("sessionId") Long sessionId
    )throws NotFoundException {
        List<Bill> allBills = this.billService.getAllBillsOfSession(barId, sessionId);
        return new ResponseEntity<>(converter.convertAll(allBills), HttpStatus.OK);
    }

    @GetMapping("/sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds bill of bar",
            notes = "Provide id of bar, session and bill to look up the specific bill from session from the bar",
            response = BillResponse.class
    )
    public ResponseEntity<BillResponse> getBillOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bill from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve the bill from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve") @PathVariable("billId") Long billId
    ) throws NotFoundException {
        Bill bill = this.billService.getBillOfBar(barId, sessionId, billId);
        return new ResponseEntity<>(converter.convert(bill), HttpStatus.OK);
    }

    @GetMapping("/sessions/active/people/{personId}/bill")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds bill of bar",
            notes = "Provide id of bar, session and bill to look up the specific bill from session from the bar",
            response = BillResponse.class
    )
    public ResponseEntity<BillResponse> getBillOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bill from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the bill you want to retrieve") @PathVariable("personId") Long personId
    ) throws NotFoundException {
        Bill bill = this.billService.getBillOfCustomerFromActiveSession(barId, personId);
        return new ResponseEntity<>(converter.convert(bill), HttpStatus.OK);
    }

    @GetMapping("/people/{personId}/bills")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds bills of customer of bar",
            notes = "Provide id of bar and customer to look up all bills from session from the bar",
            response = BillResponse.class
    )
    public ResponseEntity<List<BillSummaryResponse>> getBillsOfCustomerOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bills from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the customer you want to retrieve the bill from") @PathVariable("personId") Long personId
    ) throws NotFoundException {
        List<Bill> bills = this.billService.getBillsOfPerson(barId, personId);
        return new ResponseEntity<>(converter.convertAllToSummary(bills), HttpStatus.OK);
    }

    @GetMapping("/people/{personId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds bill of customer of bar",
            notes = "Provide id of bar and customer to look up the specific bill from session from the bar",
            response = BillResponse.class
    )
    public ResponseEntity<BillResponse> getBillOfCustomerOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bills from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the customer you want to retrieve the bill from") @PathVariable("personId") Long personId,
            @ApiParam(value = "ID value for the bill you want to retrieve") @PathVariable("billId") Long billId
    ) throws NotFoundException {
        Bill bill = this.billService.getBillOfPerson(barId, personId, billId);
        return new ResponseEntity<>(converter.convert(bill), HttpStatus.OK);
    }

    @PostMapping("/sessions/{sessionId}/")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Creates new bill for session of bar",
            notes = "Provide id of bar and session to add a new bill with information from the request body to session of the bar",
            response = BillResponse.class
    )
    public ResponseEntity<BillResponse> createBillForBar(
            @ApiParam(value = "ID value for the bar you want to create a new bill for") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to add a new bill to") @PathVariable("sessionId") Long sessionId,
            @Valid @RequestBody BillRequest billRequest) throws NotFoundException {
        Bill bill = this.billService.createNewBillForSession(barId, sessionId, billRequest);
        return new ResponseEntity<>(converter.convert(bill), HttpStatus.OK);
    }

    @PatchMapping("/sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER'})")
    @ApiOperation(
            value = "Updates the payment state of the bill of session of bar",
            notes = "Provide id of bar, session and bill to update isPayed to the requested isPayed state ",
            response = BillResponse.class
    )
    public ResponseEntity<BillResponse> setIsPayedOfBillOfBar(
            @ApiParam(value = "ID value for the bar you want to update the bill from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to update the bill from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to update") @PathVariable("billId") Long billId,
            @ApiParam(value = "Boolean value for the payment state you want to set the bill to") @RequestParam(value = "isPayed", required = false) Boolean isPayed
            ) throws NotFoundException {
        Bill bill = this.billService.setIsPayedOfBillOfSession(barId, sessionId, billId, isPayed);
        return new ResponseEntity<>(converter.convert(bill), HttpStatus.OK);
    }

    @DeleteMapping("/sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Deletes bill of session of bar",
            notes = "Provide id of bar, session and bill to delete bill of session of the bar"
    )
    public ResponseEntity<Void> deleteBill(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId
            )throws NotFoundException{
        this.billService.deleteBillFromSessionOfBar(barId, sessionId, billId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
