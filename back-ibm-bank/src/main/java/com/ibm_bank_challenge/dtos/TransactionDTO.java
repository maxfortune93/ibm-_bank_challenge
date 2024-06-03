package com.ibm_bank_challenge.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(UUID senderId, UUID receiverId, BigDecimal amount, String transactionType) {
}
