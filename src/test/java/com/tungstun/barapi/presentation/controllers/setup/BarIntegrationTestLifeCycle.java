//package com.tungstun.barapi.presentation.controllers.setup;
//
//import com.tungstun.barapi.data.SpringBarRepository;
//import com.tungstun.barapi.domain.bar.Bar;
//import com.tungstun.barapi.domain.bar.BarBuilder;
//import com.tungstun.barapi.domain.payment.Bill;
//import com.tungstun.barapi.domain.payment.BillFactory;
//import com.tungstun.barapi.domain.payment.Order;
//import com.tungstun.barapi.domain.person.Person;
//import com.tungstun.barapi.domain.person.PersonBuilder;
//import com.tungstun.barapi.domain.product.Category;
//import com.tungstun.barapi.domain.product.Product;
//import com.tungstun.barapi.domain.product.ProductBuilder;
//import com.tungstun.barapi.domain.product.ProductType;
//import com.tungstun.barapi.domain.session.Session;
//import com.tungstun.security.config.BarApiGlobalSecurityConfig;
//import com.tungstun.security.domain.user.Authorization;
//import com.tungstun.security.domain.user.Role;
//import com.tungstun.security.domain.user.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Import(BarApiGlobalSecurityConfig.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class BarIntegrationTestLifeCycle {
//    @Autowired
//    private SpringBarRepository barRepository;
//    public Person ownerPerson;
//    @Autowired
//    public MockMvc mockMvc;
//    public Bar bar;
//    public Person bartenderPerson;
//    public Person customerPerson;
//    public Person anonymousPerson;
//    @Autowired
//    private JpaRepository<User, Long> userRepository;
//    public Session session;
//    public Bill bill;
//    public Order order;
//    public Product product;
//    public Category category;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        barRepository.deleteAll();
//        userRepository.deleteAll();
//        createBar();
//        addProductToBar();
//        bar = barRepository.save(bar);
//        addUsersToBar();
//        createSessionsWithBills();
//        bar = barRepository.save(bar);
//        setTestClassVariables();
//
//    }
//    @AfterEach
//    void tearDown() {
//        barRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//
//    private void createBar() {
//        bar = new BarBuilder()
//                .setName("bar")
//                .setPhoneNumber("+0612345666")
//                .setMail("mail@testmail.com")
//                .setAddress("adressBar 1")
//                .build();
//    }
//
//    private void addProductToBar() {
//        category = new Category(123L, "Drinks", ProductType.DRINK);
//        bar.addCategory(category);
//        product = new ProductBuilder(123L, "product", category)
//                .setPrice(1.0)
//                .setSize(100)
//                .build();
//        bar.addProduct(product);
//    }
//
//    private void addUsersToBar() {
//        User owner = new User("owner", "", "", "", "", "+310612345678", new ArrayList<>(List.of(new Authorization(bar.getId(), Role.OWNER))));
//        owner = userRepository.save(owner);
//        owner = userRepository.getById(owner.getId());
//        ownerPerson = new PersonBuilder(123L, "ownerPerson").setUser(owner).build();
//        bar.addPerson(ownerPerson);
//
//        User bartender = new User("bartender", "", "", "", "", "+310612345678", new ArrayList<>(List.of(new Authorization(bar.getId(), Role.BARTENDER))));
//        bartender = userRepository.save(bartender);
//        bartender = userRepository.getById(bartender.getId());
//        bartenderPerson = new PersonBuilder(123L, "bartenderPerson").setUser(bartender).build();
//        bar.addPerson(bartenderPerson);
//
//        User customer = new User("customer", "", "", "", "", "+310612345678" ,new ArrayList<>(List.of(new Authorization(bar.getId(), Role.CUSTOMER))));
//        customer = userRepository.save(customer);
//        customer = userRepository.getById(customer.getId());
//        customerPerson = new PersonBuilder(123L, "customerPerson").setUser(customer).build();
//        bar.addPerson(customerPerson);
//
//        //Isnt this a connected person (person -> user) , Only connection is missing authorization i gueass (which is normal customer, no?
//        User user = userRepository.save(new User("anonymous", "ss", "mail@mail.com", "sam", "fisher", "+310612345678", new ArrayList<>()));
//        user = userRepository.getById(user.getId());
//        anonymousPerson = new PersonBuilder(123L, "anonymousPerson").setUser(user).build();
//        bar.addPerson(anonymousPerson);
//    }
//
//    private void createSessionsWithBills() {
//
//
//        session = Session.create(bar.getId(), "test");
//        bill = session.addCustomer(ownerPerson);
////        bill = new BillFactory(session, ownerPerson).create();
//        bill.addOrder(product, 1, ownerPerson);
////        session.addBill(bill);
//        bar.addSession(session);
//
//        Session session2 = Session.create(bar.getId(), "test2");
//        Bill bill2 = session2.addCustomer(ownerPerson);
////        Bill bill2 = new BillFactory(session2, ownerPerson).create();
//        bill2.addOrder(product, 1, ownerPerson);
////        session2.addBill(bill2);
//        session2.lock();
//        bar.addSession(session2);
//
//        Session session3 = Session.create(bar.getId(), "test3");
//        Bill bill3 = session3.addCustomer(ownerPerson);
////        Bill bill3 = new BillFactory(session3, ownerPerson).create();
////        session3.addBill(bill3);
//        session3.lock();
//        bar.addSession(session3);
//    }
//
//    private void setTestClassVariables() {
//        ownerPerson = bar.getPeople().stream().filter(person -> person.getName().equals("ownerPerson")).findFirst().orElseThrow();
//        bartenderPerson = bar.getPeople().stream().filter(person -> person.getName().equals("bartenderPerson")).findFirst().orElseThrow();
//        customerPerson = bar.getPeople().stream().filter(person -> person.getName().equals("customerPerson")).findFirst().orElseThrow();
//        anonymousPerson = bar.getPeople().stream().filter(person -> person.getName().equals("anonymousPerson")).findFirst().orElseThrow();
//        session = bar.getSessions().get(0);
//        bill = session.getBills().get(0);
//        order = bill.getOrders().get(0);
//        product = bar.getProducts().get(0);
//        category = bar.getCategories().get(0);
//    }
//
//}
