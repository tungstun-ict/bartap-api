package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.payment.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringBillRepository extends JpaRepository<Bill, Long>{
    Optional<Bill> findById(Long id);
}