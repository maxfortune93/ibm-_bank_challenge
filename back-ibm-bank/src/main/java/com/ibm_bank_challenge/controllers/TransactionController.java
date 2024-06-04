package com.ibm_bank_challenge.controllers;

import com.ibm_bank_challenge.dtos.TransactionDTO;
import com.ibm_bank_challenge.dtos.TransactionResponseDTO;
import com.ibm_bank_challenge.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/transactions")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveTransaction(@RequestBody TransactionDTO transactionDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            transactionService.saveTransaction(transactionDTO);
            response.put("message", "Transaction saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{customerId}")
    public Page<TransactionResponseDTO> getTransactionsByCustomerId(
            @PathVariable UUID customerId,
            Pageable pageable,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return transactionService.getTransactionsByCustomerId(customerId, pageable,  month, year);
    }
}
