package com.txnoryx.backend.controller;

import com.txnoryx.backend.model.AIAnalysis;
import com.txnoryx.backend.service.TransactionService;
import com.txnoryx.backend.ai.AIAnalysisService;
import com.txnoryx.backend.risk.RiskEngine;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final TransactionService transactionService;
    private final AIAnalysisService aiAnalysisService;
    private final RiskEngine riskEngine;

    public RiskController(TransactionService transactionService,
                          AIAnalysisService aiAnalysisService,
                          RiskEngine riskEngine) {
        this.transactionService = transactionService;
        this.aiAnalysisService = aiAnalysisService;
        this.riskEngine = riskEngine;
    }

    @PostMapping("/analyze/{transactionId}")
    public AIAnalysis analyzeTransaction(@PathVariable String transactionId) {
        return aiAnalysisService.analyzeTransaction(transactionId);
    }

    @GetMapping("/analysis/{transactionId}")
    public AIAnalysis getAnalysis(@PathVariable String transactionId) {
        return aiAnalysisService.analyzeTransaction(transactionId);
    }

    @GetMapping("/{transactionId}")
    public AIAnalysis getRisk(@PathVariable String transactionId) {
        return aiAnalysisService.analyzeTransaction(transactionId);
    }

    @PostMapping("/{transactionId}")
    public AIAnalysis postRisk(@PathVariable String transactionId) {
        return aiAnalysisService.analyzeTransaction(transactionId);
    }
}