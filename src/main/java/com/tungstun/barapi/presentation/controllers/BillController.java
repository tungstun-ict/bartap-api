package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.BillService;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/")
@RolesAllowed("ROLE_BAR_OWNER")
public class BillController {
    private final BillService BILL_SERVICE;
    private final ResponseMapper RESPONSE_MAPPER;

    public BillController(BillService billService, ResponseMapper responseMapper) {
        this.BILL_SERVICE = billService;
        this.RESPONSE_MAPPER = responseMapper;
//        TypeMap<Bill, BillResponse> typeMap = this.RESPONSE_MAPPER.createTypeMap(Bill.class, BillResponse.class)
//                .addMapping(Bill::calculateTotalPrice, BillResponse::setTotalPrice);



    }

    private BillResponse convertToBillResult(Bill bill){
        BillResponse billResponse = RESPONSE_MAPPER.convert(bill, BillResponse.class);
        billResponse.setTotalPrice(bill.calculateTotalPrice());
        return billResponse;
    }

    @GetMapping("bills")
    public ResponseEntity<List<BillResponse>> getAllBills(@PathVariable("barId") Long barId)
            throws NotFoundException {
        List<Bill> allBills = this.BILL_SERVICE.getAllBills(barId);
        List<BillResponse> billResponses = RESPONSE_MAPPER.convertList(allBills, BillResponse.class);
        return new ResponseEntity<>(billResponses, HttpStatus.OK);
    }

    @GetMapping("/sessions/{sessionId}/bills/{billId}")
    public ResponseEntity<BillResponse> getBillOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        System.out.println(bill.getOrders());
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @GetMapping("/sessions/{sessionId}/bills")
    public ResponseEntity<List<BillResponse>> getAllBillsOfSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId)
            throws NotFoundException {
        List<Bill> allBills = this.BILL_SERVICE.getAllBillsOfSession(barId, sessionId);
        List<BillResponse> billResponses = RESPONSE_MAPPER.convertList(allBills, BillResponse.class);
        return new ResponseEntity<>(billResponses, HttpStatus.OK);
    }

    @PostMapping("/sessions/{sessionId}/")
    public ResponseEntity<BillResponse> createBillForBar(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @Valid @RequestBody BillRequest billRequest) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.createNewBillForSession(barId, sessionId, billRequest);
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @PatchMapping("/sessions/{sessionId}/bills/{billId}")
    public ResponseEntity<BillResponse> setIsPayedOfBillOfBar(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId,
            @RequestParam(value = "isPayed") Boolean isPayed
            ) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.setIsPayedOfBillOfSession(barId, sessionId, billId, isPayed);
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }

    @DeleteMapping("/sessions/{sessionId}/bills/{billId}")
    public ResponseEntity<Void> deleteBill(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId
            )throws NotFoundException{
        this.BILL_SERVICE.deleteBillFromSessionOfBar(barId, sessionId, billId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
