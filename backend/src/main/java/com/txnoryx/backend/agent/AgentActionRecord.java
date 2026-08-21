package com.txnoryx.backend.agent;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_actions")
public class AgentActionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private AgentAction action;

    private String status;

    private String message;

    private double confidence;

    private LocalDateTime createdAt;

    public AgentActionRecord() {
    }

    public AgentActionRecord(String transactionId,
                             AgentAction action,
                             String status,
                             String message,
                             double confidence,
                             LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.action = action;
        this.status = status;
        this.message = message;
        this.confidence = confidence;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public AgentAction getAction() {
        return action;
    }

    public void setAction(AgentAction action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}