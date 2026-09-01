package com.txnoryx.backend.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String transactionId;
    private int riskScore;
    private String riskLevel;
    private int fraudProbability;
    private int recoveryProbability;
    private String aiDecision;
    private String safetyDecision;
    private String reason;
    private int confidence;
    private double expectedValue;
    private String routeCode;
    private LocalDateTime timestamp;

    public AuditLog() {}
    public AuditLog(String transactionId, int riskScore, String riskLevel, int fraudProbability, int recoveryProbability, String aiDecision, String safetyDecision, String reason, int confidence, double expectedValue, String routeCode) {
        this.transactionId = transactionId; this.riskScore = riskScore; this.riskLevel = riskLevel; this.fraudProbability = fraudProbability; this.recoveryProbability = recoveryProbability; this.aiDecision = aiDecision; this.safetyDecision = safetyDecision; this.reason = reason; this.confidence = confidence; this.expectedValue = expectedValue; this.routeCode = routeCode; this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; } public void setTransactionId(String v) { this.transactionId = v; }
    public int getRiskScore() { return riskScore; } public void setRiskScore(int v) { this.riskScore = v; }
    public String getRiskLevel() { return riskLevel; } public void setRiskLevel(String v) { this.riskLevel = v; }
    public int getFraudProbability() { return fraudProbability; } public void setFraudProbability(int v) { this.fraudProbability = v; }
    public int getRecoveryProbability() { return recoveryProbability; } public void setRecoveryProbability(int v) { this.recoveryProbability = v; }
    public String getAiDecision() { return aiDecision; } public void setAiDecision(String v) { this.aiDecision = v; }
    public String getSafetyDecision() { return safetyDecision; } public void setSafetyDecision(String v) { this.safetyDecision = v; }
    public String getReason() { return reason; } public void setReason(String v) { this.reason = v; }
    public int getConfidence() { return confidence; } public void setConfidence(int v) { this.confidence = v; }
    public double getExpectedValue() { return expectedValue; } public void setExpectedValue(double v) { this.expectedValue = v; }
    public String getRouteCode() { return routeCode; } public void setRouteCode(String v) { this.routeCode = v; }
    public LocalDateTime getTimestamp() { return timestamp; } public void setTimestamp(LocalDateTime v) { this.timestamp = v; }
}
