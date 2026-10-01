package com.ibm_bank_challenge.repositories;

import com.ibm_bank_challenge.domain.customer.Customer;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmail(String email);

    List<Customer> findByNameContainingIgnoreCase(String query, Pageable pageable);

    Page<Customer> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM customers c WHERE c.id = :id")
    Optional<Customer> findByIdForUpdate(@Param("id") UUID id);
}
