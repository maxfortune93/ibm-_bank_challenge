package com.ibm_bank_challenge.repositories;

import com.ibm_bank_challenge.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer>findAll();

    Customer findByAccountNumber(String accountNumber);

}
