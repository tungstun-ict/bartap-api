package com.tungstun.barapi.application.order;

import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.query.GetBill;
import com.tungstun.barapi.application.order.command.AddOrder;
import com.tungstun.barapi.application.order.command.RemoveOrder;
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
public class OrderCommandHandler {
    private final BillQueryHandler billQueryHandler;
    private final ProductQueryHandler productQueryHandler;
    private final PersonQueryHandler personQueryHandler;
    private final BillRepository billRepository;

    public OrderCommandHandler(BillQueryHandler billQueryHandler, ProductQueryHandler productQueryHandler, PersonQueryHandler personQueryHandler, BillRepository billRepository) {
        this.billQueryHandler = billQueryHandler;
        this.productQueryHandler = productQueryHandler;
        this.personQueryHandler = personQueryHandler;
        this.billRepository = billRepository;
    }

    public UUID handle(AddOrder command) throws EntityNotFoundException {
        Person bartender = personQueryHandler.handle(new GetPersonByUserUsername(command.barId(), command.bartenderUsername()));
        Product product = productQueryHandler.handle(new GetProduct(command.barId(), command.productId()));
        Bill bill = billQueryHandler.handle(new GetBill(command.barId(), command.sessionId(), command.billId()));
        Order order = bill.addOrder(product, command.amount(), bartender);
        billRepository.save(bill);
        return order.getId();
    }

    public void handle(RemoveOrder command) throws EntityNotFoundException {
        Bill bill = billQueryHandler.handle(new GetBill(command.barId(), command.sessionId(), command.billId()));
        bill.removeOrder(command.orderId());
        billRepository.save(bill);
    }
}
