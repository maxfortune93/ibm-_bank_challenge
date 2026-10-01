package com.ibm_bank_challenge.services;

import com.ibm_bank_challenge.dtos.CustomerRequestDTO;
import com.ibm_bank_challenge.dtos.CustomerResponseDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Page<CustomerResponseDTO> listCustomers(Pageable pageable, String searchTerm);

    CustomerResponseDTO createCustomer(CustomerRequestDTO customerDTO);

    CustomerResponseDTO getCustomerById(UUID id);

    List<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId);

    List<CustomerResponseDTO> autocompleteCustomers(String query, int limit);
}
