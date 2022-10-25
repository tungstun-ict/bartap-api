package com.tungstun.barapi.application.bill;

import com.tungstun.barapi.application.bill.query.*;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.query.GetPersonByUserUsername;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.query.GetActiveSession;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillQueryHandler {
    private final SessionQueryHandler sessionQueryHandler;
    private final PersonQueryHandler personQueryHandler;

    public BillQueryHandler(SessionQueryHandler sessionQueryHandler, PersonQueryHandler personQueryHandler) {
        this.sessionQueryHandler = sessionQueryHandler;
        this.personQueryHandler = personQueryHandler;
    }

    public Bill handle(GetBill query) {
        return sessionQueryHandler.handle(new GetSession(query.barId(), query.sessionId()))
                .getBill(query.billId());
    }

    public Bill handle(GetBillAsCustomer query) {
        return Optional.ofNullable(handle(new GetBill(query.barId(), query.sessionId(), query.billId())))
                .filter(bill -> bill.getCustomer().getUser().getId().equals(query.userId()))
                .orElseThrow(() -> new EntityNotFoundException("No bill found with id: " + query.billId()));
    }

    public Bill handle(GetActiveBillOfUser query) throws EntityNotFoundException {
        Person person = personQueryHandler.handle(new GetPersonByUserUsername(query.barId(), query.userId()));
        return sessionQueryHandler.handle(new GetActiveSession(query.barId()))
                .getBills()
                .stream()
                .filter(bill -> bill.getCustomer().getId().equals(person.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No active bill found for user"));
    }

    public List<Bill> handle(ListBillsOfCustomer query) {
        return sessionQueryHandler.handle(new ListSessionsOfBar(query.barId()))
                .stream()
                .map(Session::getBills)
                .flatMap(List::stream)
                .filter(bill -> bill.getCustomer().getId().equals(query.customerId()))
                .collect(Collectors.toList());
    }

    public List<Bill> handle(ListBillsOfUser query) {
        Person person = personQueryHandler.handle(new GetPersonByUserUsername(query.barId(), query.userId()));
        return handle(new ListBillsOfCustomer(query.barId(), person.getId()));
    }
}
