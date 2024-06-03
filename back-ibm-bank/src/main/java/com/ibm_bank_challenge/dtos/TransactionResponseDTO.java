package com.ibm_bank_challenge.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID senderId,
        String senderAccountNumber,
        UUID receiverId,
        String receiverAccountNumber,
        BigDecimal amount,
        String transactionType,
        LocalDateTime timestamp
) {
}
