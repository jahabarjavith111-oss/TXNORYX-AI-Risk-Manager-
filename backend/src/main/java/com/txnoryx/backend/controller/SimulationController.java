package com.txnoryx.backend.controller;

import com.txnoryx.backend.audit.AuditLog;
import com.txnoryx.backend.audit.AuditService;
import com.txnoryx.backend.dto.SimulateRequest;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.service.TransactionService;
import com.txnoryx.backend.agent.AutonomousPaymentAgent;
import com.txnoryx.backend.recovery.RecoveryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    private final TransactionService transactionService;
    private final AutonomousPaymentAgent agent;
    private final RecoveryService recoveryService;
    private final AuditService auditService;

    public SimulationController(TransactionService transactionService, AutonomousPaymentAgent agent, RecoveryService recoveryService, AuditService auditService) {
        this.transactionService = transactionService; this.agent = agent; this.recoveryService = recoveryService; this.auditService = auditService;
    }

    @PostMapping("/generate")
    public Map<String, Object> generate(@RequestBody Map<String, String> body) {
        String scenario = body.getOrDefault("scenario", "GATEWAY_TIMEOUT");
        if (scenario.equals("Normal")) scenario = "SUCCESS";
        Transaction tx = transactionService.simulateTransaction(new SimulateRequest(scenario));
        var decision = agent.decide(tx, 0);
        var recovery = recoveryService.recover(tx.getTransactionId());
        List<AuditLog> audit = auditService.getByTransaction(tx.getTransactionId());
        return Map.of("transaction", tx, "agentDecision", decision.getAction().name(), "recovery", recovery, "audit", audit);
    }

    @PostMapping("/bulk")
    public List<Transaction> bulk(@RequestParam(defaultValue = "20") int count) {
        String[] scenarios = {"GATEWAY_TIMEOUT","BANK_DECLINE","INSUFFICIENT_FUNDS","NETWORK_FAILURE","SUSPICIOUS_ACTIVITY","LIMIT_EXCEEDED","SUCCESS"};
        java.util.ArrayList<Transaction> out = new java.util.ArrayList<>();
        for (int i=0;i<count;i++) out.add(transactionService.simulateTransaction(new SimulateRequest(scenarios[i % scenarios.length])));
        return out;
    }
}
