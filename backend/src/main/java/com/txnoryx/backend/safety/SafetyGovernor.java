package com.txnoryx.backend.safety;

import com.txnoryx.backend.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class SafetyGovernor {
    private final SafetyPolicy policy;

    public SafetyGovernor(SafetyPolicy policy) { this.policy = policy; }

    public SafetyDecision evaluate(Transaction tx, int fraudProbability, int riskScore, int confidence, int retries) {
        double amount = tx.getAmount() != null ? tx.getAmount().doubleValue() : 0;
        if (fraudProbability >= policy.getFraudBlock()) return new SafetyDecision(false, "BLOCK", "Fraud " + fraudProbability + "% ≥ " + policy.getFraudBlock() + "% → BLOCK");
        if (retries >= policy.getMaxRetries()) return new SafetyDecision(false, "STOP", "Retries " + retries + " ≥ " + policy.getMaxRetries() + " → STOP");
        if (riskScore >= policy.getRiskReview()) return new SafetyDecision(false, "HUMAN_REVIEW", "Risk " + riskScore + " ≥ " + policy.getRiskReview() + " → HUMAN REVIEW");
        if (amount > policy.getAmountThreshold()) return new SafetyDecision(false, "HUMAN_REVIEW", "Amount ₹" + amount + " > ₹" + policy.getAmountThreshold() + " → HUMAN REVIEW");
        if (confidence < policy.getConfidenceReview()) return new SafetyDecision(false, "HUMAN_REVIEW", "Confidence " + confidence + "% < " + policy.getConfidenceReview() + "% → HUMAN REVIEW");
        return new SafetyDecision(true, "APPROVED", "Safety approved");
    }
}
