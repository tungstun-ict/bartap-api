package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.BillService;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.dto.response.BillSummaryResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/bars/{barId}/")
public class BillController {
    private final BillService BILL_SERVICE;
    private final ResponseMapper RESPONSE_MAPPER;

    public BillController(BillService billService, ResponseMapper responseMapper) {
        this.BILL_SERVICE = billService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private BillResponse convertToBillResult(Bill bill){
        BillResponse billResponse = RESPONSE_MAPPER.convert(bill, BillResponse.class);
        billResponse.setTotalPrice(bill.calculateTotalPrice());
        return billResponse;
    }

    private BillSummaryResponse convertToBillSummaryResult(Bill bill){
        BillSummaryResponse billResponse = RESPONSE_MAPPER.convert(bill, BillSummaryResponse.class);
        billResponse.setTotalPrice(bill.calculateTotalPrice());
        return billResponse;
    }

    private List<BillSummaryResponse> convertListToBillSummaryResult(List<Bill> bills) {
        return bills.stream()
                .map(this::convertToBillSummaryResult)
                .collect(Collectors.toList());
    }

    @GetMapping("bills")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds all bills of bar",
            notes = "Provide id of bar to look up all bills that are linked to the bar",
            response = BillResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<BillResponse>> getAllBills(
            @ApiParam(value = "ID value for the bar you want to retrieve bills from") @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<Bill> allBills = this.BILL_SERVICE.getAllBills(barId);
        List<BillResponse> billResponses = allBills.stream()
                .map(this::convertToBillResult)
                .collect(Collectors.toList());
        return new ResponseEntity<>(billResponses, HttpStatus.OK);
    }

    @GetMapping("/sessions/{sessionId}/bills")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
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
        List<Bill> allBills = this.BILL_SERVICE.getAllBillsOfSession(barId, sessionId);
        List<BillResponse> billResponses = allBills.stream()
                .map(this::convertToBillResult)
                .collect(Collectors.toList());
        return new ResponseEntity<>(billResponses, HttpStatus.OK);
    }

    @GetMapping("/sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
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
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @GetMapping("/people/{personId}/bills")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds bills of customer of bar",
            notes = "Provide id of bar and customer to look up all bills from session from the bar",
            response = BillResponse.class
    )
    public ResponseEntity<List<BillSummaryResponse>> getBillsOfCustomerOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bills from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the customer you want to retrieve the bill from") @PathVariable("personId") Long personId
    ) throws NotFoundException {
        List<Bill> bills = this.BILL_SERVICE.getBillsOfPerson(barId, personId);
        List<BillSummaryResponse> billResponses = convertListToBillSummaryResult(bills);
        return new ResponseEntity<>(billResponses, HttpStatus.OK);
    }

    @GetMapping("/people/{personId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
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
        Bill bill = this.BILL_SERVICE.getBillOfPerson(barId, personId, billId);
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @PostMapping("/sessions/{sessionId}/")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Creates new bill for session of bar",
            notes = "Provide id of bar and session to add a new bill with information from the request body to session of the bar",
            response = BillResponse.class
    )
    public ResponseEntity<BillResponse> createBillForBar(
            @ApiParam(value = "ID value for the bar you want to create a new bill for") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to add a new bill to") @PathVariable("sessionId") Long sessionId,
            @Valid @RequestBody BillRequest billRequest) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.createNewBillForSession(barId, sessionId, billRequest);
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @PatchMapping("/sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
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
        Bill bill = this.BILL_SERVICE.setIsPayedOfBillOfSession(barId, sessionId, billId, isPayed);
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @DeleteMapping("/sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes bill of session of bar",
            notes = "Provide id of bar, session and bill to delete bill of session of the bar"
    )
    public ResponseEntity<Void> deleteBill(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId
            )throws NotFoundException{
        this.BILL_SERVICE.deleteBillFromSessionOfBar(barId, sessionId, billId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
