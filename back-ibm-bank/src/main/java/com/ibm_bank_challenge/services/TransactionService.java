package com.ibm_bank_challenge.services;

import com.ibm_bank_challenge.dtos.TransactionDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO) throws Exception;
    List<TransactionResponseDTO> getTransactionsById(UUID customerId);

    Page<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId, Pageable pageable,Integer month, Integer year);
}
