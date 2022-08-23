package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.payment.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface SpringBillRepository extends JpaRepository<Bill, UUID>{
    Optional<Bill> findById(Long id);
}