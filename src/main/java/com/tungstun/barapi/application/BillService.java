package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.domain.Customer;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.BillFactory;
import com.tungstun.barapi.domain.order.Order;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<Bill> bills = getAllBillsFromSessions(sessions);
        if (bills.isEmpty()) throw new NotFoundException(String.format("No bills found for bar with id: %s", barId));
        return bills;
    }

    private List<Bill> getAllBillsFromSessions(List<Session> sessions) {
        List<Bill> bills = new ArrayList<>();
        for (Session session : sessions){
            bills.addAll(session.getBills());
        }
        return bills;
    }

    public Bill getBillOfBar(Long barId, Long sessionId, Long billId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        for (Session session : sessions){
            for (Bill bill : session.getBills()){
                if (bill.getId().equals(billId) &&
                    session.getId().equals(sessionId)) return bill;
            }
        }
        throw new NotFoundException(String.format("No bill with id: %s was found in session with id:  %s", billId, sessionId));
    }

    public List<Bill> getAllBillsOfSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        List<Bill> bills = session.getBills();
        if (bills == null || bills.isEmpty()) throw new NotFoundException(
                String.format("No bills found of session with id: %s", sessionId));
        return bills;
    }

    public Bill createNewBillForSession(Long barId, Long sessionId, BillRequest billRequest) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        this.SESSION_SERVICE.sessionIsActive(session);
        Customer customer = this.PERSON_SERVICE.getCustomerOfBar(barId, billRequest.customerId);
        if (sessionHasBillWithCustomer(session, customer))
            throw new DuplicateRequestException(String.format("Session already contains a bill for customer with id %s", customer.getId()));
        Bill bill = new BillFactory(session, customer).create();
        return saveBillToSession(bill, session);
    }

    private boolean sessionHasBillWithCustomer(Session session, Customer customer){
        for (Bill bill : session.getBills()){
            if (bill.getCustomer().equals(customer)) return true;
        }
        return false;
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
        this.SESSION_SERVICE.sessionIsActive(bill.getSession());
        bill.getSession().removeBill(bill);
        this.SPRING_BILL_REPOSITORY.delete(bill);
    }

    public Bill addOrderToBill(Bill bill, Order order) {
        this.SESSION_SERVICE.sessionIsActive(bill.getSession());
        bill.addOrder(order);
        return this.SPRING_BILL_REPOSITORY.save(bill);
    }

    public Bill removeOrderFromBill(Bill bill, Order order) {
        this.SESSION_SERVICE.sessionIsActive(bill.getSession());
        bill.removeOrder(order);
        return this.SPRING_BILL_REPOSITORY.save(bill);
    }
}
