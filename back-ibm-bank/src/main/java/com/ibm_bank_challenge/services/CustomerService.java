package com.ibm_bank_challenge.services;
//
//import com.ibm_bank_challenge.domain.Transaction.Transaction;
//import com.ibm_bank_challenge.domain.customer.Customer;
//import com.ibm_bank_challenge.repositories.CustomerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class CustomerService {
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private TransactionService transactionService;
//
//    public List<Customer> listCustomers() {
//        return this.customerRepository.findAll();
//    }
//
//    public Customer createCustomer(Customer customer) {
//        customer.setAccountNumber(normalizeAccountNumber(customer.getAccountNumber()));
//        customer.setBalance(BigDecimal.ZERO);
//        if (isAccountNumberUnique(customer.getAccountNumber())) {
//            this.saveCustomer(customer);
//        } else {
//            throw new IllegalArgumentException("Account number already exists");
//        }
//        return customer;
//    }
//
//    public void saveCustomer(Customer customer){ this.customerRepository.save(customer); }
//
//    public Customer getCustomerById(UUID id) throws Exception {
//        return this.customerRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
//    }
//
////    public void deleteCustomer(UUID id) {
////        customerRepository.deleteById(id);
////    }
//
//    public List<Transaction> getTransactionsByCustomerId(UUID customerId) {
//        return transactionService.getTransactionsById(customerId);
//    }
//
//    private String normalizeAccountNumber(String accountNumber) {
//        return accountNumber.replaceAll("-", "");
//    }
//
//    private boolean isAccountNumberUnique(String accountNumber) {
//        return this.customerRepository.findByAccountNumber(accountNumber) == null;
//    }
//}

import com.ibm_bank_challenge.domain.customer.Customer;
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
    CustomerResponseDTO getCustomerById(UUID id) throws Exception;
    List<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId);

    Customer getCustomerEntityById(UUID id) throws Exception;

    void saveCustomer(Customer customer);

    List<CustomerResponseDTO> autocompleteCustomers(String query, int limit);
}
