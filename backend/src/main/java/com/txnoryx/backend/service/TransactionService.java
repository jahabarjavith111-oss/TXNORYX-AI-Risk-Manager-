package com.txnoryx.backend.service;

import com.txnoryx.backend.dto.CreateTransactionRequest;
import com.txnoryx.backend.dto.SimulateRequest;
import com.txnoryx.backend.failure.FailureAnalyzer;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.model.TransactionEvent;
import com.txnoryx.backend.repository.TransactionEventRepository;
import com.txnoryx.backend.repository.TransactionRepository;

import com.txnoryx.backend.security.PromptSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventRepository eventRepository;
    private final FailureAnalyzer failureAnalyzer;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionEventRepository eventRepository,
                              FailureAnalyzer failureAnalyzer) {
        this.transactionRepository = transactionRepository;
        this.eventRepository = eventRepository;
        this.failureAnalyzer = failureAnalyzer;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {
        if (transactionRepository.findByTransactionIdIgnoreCase(request.getTransactionId().trim()).isPresent()) throw new IllegalArgumentException("Duplicate transactionId: "+request.getTransactionId());
        Transaction transaction = new Transaction();
        transaction.setTransactionId(HtmlUtils.htmlEscape(request.getTransactionId().trim().toUpperCase()));
        transaction.setUserId(request.getUserId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setMerchant(HtmlUtils.htmlEscape(PromptSanitizer.sanitize(request.getMerchant())));
        transaction.setFailureReason(HtmlUtils.htmlEscape(PromptSanitizer.sanitize(request.getFailureReason())));
        transaction.setDeviceId(HtmlUtils.htmlEscape(request.getDeviceId()));
        transaction.setLocation(HtmlUtils.htmlEscape(PromptSanitizer.sanitize(request.getLocation())));
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
        return transactionRepository.findAll().stream().peek(t->{
            if(t.getDeviceId()!=null) t.setDeviceId(mask(t.getDeviceId()));
        }).collect(Collectors.toList());
    }
    private String mask(String s){ if(s==null||s.length()<=4) return "***"; return "***"+s.substring(s.length()-4); }


    public Transaction simulateTransaction(SimulateRequest request) {

        String scenario = request.getScenario();

        FailureResult fr = failureAnalyzer.analyze(scenario, null);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(
                "TXN-SIM-" + UUID.randomUUID().toString()
                        .substring(0, 8).toUpperCase());
        transaction.setUserId(1L);
        transaction.setAmount(new java.math.BigDecimal("1499.00"));
        transaction.setCurrency("INR");
        transaction.setPaymentMethod("UPI");
        transaction.setMerchant("Simulated Merchant");
        transaction.setStatus(fr.getType().getCanonicalStatus());
        transaction.setFailureReason(scenario + " [" + fr.getType().name() + ": " + failureAnalyzer.explain(fr) + "]");
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
        String nid = transactionId != null ? transactionId.trim() : "";
        return transactionRepository.findByTransactionIdIgnoreCase(nid)
                .or(() -> transactionRepository.findByTransactionId(nid))
                .or(() -> transactionRepository.findByTransactionIdIgnoreCase(transactionId))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + nid));
    }

    public List<TransactionEvent> getTransactionHistory(String transactionId) {
        String nid = transactionId != null ? transactionId.trim() : "";
        List<TransactionEvent> ev = eventRepository.findByTransactionId(nid);
        if (!ev.isEmpty()) return ev;
        return eventRepository.findByTransactionId(nid.toUpperCase());
    }
}