package com.txnoryx.backend.safety;

public class SafetyDecision {
    private final boolean allowed;
    private final String action;
    private final String reason;

    public SafetyDecision(boolean allowed, String action, String reason) {
        this.allowed = allowed; this.action = action; this.reason = reason;
    }

    public boolean isAllowed() { return allowed; }
    public String getAction() { return action; }
    public String getReason() { return reason; }
}
