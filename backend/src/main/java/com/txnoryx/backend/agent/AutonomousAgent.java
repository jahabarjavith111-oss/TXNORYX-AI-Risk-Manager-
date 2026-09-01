package com.txnoryx.backend.agent;

import com.txnoryx.backend.failure.FailureAnalyzer;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.failure.FailureType;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.risk.RiskResult;

import org.springframework.stereotype.Service;

@Service
public class AutonomousAgent {

    private final FailureAnalyzer failureAnalyzer;

    public AutonomousAgent(FailureAnalyzer failureAnalyzer) {
        this.failureAnalyzer = failureAnalyzer;
    }

    public AutonomousAgent() {
        this.failureAnalyzer = new FailureAnalyzer();
    }

    public AgentDecision decide(Transaction transaction, RiskResult risk) {
        FailureResult fr = failureAnalyzer.analyze(transaction);
        return decideWithFailure(transaction, risk, fr);
    }

    public AgentDecision decideWithFailure(Transaction transaction, RiskResult risk, FailureResult fr) {
        if (risk.getLevel().name().equals("CRITICAL")) return new AgentDecision(AgentAction.BLOCK, "Critical risk detected", 0.95);
        if (fr.getType() == FailureType.GATEWAY_TIMEOUT || fr.getType() == FailureType.NETWORK_FAILURE) return new AgentDecision(AgentAction.RETRY_PAYMENT, failureAnalyzer.explain(fr), 0.92);
        if (fr.getType() == FailureType.BANK_DECLINE) return new AgentDecision(AgentAction.VERIFY_PAYMENT, "Bank decline — verify", 0.89);
        if (fr.getType() == FailureType.INSUFFICIENT_FUNDS || fr.getType() == FailureType.LIMIT_EXCEEDED) return new AgentDecision(AgentAction.ESCALATE, failureAnalyzer.explain(fr), 0.88);
        if (fr.getType() == FailureType.SUSPICIOUS_ACTIVITY) return new AgentDecision(AgentAction.ESCALATE, "Suspicious activity — review", 0.93);
        if (risk.getLevel().name().equals("HIGH")) return new AgentDecision(AgentAction.ESCALATE, "High risk requires manual review", 0.93);
        return new AgentDecision(AgentAction.APPROVE, "Transaction appears normal", 0.97);
    }
}