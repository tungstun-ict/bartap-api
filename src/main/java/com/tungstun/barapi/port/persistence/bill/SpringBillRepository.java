package com.tungstun.barapi.port.persistence.bill;

import com.tungstun.barapi.domain.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringBillRepository extends JpaRepository<Bill, UUID> {
    @Query(
            nativeQuery = true,
            value = "Select * from bill, session, bar_sessions " +
                    "where bill.session_id = session.id " +
                    "and session.id = bar_sessions.sessions_id " +
                    "and bar_sessions.bar_id = ?1 " +
                    "and bill.customer_id = ?2"
    )
    List<Bill> findByBarAndPerson(UUID barId, UUID personId);
}
