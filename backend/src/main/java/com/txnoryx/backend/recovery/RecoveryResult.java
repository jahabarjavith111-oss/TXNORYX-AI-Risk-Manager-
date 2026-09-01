package com.txnoryx.backend.recovery;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryResult {

    private String transactionId;

    private RecoveryStrategy strategy;

    private RetryStrategy retryStrategy;

    private String status;

    private int attempts;

    private double probability;

    private String message;

    private LocalDateTime createdAt;

    public RecoveryResult(String transactionId,
                          RecoveryStrategy strategy,
                          String status,
                          int attempts,
                          double probability,
                          String message) {
        this.transactionId = transactionId;
        this.strategy = strategy;
        this.retryStrategy = mapRetry(strategy, probability);
        this.status = status;
        this.attempts = attempts;
        this.probability = probability;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public RecoveryResult(String transactionId, RecoveryStrategy strategy, RetryStrategy retryStrategy, String status, int attempts, double probability, String message) {
        this.transactionId = transactionId;
        this.strategy = strategy;
        this.retryStrategy = retryStrategy;
        this.status = status;
        this.attempts = attempts;
        this.probability = probability;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    private static RetryStrategy mapRetry(RecoveryStrategy s, double p) {
        if (s == RecoveryStrategy.RETRY) return p > 0.85 ? RetryStrategy.RETRY_NOW : RetryStrategy.DELAY_RETRY;
        if (s == RecoveryStrategy.ALTERNATIVE_ROUTE) return RetryStrategy.SWITCH_ROUTE;
        if (s == RecoveryStrategy.VERIFY) return RetryStrategy.REQUEST_AUTHENTICATION;
        if (s == RecoveryStrategy.ESCALATE) return RetryStrategy.HUMAN_REVIEW;
        if (s == RecoveryStrategy.BLOCK) return RetryStrategy.BLOCK;
        return RetryStrategy.HUMAN_REVIEW;
    }

    public RecoveryStrategy getStrategy() {
        return strategy;
    }

    public String getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public double getProbability() {
        return probability;
    }

    public String getMessage() {
        return message;
    }

    public RetryStrategy getRetryStrategy() { return retryStrategy; }
    public void setRetryStrategy(RetryStrategy retryStrategy) { this.retryStrategy = retryStrategy; }
}