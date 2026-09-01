package com.txnoryx.backend.failure;

public class FailureResult {
    private final FailureType type;
    private final double confidence;
    private final String matchedSnippet;
    private final String normalizedReason;
    private final boolean retryable;
    private final String suggestedStrategy;

    public FailureResult(FailureType type, double confidence, String matchedSnippet, String normalizedReason) {
        this.type = type;
        this.confidence = confidence;
        this.matchedSnippet = matchedSnippet;
        this.normalizedReason = normalizedReason;
        this.retryable = type.isRetryable();
        this.suggestedStrategy = type.getDefaultStrategy();
    }

    public FailureType getType() { return type; }
    public double getConfidence() { return confidence; }
    public String getMatchedSnippet() { return matchedSnippet; }
    public String getNormalizedReason() { return normalizedReason; }
    public boolean isRetryable() { return retryable; }
    public String getSuggestedStrategy() { return suggestedStrategy; }
}
