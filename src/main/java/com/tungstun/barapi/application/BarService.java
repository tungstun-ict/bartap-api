package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.*;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarService {
    SpringBarRepository SPRING_BAR_REPOSITORY;

    public BarService(SpringBarRepository springBarRepository) { this.SPRING_BAR_REPOSITORY = springBarRepository; }

    /**
     * Checks if bar with name already exists in datastore.
     * If bar with name already exists, then throws exeption.
     * @param name bar name
     * @exception DuplicateRequestException if bar with given name already exists
     */
    private void checkIfBarExists(String name){
        SPRING_BAR_REPOSITORY.findBarByName(name).ifPresent(error ->{
            throw new DuplicateRequestException(
                    String.format("Bar with name %s already exists", name)); });
    }


    /**
     * Returns a List of all existing bars.
     * @return list of bars
     * @exception NotFoundException if no bars are found
     */
    public List<Bar> getAllBars() throws NotFoundException{
        List<Bar> bars = this.SPRING_BAR_REPOSITORY.findAll();
        if (bars.isEmpty()) throw new NotFoundException("There are no bars available");
        return bars;
    }

    /**
     * Returns bar with given id.
     * @param id id of bar
     * @return Bar
     * @exception NotFoundException if no bar was found with {@param id}
     */
    public Bar getBar(Long id) throws NotFoundException {
        return this.SPRING_BAR_REPOSITORY.findById(id)
         .orElseThrow(() -> new NotFoundException("bar with id " + id + " doesn't exist"));
    }

    /**
     * Will create a new bar with given bar information and return if succesfull.
     * @return created bar
     */
    public Bar addBar(String adres, String name, String mail, String phoneNumber) {
        checkIfBarExists(name);
        Bar bar = new BarBuilder()
                .setAdres(adres)
                .setName(name)
                .setMail(mail)
                .setPhoneNumber(phoneNumber)
                .build();
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    /**
     * Edits bar information and saves it. Returns altered bar, if succesfull.
     * @return edited bar
     */
    public Bar editBar(Long id, String adres, String name, String mail, String phoneNumber) throws NotFoundException {
        Bar bar = getBar(id);

        if (adres != null) bar.setAdres(adres);
        if (name != null) bar.setName(name);
        if (mail != null) bar.setMail(mail);
        if (phoneNumber != null) bar.setPhoneNumber(phoneNumber);

        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    /**
     * Deletes bar with given id
     */
    public void deleteBar(Long id) throws NotFoundException {
        this.SPRING_BAR_REPOSITORY.delete(getBar(id));
    }
}
