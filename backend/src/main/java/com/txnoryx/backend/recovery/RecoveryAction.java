package com.txnoryx.backend.recovery;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recovery_actions")
@Getter
@Setter
@NoArgsConstructor
public class RecoveryAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private RecoveryStrategy strategy;

    private String retryStrategy;

    private String status;

    private int attempts;

    private double probability;

    private String message;

    private LocalDateTime createdAt;

    public RecoveryAction(String transactionId,
                          RecoveryStrategy strategy,
                          String status,
                          int attempts,
                          double probability,
                          String message,
                          LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.strategy = strategy;
        this.status = status;
        this.attempts = attempts;
        this.probability = probability;
        this.message = message;
        this.createdAt = createdAt;
    }
}