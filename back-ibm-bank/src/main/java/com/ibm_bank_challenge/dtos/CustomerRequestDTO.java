package com.ibm_bank_challenge.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(
        @NotBlank(message = "Nome é obrigatório") @Size(max = 255) String name,
        @Min(value = 0, message = "Idade inválida") @Max(value = 150, message = "Idade inválida") int age,
        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") @Size(max = 80) String email,
        @NotBlank(message = "Número da conta é obrigatório") @Size(max = 20) String accountNumber,
        @NotBlank(message = "Agência é obrigatória") @Size(max = 20) String branch,
        @NotBlank(message = "Banco é obrigatório") @Size(max = 255) String bankName
) {
}
