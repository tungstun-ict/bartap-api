package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.bill.BillCommandHandler;
import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.command.AddCustomerToSession;
import com.tungstun.barapi.application.bill.command.DeleteBill;
import com.tungstun.barapi.application.bill.command.PayBill;
import com.tungstun.barapi.application.bill.query.GetBill;
import com.tungstun.barapi.application.bill.query.ListBillsOfCustomer;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.presentation.dto.converter.BillConverter;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.dto.response.BillSummaryResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/bars/{barId}/")
public class BillController {
    private final BillCommandHandler billCommandHandler;
    private final BillQueryHandler billQueryHandler;
    private final BillConverter converter;

    public BillController(BillCommandHandler billCommandHandler, BillQueryHandler billQueryHandler, BillConverter converter) {
        this.billCommandHandler = billCommandHandler;
        this.billQueryHandler = billQueryHandler;
        this.converter = converter;
    }

//    @GetMapping("bills")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
//    @ApiOperation(
//            value = "Finds all bills of bar",
//            notes = "Provide categoryId of bar to look up all bills that are linked to the bar",
//            response = BillResponse.class,
//            responseContainer = "List"
//    )
//    public ResponseEntity<List<BillResponse>> getAllBills(
//            @ApiParam(value = "ID value for the bar you want to retrieve bills from") @PathVariable("barId") UUID barId
//    ) throws EntityNotFoundException {
//        List<Bill> allBills = this.billService.getAllBills(barId);
//        return new ResponseEntity<>(converter.convertAll(allBills), HttpStatus.OK);
//    }

//    @GetMapping("/sessions/{sessionId}/bills")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
//    @ApiOperation(
//            value = "Finds bills of session of bar",
//            notes = "Provide categoryId of bar and session to look up bills from the session from the bar",
//            response = BillResponse.class,
//            responseContainer = "List"
//    )
//    public ResponseEntity<List<BillResponse>> getAllBillsOfSession(
//            @ApiParam(value = "ID value for the bar you want to retrieve bills from") @PathVariable("barId") UUID barId,
//            @ApiParam(value = "ID value for the session you want to retrieve bills from") @PathVariable("sessionId") UUID sessionId
//    ) throws EntityNotFoundException {
//        List<Bill> allBills = this.billService.getAllBillsOfSession(barId, sessionId);
//        return new ResponseEntity<>(converter.convertAll(allBills), HttpStatus.OK);
//    }

    @GetMapping("/sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds bill of bar",
            notes = "Provide categoryId of bar, session and bill to look up the specific bill from session from the bar",
            response = BillResponse.class
    )
    public BillResponse getBillOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bill from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve the bill from") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        Bill bill = billQueryHandler.handle(new GetBill(billId, sessionId, barId));
        return converter.convert(bill);
    }

//    @GetMapping("/sessions/active/people/{personId}/bill")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
//    @ApiOperation(
//            value = "Finds bill of bar",
//            notes = "Provide categoryId of bar, session and bill to look up the specific bill from session from the bar",
//            response = BillResponse.class
//    )
//    public ResponseEntity<BillResponse> getBillOfBar(
//            @ApiParam(value = "ID value for the bar you want to retrieve the bill from") @PathVariable("barId") UUID barId,
//            @ApiParam(value = "ID value for the bill you want to retrieve") @PathVariable("personId") UUID personId
//    ) throws EntityNotFoundException {
//        Bill bill = this.billService.getBillOfCustomerFromActiveSession(barId, personId);
//        return new ResponseEntity<>(converter.convert(bill), HttpStatus.OK);
//    }

    @GetMapping("/people/{personId}/bills")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds bills of customer of bar",
            notes = "Provide categoryId of bar and customer to look up all bills from session from the bar",
            response = BillResponse.class
    )
    public List<BillSummaryResponse> getBillsOfCustomerOfBar(
            @ApiParam(value = "ID value for the bar you want to retrieve the bills from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the customer you want to retrieve the bill from") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        List<Bill> bills = billQueryHandler.handle(new ListBillsOfCustomer(barId, personId));
        return converter.convertAllToSummary(bills);
    }

//    @GetMapping("/people/{personId}/bills/{billId}")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
//    @ApiOperation(
//            value = "Finds bill of customer of bar",
//            notes = "Provide categoryId of bar and customer to look up the specific bill from session from the bar",
//            response = BillResponse.class
//    )
//    public BillResponse getBillOfCustomerOfBar(
//            @ApiParam(value = "ID value for the bar you want to retrieve the bills from") @PathVariable("barId") UUID barId,
//            @ApiParam(value = "ID value for the customer you want to retrieve the bill from") @PathVariable("personId") UUID personId,
//            @ApiParam(value = "ID value for the bill you want to retrieve") @PathVariable("billId") UUID billId
//    ) throws EntityNotFoundException {
//        Bill bill = this.billService.getBillOfPerson(barId, personId, billId);
//        return converter.convert(bill);
//    }

    @PostMapping("/sessions/{sessionId}/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Creates new bill for session of bar",
            notes = "Provide categoryId of bar and session to add a new bill with information from the request body to session of the bar",
            response = BillResponse.class
    )
    public UUID createBillForBar(
            @ApiParam(value = "ID value for the bar you want to create a new bill for") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to add a new bill to") @PathVariable("sessionId") UUID sessionId,
            @Valid @RequestBody BillRequest billRequest
    ) throws EntityNotFoundException {
        AddCustomerToSession command = new AddCustomerToSession(barId, sessionId, billRequest.customerId);
        return billCommandHandler.handle(command);
    }

    @PatchMapping("/sessions/{sessionId}/bills/{billId}/pay")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @ApiOperation(
            value = "Updates the payment state of the bill of session of bar",
            notes = "Provide categoryId of bar, session and bill to update isPayed to the requested isPayed state ",
            response = BillResponse.class
    )
    public void payOfBillOfBar(
            @ApiParam(value = "ID value for the bar you want to update the bill from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to update the bill from") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to update") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        PayBill command = new PayBill(barId, sessionId, billId);
        billCommandHandler.handle(command);
    }

    @DeleteMapping("/sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes bill of session of bar",
            notes = "Provide categoryId of bar, session and bill to delete bill of session of the bar"
    )
    public void deleteBill(
            @PathVariable("barId") UUID barId,
            @PathVariable("sessionId") UUID sessionId,
            @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        DeleteBill command = new DeleteBill(billId);
        billCommandHandler.handle(command);
    }
}
