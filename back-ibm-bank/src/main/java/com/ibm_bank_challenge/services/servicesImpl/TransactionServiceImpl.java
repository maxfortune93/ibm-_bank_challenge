package com.ibm_bank_challenge.services.servicesImpl;

import com.ibm_bank_challenge.domain.Transaction.Transaction;
import com.ibm_bank_challenge.domain.Transaction.TransactionType;
import com.ibm_bank_challenge.domain.customer.Customer;
import com.ibm_bank_challenge.dtos.TransactionDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.repositories.TransactionRepository;
import com.ibm_bank_challenge.services.BalanceService;
import com.ibm_bank_challenge.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) throws Exception {

        Customer sender = null;
        Customer receiver = null;

        if (TransactionType.TRANSFER.name().equals(transactionDTO.transactionType())) {
            Map<String, Customer> participants = balanceService.updateBalancesForTransfer(transactionDTO.senderId(), transactionDTO.receiverId(), transactionDTO.amount());
            sender = participants.get("sender");
            receiver = participants.get("receiver");
        } else if (TransactionType.DEPOSIT.name().equals(transactionDTO.transactionType())) {
            receiver = balanceService.updateBalanceForDeposit(transactionDTO.receiverId(), transactionDTO.amount());
        } else if (TransactionType.WITHDRAWAL.name().equals(transactionDTO.transactionType())) {
            sender = balanceService.updateBalanceForWithdrawal(transactionDTO.senderId(), transactionDTO.amount());
        } else {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionDTO.transactionType());
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.valueOf(transactionDTO.transactionType()));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAmount(transactionDTO.amount());

        if (TransactionType.TRANSFER.name().equals(transactionDTO.transactionType())) {
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
        } else if (TransactionType.DEPOSIT.name().equals(transactionDTO.transactionType())) {
            transaction.setReceiver(receiver);
        } else if (TransactionType.WITHDRAWAL.name().equals(transactionDTO.transactionType())) {
            transaction.setSender(sender);
        }

        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsById(UUID customerId) {
        return transactionRepository.findBySenderIdOrReceiverId(customerId, customerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId, Pageable pageable) {
        return transactionRepository.findBySenderIdOrReceiverId(customerId, customerId, pageable)
                .map(this::convertToResponseDTO);
    }

    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getSender() != null ? transaction.getSender().getId() : null,
                transaction.getSender() != null ? transaction.getSender().getAccountNumber() : null,
                transaction.getReceiver() != null ? transaction.getReceiver().getId() : null,
                transaction.getReceiver() != null ? transaction.getReceiver().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getTimestamp()
        );
    }
}

