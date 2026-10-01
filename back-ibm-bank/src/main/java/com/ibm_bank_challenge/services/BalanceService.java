package com.ibm_bank_challenge.services;

import com.ibm_bank_challenge.domain.customer.Customer;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceService {
    Customer deposit(UUID customerId, BigDecimal amount);

    Customer withdraw(UUID customerId, BigDecimal amount);

    /** Debits the sender and credits the receiver; returns {sender, receiver}. */
    Customer[] transfer(UUID senderId, UUID receiverId, BigDecimal amount);
}
