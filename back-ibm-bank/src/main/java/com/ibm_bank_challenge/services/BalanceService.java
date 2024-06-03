package com.ibm_bank_challenge.services;

//import com.ibm_bank_challenge.domain.customer.Customer;
//import com.ibm_bank_challenge.repositories.CustomerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//@Service
//public class BalanceService {
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    public void updateBalance(UUID customerId, BigDecimal amount, boolean isCredit) throws Exception {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new Exception("Customer not found"));
//
//        BigDecimal currentBalance = customer.getBalance();
//
//        if (isCredit) {
//            if (currentBalance.compareTo(amount) < 0) {
//                throw new IllegalArgumentException("Insufficient balance");
//            }
//            customer.setBalance(currentBalance.subtract(amount));
//        } else {
//            customer.setBalance(currentBalance.add(amount));
//        }
//
//        customerRepository.save(customer);
//    }
//
//    public BigDecimal getBalance(UUID customerId) throws Exception {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new Exception("Customer not found"));
//        return customer.getBalance();
//    }
//}

import com.ibm_bank_challenge.domain.customer.Customer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface BalanceService {
    Customer updateBalanceForDeposit(UUID customerId, BigDecimal amount) throws Exception;
    Customer updateBalanceForWithdrawal(UUID customerId, BigDecimal amount) throws Exception;
    Map<String, Customer> updateBalancesForTransfer(UUID senderId, UUID receiverId, BigDecimal amount) throws Exception;
}
