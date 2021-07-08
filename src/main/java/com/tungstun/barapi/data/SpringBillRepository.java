package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.payment.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringBillRepository extends JpaRepository<Bill, Long>{
    Optional<Bill> findById(Long id);
}