package com.txnoryx.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_analysis")
public class AIAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    private int riskScore;

    private String riskLevel;

    private double confidence;

    private int fraudProbability;

    private int recoveryProbability;

    private int decisionConfidence;

    private String failureType;

    private String failureExplanation;

    private String rootCause;

    private String recommendation;

    private String explanation;

    private java.time.LocalDateTime createdAt;

    public AIAnalysis() {}

    public AIAnalysis(String transactionId, int riskScore, String riskLevel,
                      double confidence, String rootCause, String recommendation,
                      String explanation, java.time.LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.confidence = confidence;
        this.rootCause = rootCause;
        this.recommendation = recommendation;
        this.explanation = explanation;
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

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public int getFraudProbability() {
        return fraudProbability;
    }

    public void setFraudProbability(int fraudProbability) {
        this.fraudProbability = fraudProbability;
    }

    public int getRecoveryProbability() {
        return recoveryProbability;
    }

    public void setRecoveryProbability(int recoveryProbability) {
        this.recoveryProbability = recoveryProbability;
    }

    public int getDecisionConfidence() {
        return decisionConfidence;
    }

    public void setDecisionConfidence(int decisionConfidence) {
        this.decisionConfidence = decisionConfidence;
    }

    public String getFailureType() {
        return failureType;
    }

    public void setFailureType(String failureType) {
        this.failureType = failureType;
    }

    public String getFailureExplanation() {
        return failureExplanation;
    }

    public void setFailureExplanation(String failureExplanation) {
        this.failureExplanation = failureExplanation;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}