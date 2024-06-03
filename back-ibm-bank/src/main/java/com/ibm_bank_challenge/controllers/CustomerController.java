package com.ibm_bank_challenge.controllers;

import com.ibm_bank_challenge.dtos.CustomerRequestDTO;
import com.ibm_bank_challenge.dtos.CustomerResponseDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/customers")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Page<CustomerResponseDTO> listCustomers(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return this.customerService.listCustomers(pageable, searchTerm);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> saveCustomer(@RequestBody CustomerRequestDTO customerDTO) {
        return ResponseEntity.ok(this.customerService.createCustomer(customerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable UUID id) throws Exception {
        CustomerResponseDTO customer = this.customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponseDTO> getTransactionsByCustomerId(@PathVariable UUID id) {
        return this.customerService.getTransactionsByCustomerId(id);
    }

    @GetMapping("/autocomplete")
    public List<CustomerResponseDTO> autocompleteCustomers(@RequestParam String query, @RequestParam int limit) {
        return customerService.autocompleteCustomers(query, limit);
    }

}
