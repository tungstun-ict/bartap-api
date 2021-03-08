package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarService {
    private final SpringBarRepository SPRING_BAR_REPOSITORY;

    public BarService(SpringBarRepository SPRING_BAR_REPOSITORY) {
        this.SPRING_BAR_REPOSITORY = SPRING_BAR_REPOSITORY;
    }

    public List<Bar> getAllBars() throws NotFoundException{
        List<Bar> bars = this.SPRING_BAR_REPOSITORY.findAll();
        if (bars.isEmpty()) throw new NotFoundException("There are no bars available");
        return bars;
    }

    public Bar getBar(Long id) throws NotFoundException {
        return this.SPRING_BAR_REPOSITORY.findById(id)
         .orElseThrow(() -> new NotFoundException(
                 String.format("Bar with id %s doesn't exist", id)) );
    }

    public Bar addBar(BarRequest barRequest) {
        checkIfBarExists(barRequest.name);
        Bar bar = new BarBuilder()
                .setAdres(barRequest.address)
                .setName(barRequest.name)
                .setMail(barRequest.mail)
                .setPhoneNumber(barRequest.phoneNumber)
                .build();
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    private void checkIfBarExists(String name){
        SPRING_BAR_REPOSITORY.findBarByName(name).ifPresent(error ->{
            throw new DuplicateRequestException(
                    String.format("Bar with name %s already exists", name)); });
    }

    public Bar updateBar(Long id, BarRequest barRequest) throws NotFoundException {
        Bar bar = getBar(id);
        bar.setAddress(barRequest.address);
        bar.setMail(barRequest.mail);
        bar.setName(barRequest.name);
        bar.setPhoneNumber(barRequest.phoneNumber);
        return this.SPRING_BAR_REPOSITORY.save(bar);
    }

    public Bar saveBar(Bar bar){ return this.SPRING_BAR_REPOSITORY.save(bar); }

    public void deleteBar(Long id) throws NotFoundException { this.SPRING_BAR_REPOSITORY.delete(getBar(id)); }
}
