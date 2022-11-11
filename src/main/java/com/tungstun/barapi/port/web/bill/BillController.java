package com.tungstun.barapi.port.web.bill;

import com.tungstun.barapi.application.bill.BillCommandHandler;
import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.command.AddCustomerToSession;
import com.tungstun.barapi.application.bill.command.DeleteBill;
import com.tungstun.barapi.application.bill.command.PayBill;
import com.tungstun.barapi.application.bill.command.PayBills;
import com.tungstun.barapi.application.bill.query.*;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.port.web.bill.converter.BillConverter;
import com.tungstun.barapi.port.web.bill.request.CreateBillRequest;
import com.tungstun.barapi.port.web.bill.response.BillResponse;
import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.security.config.filter.Authorization;
import com.tungstun.security.config.filter.UserProfile;
import com.tungstun.security.domain.user.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
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

    @GetMapping("/sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER', 'CUSTOMER'})")
    @Operation(
            summary = "Finds bill of bar",
            description = "Find the bill of session of bar with the given id's",
            tags = "Bill"
    )
    public BillResponse getBillOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId,
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        UserProfile user = (UserProfile) authentication.getPrincipal();
        Role role = user.authorizations()
                .stream()
                .filter(auth -> auth.barId().equals(barId))
                .map(Authorization::role)
                .map(Role::getRole)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No bill found for id: " + billId));
        if (role.equals(Role.CUSTOMER)) {
            Bill bill = billQueryHandler.handle(new GetBillAsCustomer(barId, sessionId, billId, user.getId()));
            return converter.convert(bill);
        }
        Bill bill = billQueryHandler.handle(new GetBill(barId, sessionId, billId));
        return converter.convert(bill);
    }

    @GetMapping("/people/{personId}/bills")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds bills of customer",
            description = "Find all the bills of customer of bar with given id's",
            tags = "Bill"
    )
    public List<BillSummaryResponse> getBillsOfCustomerOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the customer") @PathVariable("personId") UUID personId
    ) throws EntityNotFoundException {
        List<Bill> bills = billQueryHandler.handle(new ListBillsOfCustomer(barId, personId));
        return converter.convertAllToSummary(bills);
    }

    @GetMapping("/bills")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER', 'CUSTOMER'})")
    @Operation(
            summary = "Finds bills of authenticated user",
            description = "Find all the bills of the authenticated user of bar with given id's",
            tags = "Bill"
    )
    public List<BillSummaryResponse> getBillsOfAuthenticatedUserOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        UserProfile userProfile = (UserProfile) authentication.getPrincipal();
        List<Bill> bills = billQueryHandler.handle(new ListBillsOfUser(barId, userProfile.id()));
        return converter.convertAllToSummary(bills);
    }

    @GetMapping("/bills/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER', 'CUSTOMER'})")
    @Operation(
            summary = "Finds active bill of authenticated user",
            description = "Find the active bill of the authenticated user of bar with given id's",
            tags = "Bill"
    )
    public BillSummaryResponse getActiveBillOfAuthenticatedUserOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(hidden = true) Authentication authentication
    ) throws EntityNotFoundException {
        UserProfile userProfile = (UserProfile) authentication.getPrincipal();
        Bill bill = billQueryHandler.handle(new GetActiveBillOfUser(barId, userProfile.id()));
        return converter.convertToSummary(bill);
    }

    @PostMapping("/sessions/{sessionId}/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Adds bill for customer to session",
            description = "Add new bill for a customer to a session if that person did not already have a bill active in the session",
            tags = "Bill"
    )
    public UUID createBillForBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Valid @RequestBody CreateBillRequest createBillRequest
    ) throws EntityNotFoundException {
        AddCustomerToSession command = new AddCustomerToSession(barId, sessionId, createBillRequest.personId());
        return billCommandHandler.handle(command);
    }

    @PatchMapping("/sessions/{sessionId}/bills/{billId}/pay")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Updates the payment state of the bill",
            description = "Update the payment state of a bill to payed (payed = true)",
            tags = "Bill"
    )
    public void payOfBillOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        PayBill command = new PayBill(barId, sessionId, billId);
        billCommandHandler.handle(command);
    }

    @PatchMapping("/customer/{customerId}/bills/pay")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER'})")
    @Operation(
            summary = "Updates payment state of bills up to date",
            description = "Updates the payment state of all bil up to and on the passed date to true. " +
                    "If no date specified, the current date is used",
            tags = "Bill"
    )
    public void payOfBillOfBar(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("customerId") UUID customerId,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date
    ) throws EntityNotFoundException {
        date = Objects.requireNonNullElse(date, LocalDate.now());
        PayBills command = new PayBills(barId, customerId, date);
        billCommandHandler.handle(command);
    }

    @DeleteMapping("/sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Deletes bill of session",
            description = "Delete bill of session of bar with the given id's",
            tags = "Bill"
    )
    public void deleteBill(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        DeleteBill command = new DeleteBill(barId, sessionId, billId);
        billCommandHandler.handle(command);
    }
}
