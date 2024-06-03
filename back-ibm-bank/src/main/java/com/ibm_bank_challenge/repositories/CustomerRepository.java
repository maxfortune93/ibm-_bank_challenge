package com.ibm_bank_challenge.repositories;

import com.ibm_bank_challenge.domain.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Page<Customer> findAll(Pageable pageable);

    Customer findByAccountNumber(String accountNumber);
    Customer findByEmail(String email);

    List<Customer> findByNameContainingIgnoreCase(String query);

    Page<Customer> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

}
