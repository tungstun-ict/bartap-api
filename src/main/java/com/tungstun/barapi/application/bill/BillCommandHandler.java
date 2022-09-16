package com.tungstun.barapi.application.bill;

import com.tungstun.barapi.application.bill.command.AddCustomerToSession;
import com.tungstun.barapi.application.bill.command.DeleteBill;
import com.tungstun.barapi.application.bill.command.PayBill;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.BillRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class BillCommandHandler {
    private final BillRepository billRepository;
    private final SessionRepository sessionRepository;
    private final SessionQueryHandler sessionQueryHandler;
    private final PersonQueryHandler personQueryHandler;

    public BillCommandHandler(BillRepository billRepository, SessionRepository sessionRepository, SessionQueryHandler sessionQueryHandler, PersonQueryHandler personQueryHandler) {
        this.billRepository = billRepository;
        this.sessionRepository = sessionRepository;
        this.sessionQueryHandler = sessionQueryHandler;
        this.personQueryHandler = personQueryHandler;
    }

    public UUID handle(AddCustomerToSession command) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(command.barId(), command.sessionId()));
        Person customer = personQueryHandler.handle(new GetPerson(command.barId(), command.customerId()));
        Bill bill = session.addCustomer(customer);
        sessionRepository.save(session);
        return bill.getId();
    }

    public void handle(PayBill command) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(command.barId(), command.sessionId()));
        session.getBill(command.billId())
                .pay();
        sessionRepository.save(session);
    }

    public void handle(DeleteBill command) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(command.barId(), command.sessionId()));
        session.removeBill(command.billId());
        sessionRepository.save(session);
        billRepository.delete(command.billId());
    }
}
