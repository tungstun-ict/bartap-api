package com.tungstun.barapi.domain.session;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {
    private Session session;

    private static LocalDateTime getTruncatedCurrentDateTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @BeforeEach
    void setUp() {
        session = new SessionFactory("session").create();
    }

    @Test
    @DisplayName("End session ends session")
    void endSession_EndsSession() {
        LocalDateTime currentDateTime = getTruncatedCurrentDateTime();

        session.end();

        LocalDateTime sessionEndDateTime = session.getEndDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(currentDateTime, sessionEndDateTime);
    }

    @Test
    @DisplayName("End session when session is already ended")
    void endSession_WhenSessionIsEndedAlready() {
        session.end();

        assertThrows(
                IllegalStateException.class,
                session::end
        );
    }

    @Test
    @DisplayName("Active session is active")
    void session_IsActive() {
        assertTrue(session.isActive());
        assertFalse(session.isEnded());
    }

    @Test
    @DisplayName("Ended session is not active")
    void endedSession_IsNotActive() {
        session.end();

        assertFalse(session.isActive());
        assertTrue(session.isEnded());
    }

    @Test
    @DisplayName("Active session is editable")
    void activeSession_IsEditable() {
        assertDoesNotThrow(() -> session.checkEditable());
    }

    @Test
    @DisplayName("Ended session is editable throws")
    void endedSessionIsEditable_Throws() {
        session.end();

        assertThrows(
                InvalidSessionStateException.class,
                () -> session.checkEditable()
        );
    }

    @Test
    @DisplayName("Add customer bill")
    void addCustomer() {
        Person person = new PersonBuilder("name").build();

        Bill bill = session.addCustomer(person);

        assertNotNull(bill);
    }

    @Test
    @DisplayName("Add customer bill when already in session throws")
    void addCustomerWhenAlreadyInSession() {
        Person person = new PersonBuilder("name").build();
        session.addCustomer(person);

        assertThrows(
                DuplicateRequestException.class,
                () -> session.addCustomer(person)
        );
    }

    @Test
    @DisplayName("Add customer bill when session ended throws")
    void addCustomerWhenSessionEnded() {
        Person person = new PersonBuilder("name").build();
        session.end();

        assertThrows(
                InvalidSessionStateException.class,
                () -> session.addCustomer(person)
        );
    }

    @Test
    @DisplayName("Remove bill")
    void removeBill() {
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);

        boolean isRemoved = session.removeBill(bill.getId());

        assertTrue(isRemoved);
        assertTrue(session.getBills().isEmpty());
    }

    @Test
    @DisplayName("Remove bill not existing bill returns false")
    void removeNotExistingBill() {
        boolean isRemoved = session.removeBill(UUID.randomUUID());

        assertFalse(isRemoved);
        assertTrue(session.getBills().isEmpty());
    }

    @Test
    @DisplayName("Remove bill when session ended throws")
    void removeBill_WhenSessionEnded_Throws() {
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);
        session.end();

        assertThrows(
                InvalidSessionStateException.class,
                () -> session.removeBill(bill.getId())
        );
    }

    @Test
    @DisplayName("Get bill")
    void getBill() {
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);

        Bill actualBill = session.getBill(bill.getId());

        assertEquals(bill.getId(), actualBill.getId());
    }

    @Test
    @DisplayName("Get not existing bill throws")
    void getNonExistingBill_Throws() {
        assertThrows(
                EntityNotFoundException.class,
                () -> session.getBill(UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Get all orders with no bills is empty list")
    void getAllOrdersWithNoBill_IsEmpty() {
        List<Order> orders = session.getAllOrders();

        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Get all orders with one bills is bill's orders")
    void getAllOrdersWithOneBill_ReturnsOrders() {
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);
        Category category = new CategoryFactory("category").create();
        Product product = new ProductBuilder("prod", category).build();
        Order order = bill.addOrder(product, 1, person);

        List<Order> orders = session.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals(order.getId(), orders.get(0).getId());
    }

    @Test
    @DisplayName("Get all orders with multiple bills")
    void getAllOrdersWithMultipleBills() {
        Category category = new CategoryFactory("category").create();
        Product product = new ProductBuilder("prod", category).build();

        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);
        bill.addOrder(product, 1, person);

        Person person2 = new PersonBuilder("name2").build();
        Bill bill2 = session.addCustomer(person2);
        bill2.addOrder(product, 1, person2);


        List<Order> orders = session.getAllOrders();

        assertEquals(2, orders.size());
    }
}
