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
        this.status = status;
        this.attempts = attempts;
        this.probability = probability;
        this.message = message;
        this.createdAt = LocalDateTime.now();
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
}