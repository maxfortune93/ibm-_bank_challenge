package com.ibm_bank_challenge.services.servicesImpl;

import com.ibm_bank_challenge.domain.customer.Customer;
import com.ibm_bank_challenge.domain.transaction.Transaction;
import com.ibm_bank_challenge.dtos.TransactionDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.exception.BusinessException;
import com.ibm_bank_challenge.repositories.TransactionRepository;
import com.ibm_bank_challenge.services.BalanceService;
import com.ibm_bank_challenge.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BalanceService balanceService;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void saveTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(dto.transactionType());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAmount(dto.amount());

        switch (dto.transactionType()) {
            case DEPOSIT -> {
                requireId(dto.receiverId(), "Cliente de destino é obrigatório");
                transaction.setReceiver(balanceService.deposit(dto.receiverId(), dto.amount()));
            }
            case WITHDRAWAL -> {
                requireId(dto.senderId(), "Cliente de origem é obrigatório");
                transaction.setSender(balanceService.withdraw(dto.senderId(), dto.amount()));
            }
            case TRANSFER -> {
                requireId(dto.senderId(), "Cliente de origem é obrigatório");
                requireId(dto.receiverId(), "Cliente de destino é obrigatório");
                if (dto.senderId().equals(dto.receiverId())) {
                    throw new BusinessException("Não é possível transferir para a mesma conta");
                }
                Customer[] participants = balanceService.transfer(dto.senderId(), dto.receiverId(), dto.amount());
                transaction.setSender(participants[0]);
                transaction.setReceiver(participants[1]);
            }
        }

        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsById(UUID customerId) {
        return transactionRepository.findBySenderIdOrReceiverId(customerId, customerId).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionsByCustomerId(UUID customerId, Pageable pageable, Integer month, Integer year) {
        if (month != null && year != null) {
            if (month < 1 || month > 12) {
                throw new BusinessException("Mês inválido");
            }
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endExclusive = yearMonth.plusMonths(1).atDay(1).atStartOfDay();
            return transactionRepository
                    .findByCustomerAndPeriod(customerId, start, endExclusive, pageable)
                    .map(this::convertToResponseDTO);
        }

        return transactionRepository.findBySenderIdOrReceiverId(customerId, customerId, pageable)
                .map(this::convertToResponseDTO);
    }

    private void requireId(UUID id, String message) {
        if (id == null) {
            throw new BusinessException(message);
        }
    }

    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        Customer sender = transaction.getSender();
        Customer receiver = transaction.getReceiver();
        return new TransactionResponseDTO(
                sender != null ? sender.getId() : null,
                sender != null ? sender.getAccountNumber() : null,
                receiver != null ? receiver.getId() : null,
                receiver != null ? receiver.getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getTimestamp()
        );
    }
}
