package com.tungstun.barapi.presentation.controllers.setup;

import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.BillFactory;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.security.config.BarApiGlobalSecurityConfig;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserBarAuthorization;
import com.tungstun.security.data.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

@Import(BarApiGlobalSecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BarIntegrationTestLifeCycle {
    @Autowired
    private SpringBarRepository barRepository;
    public Person person2;
    @Autowired
    public MockMvc mockMvc;
    public Bar bar;
    public Person person;
    public Session session;
    public Bill bill;
    public Order order;
    public Product product;
    public Category category;


    @BeforeEach
    public void setUp() throws Exception {
        barRepository.deleteAll();
        createBar();
        addProductToBar();
        addUsersToBar();
        createSessionsWithBills();
        bar = barRepository.save(bar);
        setTestClassVariables();
    }

    private void createBar() {
        bar = new BarBuilder()
                .setName("bar")
                .setPhoneNumber("0600000000")
                .setMail("mail@testmail.com")
                .setAddress("adressBar 1")
                .build();
    }

    private void addProductToBar() {
        category = new Category("Drinks", ProductType.DRINK);
        bar.addCategory(category);
        product = new ProductBuilder()
                .setName("product")
                .setPrice(1.0)
                .setSize(100)
                .setCategory(category)
                .build();
        bar.addProduct(product);
    }

    private void addUsersToBar() {
        User user2 = new User("testUser", "", "", "", "", new ArrayList<>());
        user2.addUserBarAuthorizations(new UserBarAuthorization(bar, user2, UserRole.ROLE_BAR_OWNER));
        person = new PersonBuilder().setName("testPerson").setPhoneNumber("0600000000").setUser(user2).build();
        bar.addUser(person);

        User user = new User("notConnectedUser", "ss", "mail@mail.com", "sam", "fisher", new ArrayList<>());
        Person person2 = new PersonBuilder().setName("notConnectedPerson").setPhoneNumber("0600000000").setUser(user).build();
        bar.addUser(person2);
    }

    private void createSessionsWithBills() {
        session = Session.create("test");
        bill = new BillFactory(session, person).create();
        bill.addOrder(product, 1, person);
        session.addBill(bill);
        bar.addSession(session);

        Session session2 = Session.create("test2");
        Bill bill2 = new BillFactory(session2, person).create();
        bill2.addOrder(product, 1, person);
        session2.addBill(bill2);
        session2.lock();
        bar.addSession(session2);

        Session session3 = Session.create("test3");
        Bill bill3 = new BillFactory(session3, person).create();
        session3.addBill(bill3);
        session3.lock();
        bar.addSession(session3);
    }

    private void setTestClassVariables() {
        person = bar.getUsers().stream().filter(person -> person.getName().equals("testPerson")).findFirst().get();
        person2 = bar.getUsers().stream().filter(person -> person.getName().equals("notConnectedPerson")).findFirst().get();
        session = bar.getSessions().get(0);
        bill = session.getBills().get(0);
        order = bill.getOrders().get(0);
        product = bar.getProducts().get(0);
        category = bar.getCategories().get(0);
    }

}
