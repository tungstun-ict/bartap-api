package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.BillFactory;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class BillService {
    private final SpringBillRepository SPRING_BILL_REPOSITORY;
    private final SessionService SESSION_SERVICE;
    private final PersonService PERSON_SERVICE;

    public BillService(SpringBillRepository springBillRepository, SessionService sessionService, PersonService personService) {
        this.SPRING_BILL_REPOSITORY = springBillRepository;
        this.SESSION_SERVICE = sessionService;
        this.PERSON_SERVICE = personService;
    }

    public List<Bill> getAllBills(Long barId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        return getAllBillsFromSessions(sessions);
    }

    private List<Bill> getAllBillsFromSessions(List<Session> sessions) {
        return sessions.stream()
                .map(Session::getBills)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Bill getBillOfBar(Long barId, Long sessionId, Long billId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        return sessions.stream()
                .map(Session::getBills)
                .flatMap(List::stream)
                .filter(bill -> bill.getId().equals(billId) && bill.getSession().getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No bill with id: %s was found in session with id:  %s", billId, sessionId)));
    }

    public List<Bill> getAllBillsOfSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        return session.getBills();
    }

    public Bill getBillOfCustomerFromActiveSession(Long barId, Long personId) throws NotFoundException {
        Session activeSession = this.SESSION_SERVICE.getActiveSessionOfBar(barId);
        return activeSession.getBills().stream()
                .filter(bill -> bill.getCustomer().getId().equals(personId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No bill in session for given customer with id" + personId));
    }

    public List<Bill> getBillsOfPerson(Long barId, Long customerId) throws NotFoundException {
        Person customer = this.PERSON_SERVICE.getPersonOfBar(barId, customerId);
        return customer.getBills();
    }

    public Bill getBillOfPerson(Long barId, Long customerId, Long billId) throws NotFoundException {
        List<Bill> bills = getBillsOfPerson(barId, customerId);
        return bills
                .stream()
                .filter(bill -> bill.getId().equals(billId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No bill found with id '%s' for customer in bar", billId)));
    }

    public Bill createNewBillForSession(Long barId, Long sessionId, BillRequest billRequest) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        this.SESSION_SERVICE.checkEditable(session);
        Person customer = this.PERSON_SERVICE.getPersonOfBar(barId, billRequest.customerId);
        if (sessionHasBillWithCustomer(session, customer))
            throw new DuplicateRequestException(String.format("Session already contains a bill for customer with id %s", customer.getId()));
        Bill bill = new BillFactory(session, customer).create();
        return saveBillToSession(bill, session);
    }

    private boolean sessionHasBillWithCustomer(Session session, Person customer) {
        return session.getBills()
                .stream()
                .anyMatch(bill -> bill.getCustomer().equals(customer));
    }

    private Bill saveBillToSession(Bill bill, Session session) {
        session.addBill(bill);
        bill = this.SPRING_BILL_REPOSITORY.save(bill);
        this.SESSION_SERVICE.saveSession(session);
        return bill;
    }

    public Bill setIsPayedOfBillOfSession(Long barId, Long sessionId, Long billId, Boolean isPayed) throws NotFoundException {
        Bill bill = getBillOfBar(barId, sessionId, billId);
        if (isPayed == null) throw new IllegalArgumentException("Parameter isPayed must be true or false");
        bill.setPayed(isPayed);
        return this.SPRING_BILL_REPOSITORY.save(bill);
    }

    public void deleteBillFromSessionOfBar(Long barId, Long sessionId, Long billId) throws NotFoundException {
        Bill bill = getBillOfBar(barId, sessionId, billId);
        this.SESSION_SERVICE.checkEditable(bill.getSession());
        bill.getSession().removeBill(bill);
        bill.setSession(null);
        this.SPRING_BILL_REPOSITORY.delete(bill);
    }

    public Bill removeOrderFromBill(Bill bill, Order order) {
        this.SESSION_SERVICE.checkEditable(bill.getSession());
        bill.removeOrder(order);
        return this.SPRING_BILL_REPOSITORY.save(bill);
    }
}
