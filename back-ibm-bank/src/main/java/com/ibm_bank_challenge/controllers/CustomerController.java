package com.ibm_bank_challenge.controllers;

import com.ibm_bank_challenge.dtos.CustomerRequestDTO;
import com.ibm_bank_challenge.dtos.CustomerResponseDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Page<CustomerResponseDTO> listCustomers(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return customerService.listCustomers(pageable, searchTerm);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> saveCustomer(@Valid @RequestBody CustomerRequestDTO customerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerDTO));
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponseDTO> getTransactionsByCustomerId(@PathVariable UUID id) {
        return customerService.getTransactionsByCustomerId(id);
    }

    @GetMapping("/autocomplete")
    public List<CustomerResponseDTO> autocompleteCustomers(@RequestParam String query, @RequestParam(defaultValue = "5") int limit) {
        return customerService.autocompleteCustomers(query, limit);
    }
}
