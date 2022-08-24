package com.tungstun.barapi.domain.bill;

import java.util.Optional;
import java.util.UUID;

public interface BillRepository {
    Bill save(Bill bill);

    Bill update(Bill bill);

    void delete(UUID id);

    Optional<Bill> findById(UUID id);

//    Optional<Bill> findByIdAndBarId(UUID id, UUID barId);
}
