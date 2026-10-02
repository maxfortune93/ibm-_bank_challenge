package com.ibm_bank_challenge.services.servicesImpl;

import com.ibm_bank_challenge.domain.customer.Customer;
import com.ibm_bank_challenge.exception.InsufficientBalanceException;
import com.ibm_bank_challenge.exception.ResourceNotFoundException;
import com.ibm_bank_challenge.repositories.CustomerRepository;
import com.ibm_bank_challenge.services.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Balance changes run inside a transaction and lock the customer rows
 * (SELECT ... FOR UPDATE), so concurrent operations cannot overdraw an account.
 */
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer deposit(UUID customerId, BigDecimal amount) {
        Customer customer = lock(customerId);
        customer.setBalance(customer.getBalance().add(amount));
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer withdraw(UUID customerId, BigDecimal amount) {
        Customer customer = lock(customerId);
        debit(customer, amount);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer[] transfer(UUID senderId, UUID receiverId, BigDecimal amount) {
        // Always lock in the same order (by id) to avoid deadlocks between opposite transfers.
        boolean senderFirst = senderId.compareTo(receiverId) < 0;
        Customer first = lock(senderFirst ? senderId : receiverId);
        Customer second = lock(senderFirst ? receiverId : senderId);
        Customer sender = senderFirst ? first : second;
        Customer receiver = senderFirst ? second : first;

        debit(sender, amount);
        receiver.setBalance(receiver.getBalance().add(amount));

        customerRepository.save(sender);
        customerRepository.save(receiver);
        return new Customer[]{sender, receiver};
    }

    private Customer lock(UUID id) {
        return customerRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private void debit(Customer customer, BigDecimal amount) {
        if (customer.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }
        customer.setBalance(customer.getBalance().subtract(amount));
    }
}
