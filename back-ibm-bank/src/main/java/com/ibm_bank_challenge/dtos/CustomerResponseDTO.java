package com.ibm_bank_challenge.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CustomerResponseDTO(UUID id, String name, int age, String email, String accountNumber, String branch, String bankName, BigDecimal balance) {
}

