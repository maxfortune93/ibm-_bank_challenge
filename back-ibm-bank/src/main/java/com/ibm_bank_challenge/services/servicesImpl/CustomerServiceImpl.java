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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;

    @Override
    public Page<CustomerResponseDTO> listCustomers(Pageable pageable, String searchTerm) {
        Page<Customer> customerPage;
        if (searchTerm == null || searchTerm.isBlank()) {
            customerPage = customerRepository.findAll(pageable);
        } else {
            customerPage = customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm, pageable);
        }
        return customerPage.map(this::convertToResponseDTO);
    }

    @Override
    public List<CustomerResponseDTO> autocompleteCustomers(String query, int limit) {
        int size = Math.max(1, Math.min(limit, 50));
        return customerRepository.findByNameContainingIgnoreCase(query, PageRequest.of(0, size)).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);

        if (customerRepository.existsByAccountNumber(customer.getAccountNumber())) {
            throw new AlreadyExistsException("O número da conta já existe");
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new AlreadyExistsException("O email já existe");
        }

        try {
            return convertToResponseDTO(customerRepository.saveAndFlush(customer));
        } catch (DataIntegrityViolationException e) {
            // Race between the checks above and the insert: the unique constraints win.
            throw new AlreadyExistsException("E-mail ou número da conta já cadastrado");
        }
    }

    @Override
    public CustomerResponseDTO getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId) {
        return transactionService.getTransactionsById(customerId);
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

    private Customer convertToEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name().trim());
        customer.setAge(dto.age());
        customer.setEmail(dto.email().trim().toLowerCase());
        customer.setAccountNumber(dto.accountNumber().replace("-", ""));
        customer.setBranch(dto.branch());
        customer.setBankName(dto.bankName());
        customer.setBalance(BigDecimal.ZERO);
        return customer;
    }
}
