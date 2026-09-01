package com.txnoryx.backend.risk;

public class RiskResult {

    private int score;

    private RiskLevel level;

    private String reason;

    private int fraudProbability;

    private int recoveryProbability;

    private int confidence;

    public RiskResult() {}

    public RiskResult(int score, RiskLevel level, String reason) {
        this.score = score;
        this.level = level;
        this.reason = reason;
    }

    public RiskResult(int score, RiskLevel level, String reason, int fraudProbability, int recoveryProbability, int confidence) {
        this.score = score;
        this.level = level;
        this.reason = reason;
        this.fraudProbability = fraudProbability;
        this.recoveryProbability = recoveryProbability;
        this.confidence = confidence;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public RiskLevel getLevel() {
        return level;
    }

    public void setLevel(RiskLevel level) {
        this.level = level;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
}