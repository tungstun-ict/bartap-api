package com.tungstun.barapi.port.persistence.bill;

import com.tungstun.barapi.domain.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringBillRepository extends JpaRepository<Bill, UUID> {
}
