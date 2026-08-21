package com.txnoryx.backend.recovery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryDecision {

    private RecoveryStrategy strategy;

    private String reason;

    private double probability;

    private int maxAttempts;

    public RecoveryDecision() {
    }

    public RecoveryDecision(RecoveryStrategy strategy, String reason, double probability, int maxAttempts) {
        this.strategy = strategy;
        this.reason = reason;
        this.probability = probability;
        this.maxAttempts = maxAttempts;
    }

    public double getProbability() {
        return probability;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public RecoveryStrategy getStrategy() {
        return strategy;
    }
}