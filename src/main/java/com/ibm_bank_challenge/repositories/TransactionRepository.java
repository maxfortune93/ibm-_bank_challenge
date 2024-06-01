package com.ibm_bank_challenge.repositories;

import com.ibm_bank_challenge.domain.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByCustomerId(UUID customerId);
}
