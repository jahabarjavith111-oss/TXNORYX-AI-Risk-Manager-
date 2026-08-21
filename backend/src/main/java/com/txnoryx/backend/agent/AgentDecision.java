package com.txnoryx.backend.agent;

public class AgentDecision {

    private AgentAction action;

    private String reason;

    private double confidence;

    public AgentDecision() {}

    public AgentDecision(AgentAction action, String reason, double confidence) {
        this.action = action;
        this.reason = reason;
        this.confidence = confidence;
    }

    public AgentAction getAction() {
        return action;
    }

    public void setAction(AgentAction action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}