package com.tungstun.barapi.application.order;

import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.query.GetBill;
import com.tungstun.barapi.application.order.command.AddOrder;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.query.GetPersonByUserUsername;
import com.tungstun.barapi.application.product.ProductQueryHandler;
import com.tungstun.barapi.application.product.query.GetProduct;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.BillRepository;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class OrderService {
    private final BillQueryHandler billQueryHandler;
    private final ProductQueryHandler productQueryHandler;
    private final PersonQueryHandler personQueryHandler;
    private final BillRepository billRepository;

    public OrderService(BillQueryHandler billQueryHandler, ProductQueryHandler productQueryHandler, PersonQueryHandler personQueryHandler, BillRepository billRepository) {
        this.billQueryHandler = billQueryHandler;
        this.productQueryHandler = productQueryHandler;
        this.personQueryHandler = personQueryHandler;
        this.billRepository = billRepository;
    }

    public UUID addProductToBill(AddOrder command) throws EntityNotFoundException {
        Person bartender = personQueryHandler.handle(new GetPersonByUserUsername(command.bartenderUsername(), command.barId()));
        Product product = productQueryHandler.handle(new GetProduct(command.productId(), command.barId()));
        Bill bill = billQueryHandler.handle(new GetBill(command.billId(), command.sessionId(), command.barId()));
        Order order = bill.addOrder(product, command.amount(), bartender);
        billRepository.save(bill);
        return order.getId();
    }

    public void deleteOrderFromBill(UUID barId, UUID sessionId, UUID billId, UUID orderId) throws EntityNotFoundException {
        Bill bill = billQueryHandler.handle(new GetBill(billId, sessionId, barId));
        bill.removeOrder(orderId);
        billRepository.save(bill);
    }
}
