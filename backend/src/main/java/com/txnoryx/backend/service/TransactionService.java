package com.txnoryx.backend.service;

import com.txnoryx.backend.dto.CreateTransactionRequest;
import com.txnoryx.backend.dto.SimulateRequest;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.model.TransactionEvent;
import com.txnoryx.backend.repository.TransactionEventRepository;
import com.txnoryx.backend.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventRepository eventRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionEventRepository eventRepository) {
        this.transactionRepository = transactionRepository;
        this.eventRepository = eventRepository;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {

        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setUserId(request.getUserId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setMerchant(request.getMerchant());
        transaction.setFailureReason(request.getFailureReason());
        transaction.setDeviceId(request.getDeviceId());
        transaction.setLocation(request.getLocation());
        transaction.setStatus(
                request.getStatus() != null ? request.getStatus() : "PENDING");
        transaction.setCreatedAt(LocalDateTime.now());


        Transaction saved = transactionRepository.save(transaction);


        TransactionEvent event = new TransactionEvent();
        event.setTransactionId(saved.getTransactionId());
        event.setEventType("TRANSACTION_CREATED");
        event.setDescription("Transaction created");
        event.setTimestamp(LocalDateTime.now());


        eventRepository.save(event);


        return saved;
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    public Transaction simulateTransaction(SimulateRequest request) {

        String scenario = request.getScenario();

        Transaction transaction = new Transaction();
        transaction.setTransactionId(
                "TXN-SIM-" + UUID.randomUUID().toString()
                        .substring(0, 8).toUpperCase());
        transaction.setUserId(1L);
        transaction.setAmount(new java.math.BigDecimal("1499.00"));
        transaction.setCurrency("INR");
        transaction.setPaymentMethod("UPI");
        transaction.setMerchant("Simulated Merchant");
        transaction.setStatus("FAILED");
        transaction.setFailureReason(scenario);
        transaction.setDeviceId("SIM-DEVICE");
        transaction.setLocation("Simulation");
        transaction.setCreatedAt(LocalDateTime.now());


        Transaction saved = transactionRepository.save(transaction);


        TransactionEvent createdEvent = new TransactionEvent();
        createdEvent.setTransactionId(saved.getTransactionId());
        createdEvent.setEventType("TRANSACTION_CREATED");
        createdEvent.setDescription(
                "Simulated transaction created for scenario " + scenario);
        createdEvent.setTimestamp(LocalDateTime.now());
        eventRepository.save(createdEvent);


        TransactionEvent failedEvent = new TransactionEvent();
        failedEvent.setTransactionId(saved.getTransactionId());
        failedEvent.setEventType("TRANSACTION_FAILED");
        failedEvent.setDescription(
                "Simulated failure: " + scenario);
        failedEvent.setTimestamp(LocalDateTime.now());
        eventRepository.save(failedEvent);


        return saved;
    }


    public Transaction getTransaction(String transactionId) {
        return transactionRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));
    }


    public List<TransactionEvent> getTransactionHistory(
            String transactionId) {


        return eventRepository
                .findByTransactionId(transactionId);
    }
}