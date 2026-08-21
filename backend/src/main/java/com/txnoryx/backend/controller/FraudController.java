package com.txnoryx.backend.controller;

import com.txnoryx.backend.fraud.FraudAnalysis;
import com.txnoryx.backend.fraud.FraudRisk;
import com.txnoryx.backend.service.FraudService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudService fraudService;

    public FraudController(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    @PostMapping(value = "/analyze/{transactionId}", produces = "application/json")
    public FraudAnalysis analyzeTransaction(@PathVariable String transactionId) {
        return fraudService.analyzeTransaction(transactionId);
    }

    @GetMapping(value = "/analysis/{transactionId}", produces = "application/json")
    public FraudAnalysis getAnalysis(@PathVariable String transactionId) {
        return fraudService.analyzeTransaction(transactionId);
    }

    @GetMapping(value = "/analyze/{transactionId}", produces = "application/json")
    public FraudAnalysis getFraudAnalyze(@PathVariable String transactionId) {
        return fraudService.analyzeTransaction(transactionId);
    }

    @PostMapping(value = "/risk-level/{score}", produces = "application/json")
    public FraudRisk getRiskLevel(@PathVariable int score) {
        return fraudService.getRiskLevel(score);
    }
}