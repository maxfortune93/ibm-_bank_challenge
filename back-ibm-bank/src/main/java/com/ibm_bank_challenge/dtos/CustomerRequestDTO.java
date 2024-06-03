package com.ibm_bank_challenge.dtos;

import java.math.BigDecimal;

public record CustomerRequestDTO(String name, int age, String email, String accountNumber, String branch, String bankName, BigDecimal balance) {
}
