package com.tungstun.barapi.port.persistence.bill;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.BillRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultBillRepository implements BillRepository {
    private final SpringBillRepository repository;

    public DefaultBillRepository(SpringBillRepository repository) {
        this.repository = repository;
    }

    @Override
    public Bill save(Bill bill) {
        return repository.save(bill);
    }

    @Override
    public Bill update(Bill bill) {
        return repository.save(bill);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Bill> findById(UUID id) {
        return repository.findById(id);
    }

//    @Override
//    public Optional<Bill> findByIdAndBarId(UUID id, UUID barId) {
//        return repository.findByIdAndBarId(id, barId);
//    }
}
