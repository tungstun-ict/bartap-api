package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.domain.Bill;
import com.tungstun.barapi.domain.BillFactory;
import com.tungstun.barapi.domain.Customer;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {
    private final SpringBillRepository SPRING_BILL_REPOSITORY;
    private final SessionService SESSION_SERVICE;

    public BillService(SpringBillRepository springBillRepository, SessionService sessionService) {
        this.SPRING_BILL_REPOSITORY = springBillRepository;
        this.SESSION_SERVICE = sessionService;
    }

    /**
     * Returns a list with all bills of a bar
     * @return list of bills
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any sessions
     */
    public List<Bill> getAllBills(Long barId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        List<Bill> bills = new ArrayList<>();
        for (Session session : sessions){
            List<Bill> sessionBills = session.getBills();
            if ( sessionBills != null && !sessionBills.isEmpty() ) {
                bills.addAll(sessionBills);
            }
        }
        if (bills.isEmpty()) throw new NotFoundException(String.format("No bills found for bar with id: %s", barId));
        return bills;
    }

    /**
     * Returns bill with given id of session with given id of bar with given id
     * @return bill
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any sessions or
     *      if bar does not have a session with given id or
     *      if no session has a bill with given id
     */
    public Bill getBillOfBar(Long barId, Long sessionId, Long billId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        for (Session session : sessions){
            for (Bill bill : session.getBills()){
                if ( bill.getId().equals(billId)
                        && session.getId().equals(sessionId) ) return bill;
            }
        }
        throw new NotFoundException(String.format("No bill with id: %s was found in session with id:  %s", billId, sessionId));
    }

    /**
     * Returns a list with all bills of session of a bar
     * @return list of bills
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any sessions or
     *      if bar does not have a session with given id
     */
    public List<Bill> getAllBillsOfSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        List<Bill> bills = session.getBills();
        if (bills == null || bills.isEmpty()) throw new NotFoundException(
                String.format("No bills found of session with id: %s", sessionId));
        return bills;
    }

    /**
     * Creates a new bill and adds it to the session with given id of bar with given id
     * @return created bill
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any sessions or
     *      if bar does not have a session with given id
     */
    public Bill createNewBillForSession(Long barId, Long sessionId, BillRequest billRequest) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        Customer customer = null;
        for (Bill bill : session.getBills()){
            if (bill.getCustomer().getId().equals(billRequest.customerId)) customer = bill.getCustomer();
        }
        if (sessionHasBillWithCustomer(session, customer))
            throw new DuplicateRequestException("Session already contains a bill for requested customer");
        Bill bill = new BillFactory(session, customer).create();
        session.addBill(bill);
        bill = this.SPRING_BILL_REPOSITORY.save(bill);
        this.SESSION_SERVICE.saveSession(session);
        return bill;
    }

    /**
     * Sets the isPayed attribute of bill with given id of session with given id
     * of bar with given id
     * @return altered bill
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any sessions or
     *      if bar does not have a session with given id
     *      if no session has a bill with given id
     */
    public Bill setIsPayedOfBillOfSession(Long barId, Long sessionId, Long billId, Boolean isPayed) throws NotFoundException {
       Bill bill = getBillOfBar(barId, sessionId, billId);
       if (isPayed == null) throw new IllegalArgumentException("isPayed must be true or false");
       bill.setPayed(isPayed);
       return this.SPRING_BILL_REPOSITORY.save(bill);
    }

    /**
     * Deletes bill with given id of session with given id of bar with given id
     * @throws NotFoundException if no bar with given id is found or
     *      if bar does not have any sessions or
     *      if bar does not have a session with given id
     *      if no session has a bill with given id
     */
    public void deleteBillFromSessionOfBar(Long barId, Long sessionId, Long billId) throws NotFoundException {
        Bill bill = getBillOfBar(barId, sessionId, billId);
        bill.getSession().removeBill(bill);
        this.SPRING_BILL_REPOSITORY.delete(bill);
    }

    /**
     * Checks if session already has an existing bill for customer
     * @return true if session has bill with customer
     *      false if session does not have a bill with customer
     */
    private boolean sessionHasBillWithCustomer(Session session, Customer customer){
        for (Bill bill : session.getBills()){
            if (bill.getCustomer().equals(customer)) return true;
        }
        return false;
    }
}
