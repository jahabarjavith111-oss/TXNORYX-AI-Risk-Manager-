package com.txnoryx.backend.controller;

import com.txnoryx.backend.failure.FailureAnalyzer;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/failure")
public class FailureController {

    private final FailureAnalyzer failureAnalyzer;
    private final TransactionRepository transactionRepository;

    public FailureController(FailureAnalyzer failureAnalyzer, TransactionRepository transactionRepository) {
        this.failureAnalyzer = failureAnalyzer;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{transactionId}")
    public Map<String, Object> classify(@PathVariable String transactionId) {
        Transaction tx = transactionRepository.findByTransactionId(transactionId).orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
        FailureResult fr = failureAnalyzer.analyze(tx);
        return Map.of("transactionId", transactionId, "failureType", fr.getType().name(), "confidence", fr.getConfidence(), "explanation", failureAnalyzer.explain(fr), "retryable", fr.isRetryable(), "suggestedStrategy", fr.getSuggestedStrategy(), "matchedSnippet", fr.getMatchedSnippet());
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyzeReason(@RequestBody Map<String, String> body) {
        String reason = body.getOrDefault("reason", "");
        String status = body.getOrDefault("status", "");
        FailureResult fr = failureAnalyzer.analyze(reason, status);
        return Map.of("failureType", fr.getType().name(), "confidence", fr.getConfidence(), "explanation", failureAnalyzer.explain(fr), "retryable", fr.isRetryable(), "suggestedStrategy", fr.getSuggestedStrategy());
    }
}
