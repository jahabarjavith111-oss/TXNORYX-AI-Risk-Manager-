package com.txnoryx.backend.recovery;

import com.txnoryx.backend.failure.FailureAnalyzer;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.failure.FailureType;
import com.txnoryx.backend.model.Transaction;

import org.springframework.stereotype.Component;

@Component
public class RecoveryEngine {

    private final FailureAnalyzer failureAnalyzer;

    public RecoveryEngine(FailureAnalyzer failureAnalyzer) {
        this.failureAnalyzer = failureAnalyzer;
    }

    public RecoveryEngine() {
        this.failureAnalyzer = new FailureAnalyzer();
    }

    public RecoveryDecision decide(Transaction transaction) {
        FailureResult fr = failureAnalyzer.analyze(transaction);
        return decideByFailure(fr);
    }

    public RecoveryDecision decideByFailure(FailureResult fr) {
        FailureType t = fr.getType();
        if (t == FailureType.GATEWAY_TIMEOUT) return new RecoveryDecision(RecoveryStrategy.RETRY, "Temporary Gateway Failure", 0.87, 2);
        if (t == FailureType.NETWORK_FAILURE) return new RecoveryDecision(RecoveryStrategy.RETRY, "Network Failure — retry", 0.75, 1);
        if (t == FailureType.BANK_DECLINE) return new RecoveryDecision(RecoveryStrategy.ALTERNATIVE_ROUTE, "Bank decline — alternate route", 0.68, 1);
        if (t == FailureType.INSUFFICIENT_FUNDS) return new RecoveryDecision(RecoveryStrategy.VERIFY, "Insufficient funds — verify", 0.30, 0);
        if (t == FailureType.LIMIT_EXCEEDED) return new RecoveryDecision(RecoveryStrategy.VERIFY, "Limit exceeded — verify", 0.50, 0);
        if (t == FailureType.SUSPICIOUS_ACTIVITY) return new RecoveryDecision(RecoveryStrategy.ESCALATE, "Suspicious activity — review", 0.95, 0);
        return new RecoveryDecision(RecoveryStrategy.NO_ACTION, "No recovery required", 0.99, 0);
    }

    public RecoveryDecision decideIntelligent(Transaction transaction, int fraudProbability, int recoveryProbability) {
        FailureResult fr = failureAnalyzer.analyze(transaction);
        FailureType t = fr.getType();
        if (t == FailureType.GATEWAY_TIMEOUT && fraudProbability < 70 && recoveryProbability > 80) return new RecoveryDecision(RecoveryStrategy.RETRY, "RETRY_NOW — Gateway timeout low fraud high recovery", 0.92, 2);
        if (t == FailureType.GATEWAY_TIMEOUT && fraudProbability < 90 && recoveryProbability > 50) return new RecoveryDecision(RecoveryStrategy.RETRY, "DELAY_RETRY — transient gateway", 0.78, 2);
        if (t == FailureType.NETWORK_FAILURE) return new RecoveryDecision(RecoveryStrategy.RETRY, "DELAY_RETRY — network", 0.75, 1);
        if (t == FailureType.BANK_DECLINE) return new RecoveryDecision(RecoveryStrategy.ALTERNATIVE_ROUTE, "SWITCH_ROUTE — bank decline", 0.68, 1);
        if (t == FailureType.INSUFFICIENT_FUNDS || t == FailureType.LIMIT_EXCEEDED) return new RecoveryDecision(RecoveryStrategy.VERIFY, "REQUEST_AUTHENTICATION — funds/limit", 0.40, 0);
        if (t == FailureType.SUSPICIOUS_ACTIVITY && fraudProbability > 70) return new RecoveryDecision(RecoveryStrategy.BLOCK, "BLOCK — high fraud suspicious", 0.96, 0);
        if (t == FailureType.SUSPICIOUS_ACTIVITY) return new RecoveryDecision(RecoveryStrategy.ESCALATE, "HUMAN_REVIEW — suspicious", 0.95, 0);
        if (fraudProbability > 85) return new RecoveryDecision(RecoveryStrategy.BLOCK, "BLOCK — fraud", 0.96, 0);
        if (fraudProbability > 60) return new RecoveryDecision(RecoveryStrategy.ESCALATE, "HUMAN_REVIEW — elevated fraud", 0.90, 0);
        return decideByFailure(fr);
    }

    public RetryStrategy toRetryStrategy(RecoveryDecision decision) {
        switch (decision.getStrategy()) {
            case RETRY: return decision.getProbability() > 0.85 ? RetryStrategy.RETRY_NOW : RetryStrategy.DELAY_RETRY;
            case ALTERNATIVE_ROUTE: return RetryStrategy.SWITCH_ROUTE;
            case VERIFY: return RetryStrategy.REQUEST_AUTHENTICATION;
            case ESCALATE: return RetryStrategy.HUMAN_REVIEW;
            case BLOCK: return RetryStrategy.BLOCK;
            default: return RetryStrategy.HUMAN_REVIEW;
        }
    }
}