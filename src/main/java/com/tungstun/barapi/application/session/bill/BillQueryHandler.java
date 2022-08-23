package com.tungstun.barapi.application.session.bill;

import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.bill.query.GetBill;
import com.tungstun.barapi.application.session.bill.query.ListBillsOfCustomer;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.session.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillQueryHandler {
    private final SessionQueryHandler sessionQueryHandler;

    public BillQueryHandler(SessionQueryHandler sessionQueryHandler) {
        this.sessionQueryHandler = sessionQueryHandler;
    }

    public Bill handle(GetBill query) {
        return sessionQueryHandler.handle(new GetSession(query.sessionId(), query.barId()))
                .getBill(query.billId());
    }

    public List<Bill> handle(ListBillsOfCustomer query) {
        return sessionQueryHandler.handle(new ListSessionsOfBar(query.barId()))
                .stream()
                .map(Session::getBills)
                .flatMap(List::stream)
                .filter(bill -> bill.getCustomer().getId().equals(query.customerId()))
                .collect(Collectors.toList());
    }

//
//
//    //    public List<Bill> getAllBills(Long barId) throws EntityNotFoundException {
////        List<Session> sessions = this.sessionService.getAllSessionsOfBar(barId);
////        return sessions.stream()
////                .map(Session::getBills)
////                .flatMap(List::stream)
////                .collect(Collectors.toList());
////    }
////
//    public Bill getBillOfBar(Long barId, Long sessionId, Long billId) throws EntityNotFoundException {
//        List<Session> sessions = this.sessionService.getAllSessionsOfBar(barId);
//        return sessions.stream()
//                .map(Session::getBills)
//                .flatMap(List::stream)
//                .filter(bill -> bill.getId().equals(billId))
//                .filter(bill -> bill.getSession().getId().equals(sessionId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException(String.format("No bill with id: %s was found in session with id:  %s", billId, sessionId)));
//    }
//
//    public List<Bill> getAllBillsOfSession(Long barId, Long sessionId) throws EntityNotFoundException {
//        Session session = this.sessionService.getSessionOfBar(barId, sessionId);
//        return session.getBills();
//    }
//
//    public Bill getBillOfCustomerFromActiveSession(Long barId, Long personId) throws EntityNotFoundException {
//        Session activeSession = this.sessionService.getActiveSessionOfBar(barId);
//        return activeSession.getBills().stream()
//                .filter(bill -> bill.getCustomer().getId().equals(personId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("No bill in session for given customer with id" + personId));
//    }
//
////    public List<Bill> getBillsOfPerson(Long barId, Long customerId) throws EntityNotFoundException {
////        Person customer = this.personService.getPersonOfBar(barId, customerId);
////        return customer.getBills();
////    }
//
//    public Bill getBillOfPerson(Long barId, Long customerId, Long billId) throws EntityNotFoundException {
//        List<Bill> bills = getBillsOfPerson(barId, customerId);
//        return bills
//                .stream()
//                .filter(bill -> bill.getId().equals(billId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException(String.format("No bill found with id '%s' for customer in bar", billId)));
//    }

}
