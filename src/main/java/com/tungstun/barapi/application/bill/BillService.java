package com.tungstun.barapi.application.bill;

import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.bill.Bill;
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
    private final PersonQueryHandler personQueryHandler;

    public BillService(SessionRepository sessionRepository, SessionQueryHandler sessionQueryHandler, PersonQueryHandler personQueryHandler) {
        this.sessionRepository = sessionRepository;
        this.sessionQueryHandler = sessionQueryHandler;
        this.personQueryHandler = personQueryHandler;
    }

    public UUID addCustomerToSession(UUID barId, UUID sessionId, BillRequest billRequest) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        Person customer = personQueryHandler.handle(new GetPerson(billRequest.customerId, barId));
        Bill bill = session.addCustomer(customer);
        sessionRepository.save(session);
        return bill.getId();
    }

    public void payBill(UUID barId, UUID sessionId, UUID billId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.getBill(billId)
                .pay();
        sessionRepository.save(session);
    }

    public void deleteBill(UUID barId, UUID sessionId, UUID billId) throws EntityNotFoundException {
        Session session = sessionQueryHandler.handle(new GetSession(sessionId, barId));
        session.removeBill(billId);
        sessionRepository.save(session);
    }
}
