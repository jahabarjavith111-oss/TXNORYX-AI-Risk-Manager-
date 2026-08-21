package com.txnoryx.backend.risk;

public class RiskResult {

    private int score;

    private RiskLevel level;

    private String reason;

    public RiskResult() {}

    public RiskResult(int score, RiskLevel level, String reason) {
        this.score = score;
        this.level = level;
        this.reason = reason;
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
}