package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.security.application.UserService;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserBarAuthorization;
import com.tungstun.security.data.model.UserRole;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarService {
    private final SpringBarRepository SPRING_BAR_REPOSITORY;
    private final UserService USER_SERVICE;
    private final SpringPersonRepository SPRING_USER_REPOSITORY;

    public BarService(SpringBarRepository springBarRepository, UserService userService, SpringPersonRepository springPersonRepository) {
        this.SPRING_BAR_REPOSITORY = springBarRepository;
        this.USER_SERVICE = userService;
        this.SPRING_USER_REPOSITORY = springPersonRepository;
    }

    /**
     * Checks if bar with name already exists in datastore.
     * If bar with name already exists, then throws exeption.
     * @exception DuplicateRequestException if bar with given name already exists
     */
    private void checkIfBarExists(String name){
        SPRING_BAR_REPOSITORY.findBarByName(name).ifPresent(error ->{
            throw new DuplicateRequestException(
                    String.format("Bar with name %s already exists", name)); });
    }

    /**
     * Returns a List of all existing bars.
     * @return list with all existing bars
     * @exception NotFoundException if no bars are found
     */
    public List<Bar> getAllBars() throws NotFoundException{
        List<Bar> bars = this.SPRING_BAR_REPOSITORY.findAll();
        if (bars.isEmpty()) throw new NotFoundException("There are no bars available");
        return bars;
    }

    /**
     * Returns bar with given id.
     * @return Bar
     * @exception NotFoundException if no bar was found with {@param id}
     */
    public Bar getBar(Long id) throws NotFoundException {
        return this.SPRING_BAR_REPOSITORY.findById(id)
         .orElseThrow(() -> new NotFoundException(
                 String.format("bar with id %s doesn't exist", id)) );
    }

    /**
     * Will create a new bar with given bar information and return if succesfull.
     * @return created bar
     */
    public Bar addBar(String adres, String name, String mail, String phoneNumber, String username) {
        checkIfBarExists(name);
        Bar bar = new BarBuilder()
                .setAdres(adres)
                .setName(name)
                .setMail(mail)
                .setPhoneNumber(phoneNumber)
                .build();
        bar = this.SPRING_BAR_REPOSITORY.save(bar);
        User user = (User) this.USER_SERVICE.loadUserByUsername(username);
        user.addUserBarAuthorizations(new UserBarAuthorization(bar.getId(), user.getId(), UserRole.ROLE_BAR_OWNER));
        Bartender bartender = new Bartender(username);
        bartender.setUser(user);
        bar.addUser(bartender);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    /**
     * Edits bar object with all values that are not null. Returns altered bar, if succesfull.
     * @return edited bar
     */
    public Bar editBar(Long id, String address, String name, String mail, String phoneNumber) throws NotFoundException {
        Bar bar = getBar(id);
        if (address != null) bar.setAddress(address);
        if (name != null) bar.setName(name);
        if (mail != null) bar.setMail(mail);
        if (phoneNumber != null) bar.setPhoneNumber(phoneNumber);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    /**
     * Persists a Bar object
     * @return saved bar
     */
    public Bar saveBar(Bar bar){ return this.SPRING_BAR_REPOSITORY.save(bar); }

    /**
     * Deletes bar with given id
     */
    public void deleteBar(Long id) throws NotFoundException { this.SPRING_BAR_REPOSITORY.delete(getBar(id)); }
}
