package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import com.tungstun.security.domain.user.User;
import com.tungstun.statistics.domain.statistics.filter.BillFromUserWithIdFilter;
import com.tungstun.statistics.domain.statistics.filter.BillFromUserWithUsernameFilter;
import com.tungstun.statistics.domain.statistics.filter.SessionFromDateFilter;
import com.tungstun.statistics.domain.statistics.filter.SessionToDateFilter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatisticsGeneratorTest {
    private Product product;
    private User user;
    private Bill bill;
    private Bill bill2;
    private Bar bar;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        product = new ProductBuilder(
                "drink",
                new CategoryFactory("category").create())
                .setPrice(2.0)
                .build();

        user = new User(
                UUID.randomUUID(),
                "username",
                "password",
                "mail@mail.com",
                "first",
                "last",
                "+31612345679",
                new ArrayList<>());
        Person customer = new PersonBuilder("customer")
                .setUser(user)
                .build();
        Person customer2 = new PersonBuilder("customer2")
                .build();

        Session session = new SessionFactory("session").create();
        bill = session.addCustomer(customer);
        bill.addOrder(product, 4, customer); // Sums to 8 euro's (2.0 x 4)
        session.addCustomer(customer2);

        Session session2 = new SessionFactory("session2").create();
        bill2 = session2.addCustomer(customer2);
        bill2.addOrder(product, 1, customer); // Sums to 2 euro's (2.0 x 4)
        bill2.pay();

        // Set dates to have been created a week ago
        FieldUtils.writeField(session2, "creationDate", LocalDateTime.now().minusDays(7), true);
        FieldUtils.writeField(session2, "endDate", LocalDateTime.now().minusDays(6), true);


        bar = new BarBuilder("bar")
                .setSessions(List.of(session, session2))
                .build();
    }

    @Test
    @DisplayName("Constructing a StatisticsGenerator with only null values for Filters does not filter sessions")
    void noFilterConstruction() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);

        Statistics statistics = generator.generate();

        assertEquals(2, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Constructing a StatisticsGenerator with Filters with all values filled adds all filters")
    void allFilterConstruction() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(LocalDate.now(), LocalDate.now()));
        generator.addBar(bar);

        Statistics statistics = generator.generate();

        assertEquals(0, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Constructing a StatisticsGenerator with from date Filter")
    void fromFilterConstruction() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(LocalDate.now(), null));
        generator.addBar(bar);

        Statistics statistics = generator.generate();

        assertEquals(0, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Constructing a StatisticsGenerator with to date Filter")
    void toFilterConstruction() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, LocalDate.now().minusDays(1)));
        generator.addBar(bar);

        Statistics statistics = generator.generate();

        assertEquals(2, statistics.totalSpent());
        assertEquals(0, statistics.totalNotYetPayed());
        assertEquals(bill2.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Generate statistics without filters takes all sessions")
    void noFiltersUsesAllSessions() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);

        Statistics statistics = generator.generate();

        assertEquals(2, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Add bar adds bar's sessions to the collected sessions and gives stats")
    void addBarAddsSessionsAToCollectedSessions() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);

        Statistics statistics = generator.generate();

        assertEquals(2, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Generate without bar and filters returns all 0 or null values in Statistics")
    void generateWithoutSession() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));

        Statistics statistics = generator.generate();

        assertEquals(0, statistics.totalSpent());
        assertEquals(0, statistics.totalNotYetPayed());
        assertNull(statistics.mostExpensiveBill());
        assertNull(statistics.mostSoldProduct());
    }

    @Test
    @DisplayName("Adding 1 session filter adds filter to existing session filter")
    void addSessionFilter() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);
        generator.addSessionFilter(new SessionFromDateFilter(LocalDate.now()));

        Statistics statistics = generator.generate();

        assertEquals(0, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Adding multiple session filters adds filter to existing session filters")
    void addMultipleSessionFilters() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);
        generator.addSessionFilter(new SessionFromDateFilter(LocalDate.now().minusDays(8)));
        generator.addSessionFilter(new SessionToDateFilter(LocalDate.now().minusDays(1)));

        Statistics statistics = generator.generate();

        assertEquals(2, statistics.totalSpent());
        assertEquals(0, statistics.totalNotYetPayed());
        assertEquals(bill2.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Adding 1 bill filter adds filter to existing bill filter")
    void addBillFilter() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);
        generator.addBillFilter(new BillFromUserWithIdFilter(user.getId()));

        Statistics statistics = generator.generate();

        assertEquals(0, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }

    @Test
    @DisplayName("Adding multiple bill filters adds filter to existing bill filters")
    void addMultipleBillFilters() {
        StatisticsGenerator generator = new StatisticsGenerator(new Filters(null, null));
        generator.addBar(bar);
        generator.addBillFilter(new BillFromUserWithIdFilter(user.getId()));
        generator.addBillFilter(new BillFromUserWithUsernameFilter(user.getUsername()));

        Statistics statistics = generator.generate();

        assertEquals(0, statistics.totalSpent());
        assertEquals(8, statistics.totalNotYetPayed());
        assertEquals(bill.getId(), statistics.mostExpensiveBill().getId());
        assertEquals(product.getId(), statistics.mostSoldProduct().getId());
    }
}
