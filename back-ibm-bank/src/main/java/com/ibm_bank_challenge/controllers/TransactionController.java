package com.ibm_bank_challenge.controllers;

import com.ibm_bank_challenge.dtos.TransactionDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        transactionService.saveTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Transaction saved successfully"));
    }

    @GetMapping("/{customerId}")
    public Page<TransactionResponseDTO> getTransactionsByCustomerId(
            @PathVariable UUID customerId,
            Pageable pageable,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return transactionService.getTransactionsByCustomerId(customerId, pageable, month, year);
    }
}
