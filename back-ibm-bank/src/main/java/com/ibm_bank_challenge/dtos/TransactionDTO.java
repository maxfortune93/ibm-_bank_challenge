package com.ibm_bank_challenge.dtos;

import com.ibm_bank_challenge.domain.transaction.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(
        UUID senderId,
        UUID receiverId,
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        @Digits(integer = 17, fraction = 2, message = "Valor inválido")
        BigDecimal amount,
        @NotNull(message = "Tipo de transação é obrigatório") TransactionType transactionType
) {
}
