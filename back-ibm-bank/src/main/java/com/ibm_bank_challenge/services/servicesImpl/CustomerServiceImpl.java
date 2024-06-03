package com.ibm_bank_challenge.services.servicesImpl;

import com.ibm_bank_challenge.domain.customer.Customer;
import com.ibm_bank_challenge.dtos.CustomerRequestDTO;
import com.ibm_bank_challenge.dtos.CustomerResponseDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.exception.AlreadyExistsException;
import com.ibm_bank_challenge.exception.ResourceNotFoundException;
import com.ibm_bank_challenge.repositories.CustomerRepository;
import com.ibm_bank_challenge.services.CustomerService;
import com.ibm_bank_challenge.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public Page<CustomerResponseDTO> listCustomers(Pageable pageable, String searchTerm) {
        Page<Customer> customerPage;
        if (searchTerm == null || searchTerm.isEmpty()) {
            customerPage = customerRepository.findAll(pageable);
        } else {
            customerPage = customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm, pageable);
        }
        return customerPage.map(this::convertToResponseDTO);
    }

    @Override
    public List<CustomerResponseDTO> autocompleteCustomers(String query, int limit) {
        return customerRepository.findByNameContainingIgnoreCase(query).stream()
                .limit(limit)
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        customer.setAccountNumber(normalizeAccountNumber(customer.getAccountNumber()));
        customer.setBalance(BigDecimal.ZERO);
        if (!isAccountNumberUnique(customer.getAccountNumber())) {
            throw new AlreadyExistsException("O número da conta já existe\"");
        }

        if (!isEmailUnique(customer.getEmail())) {
            throw new AlreadyExistsException("O email já existe");
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponseDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomerById(UUID id) throws Exception {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return convertToResponseDTO(customer);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId) {
        return transactionService.getTransactionsById(customerId);
    }

    public void saveCustomer(Customer customer) {
        this.customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerEntityById(UUID id) throws Exception {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private String normalizeAccountNumber(String accountNumber) {
        return accountNumber.replaceAll("-", "");
    }

    private boolean isAccountNumberUnique(String accountNumber) {
        return this.customerRepository.findByAccountNumber(accountNumber) == null;
    }

    private boolean isEmailUnique(String email) {
        return this.customerRepository.findByEmail(email) == null;
    }

    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getAge(),
                customer.getEmail(),
                customer.getAccountNumber(),
                customer.getBranch(),
                customer.getBankName(),
                customer.getBalance()
        );
    }

    private Customer convertToEntity(CustomerRequestDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.name());
        customer.setAge(customerDTO.age());
        customer.setEmail(customerDTO.email());
        customer.setAccountNumber(customerDTO.accountNumber());
        customer.setBranch(customerDTO.branch());
        customer.setBankName(customerDTO.bankName());
        return customer;
    }
}
