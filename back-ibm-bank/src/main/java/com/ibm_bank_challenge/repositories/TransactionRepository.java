package com.ibm_bank_challenge.repositories;

import com.ibm_bank_challenge.domain.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySenderIdOrReceiverId(UUID senderId, UUID receiverId);

    Page<Transaction> findBySenderIdOrReceiverId(UUID senderId, UUID receiverId, Pageable pageable);

    @Query("SELECT t FROM transactions t WHERE (t.sender.id = :customerId OR t.receiver.id = :customerId) AND t.timestamp >= :start AND t.timestamp < :endExclusive")
    Page<Transaction> findByCustomerAndPeriod(
            @Param("customerId") UUID customerId,
            @Param("start") LocalDateTime start,
            @Param("endExclusive") LocalDateTime endExclusive,
            Pageable pageable
    );
}
