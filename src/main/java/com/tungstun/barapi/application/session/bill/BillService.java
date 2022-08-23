package com.tungstun.barapi.application.session.bill;

import com.tungstun.barapi.application.PersonService;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionRepository;
import com.tungstun.barapi.presentation.dto.request.BillRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class BillService {
    private final SessionRepository sessionRepository;
    private final SessionQueryHandler sessionQueryHandler;
    private final PersonService personService;

    public BillService(SessionRepository sessionRepository, SessionQueryHandler sessionQueryHandler, PersonService personService) {
        this.sessionRepository = sessionRepository;
        this.sessionQueryHandler = sessionQueryHandler;
        this.personService = personService;
    }

    public UUID addCustomerToSession(Long barId, Long sessionId, BillRequest billRequest) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        Person customer = personService.getPersonOfBar(barId, billRequest.customerId);
        Bill bill = session.addCustomer(customer);
        sessionRepository.save(session);
        return bill.getId();
    }

    public void payBill(Long barId, Long sessionId, UUID billId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.getBill(billId)
                .pay();
        sessionRepository.save(session);
    }

    public void deleteBill(Long barId, Long sessionId, UUID billId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.removeBill(billId);
        sessionRepository.save(session);
    }
}
