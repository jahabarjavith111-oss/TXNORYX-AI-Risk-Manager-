package com.txnoryx.backend.controller;

import com.txnoryx.backend.dto.CreateTransactionRequest;
import com.txnoryx.backend.dto.SimulateRequest;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.model.TransactionEvent;
import com.txnoryx.backend.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(
            @RequestBody CreateTransactionRequest request) {


        return transactionService.createTransaction(request);
    }


    @PostMapping("/simulate")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction simulateTransaction(
            @RequestBody SimulateRequest request) {


        return transactionService.simulateTransaction(request);
    }


    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }


    @GetMapping("/{transactionId}")
    public Transaction getTransaction(
            @PathVariable String transactionId) {


        return transactionService.getTransaction(transactionId);
    }


    @GetMapping("/{transactionId}/history")
    public List<TransactionEvent> getHistory(
            @PathVariable String transactionId) {


        return transactionService
                .getTransactionHistory(transactionId);
    }
}