package com.txnoryx.backend.decision;

public class DecisionResult {
    private final String recommendation;
    private final ExpectedValue expectedValue;
    private final String reason;

    public DecisionResult(String recommendation, ExpectedValue expectedValue, String reason) {
        this.recommendation = recommendation;
        this.expectedValue = expectedValue;
        this.reason = reason;
    }

    public String getRecommendation() { return recommendation; }
    public ExpectedValue getExpectedValue() { return expectedValue; }
    public String getReason() { return reason; }
}
