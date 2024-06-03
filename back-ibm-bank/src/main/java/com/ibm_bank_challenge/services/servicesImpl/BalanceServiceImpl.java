package com.ibm_bank_challenge.services.servicesImpl;

import com.ibm_bank_challenge.domain.customer.Customer;
import com.ibm_bank_challenge.exception.InsufficientBalanceException;
import com.ibm_bank_challenge.services.BalanceService;
import com.ibm_bank_challenge.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Lazy
    @Autowired
    private CustomerService customerService;

    @Override
    public Customer updateBalanceForDeposit(UUID customerId, BigDecimal amount) throws Exception {
        Customer customer = customerService.getCustomerEntityById(customerId);
        BigDecimal customerBalance = customer.getBalance();

        customer.setBalance(customerBalance.add(amount));
        customerService.saveCustomer(customer);
        return customer;
    }

    @Override
    public Customer updateBalanceForWithdrawal(UUID customerId, BigDecimal amount) throws Exception {
        Customer customer = customerService.getCustomerEntityById(customerId);
        BigDecimal customerBalance = customer.getBalance();

        if (customerBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }

        customer.setBalance(customerBalance.subtract(amount));
        customerService.saveCustomer(customer);
        return customer;
    }

    @Override
    public Map<String, Customer> updateBalancesForTransfer(UUID senderId, UUID receiverId, BigDecimal amount) throws Exception {
        Customer sender = customerService.getCustomerEntityById(senderId);
        Customer receiver = customerService.getCustomerEntityById(receiverId);

        BigDecimal senderBalance = sender.getBalance();
        BigDecimal receiverBalance = receiver.getBalance();

        if (senderBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }

        sender.setBalance(senderBalance.subtract(amount));
        receiver.setBalance(receiverBalance.add(amount));

        customerService.saveCustomer(sender);
        customerService.saveCustomer(receiver);

        Map<String, Customer> participants = new HashMap<>();
        participants.put("sender", sender);
        participants.put("receiver", receiver);

        return participants;
    }
}
