package com.txnoryx.backend.failure;

public enum FailureType {
    GATEWAY_TIMEOUT("TIMEOUT", true, "RETRY"),
    BANK_DECLINE("DECLINED", false, "ALTERNATIVE_ROUTE"),
    INSUFFICIENT_FUNDS("FAILED", false, "VERIFY"),
    NETWORK_FAILURE("TIMEOUT", true, "RETRY"),
    SUSPICIOUS_ACTIVITY("SUSPICIOUS", false, "ESCALATE"),
    LIMIT_EXCEEDED("FAILED", false, "VERIFY"),
    UNKNOWN("FAILED", false, "NO_ACTION");

    private final String canonicalStatus;
    private final boolean retryable;
    private final String defaultStrategy;

    FailureType(String canonicalStatus, boolean retryable, String defaultStrategy) {
        this.canonicalStatus = canonicalStatus;
        this.retryable = retryable;
        this.defaultStrategy = defaultStrategy;
    }

    public String getCanonicalStatus() { return canonicalStatus; }
    public boolean isRetryable() { return retryable; }
    public String getDefaultStrategy() { return defaultStrategy; }
}
