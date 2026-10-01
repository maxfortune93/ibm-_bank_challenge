package com.ibm_bank_challenge.services;

import com.ibm_bank_challenge.domain.customer.Customer;
import com.ibm_bank_challenge.exception.InsufficientBalanceException;
import com.ibm_bank_challenge.exception.ResourceNotFoundException;
import com.ibm_bank_challenge.repositories.CustomerRepository;
import com.ibm_bank_challenge.services.servicesImpl.BalanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    private Customer alice;
    private Customer bob;

    @BeforeEach
    void setUp() {
        alice = customer("100.00");
        bob = customer("50.00");
    }

    @Test
    void depositIncreasesBalance() {
        mockLock(alice);
        when(customerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Customer result = balanceService.deposit(alice.getId(), new BigDecimal("25.50"));

        assertEquals(new BigDecimal("125.50"), result.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        mockLock(alice);
        when(customerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Customer result = balanceService.withdraw(alice.getId(), new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), result.getBalance());
    }

    @Test
    void withdrawFailsWhenBalanceIsInsufficient() {
        mockLock(alice);

        assertThrows(InsufficientBalanceException.class,
                () -> balanceService.withdraw(alice.getId(), new BigDecimal("100.01")));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void transferMovesMoneyBetweenAccounts() {
        mockLock(alice);
        mockLock(bob);
        when(customerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Customer[] result = balanceService.transfer(alice.getId(), bob.getId(), new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), result[0].getBalance());
        assertEquals(new BigDecimal("80.00"), result[1].getBalance());
    }

    @Test
    void transferFailsWhenSenderHasNoFunds() {
        mockLock(alice);
        mockLock(bob);

        assertThrows(InsufficientBalanceException.class,
                () -> balanceService.transfer(alice.getId(), bob.getId(), new BigDecimal("500.00")));
        assertEquals(new BigDecimal("50.00"), bob.getBalance());
    }

    @Test
    void unknownCustomerIsReported() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findByIdForUpdate(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> balanceService.deposit(id, BigDecimal.ONE));
    }

    private void mockLock(Customer customer) {
        when(customerRepository.findByIdForUpdate(customer.getId())).thenReturn(Optional.of(customer));
    }

    private Customer customer(String balance) {
        Customer c = new Customer();
        c.setId(UUID.randomUUID());
        c.setBalance(new BigDecimal(balance));
        return c;
    }
}
