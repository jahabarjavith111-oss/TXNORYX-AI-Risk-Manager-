package com.txnoryx.backend.decision;

public class ExpectedValue {
    private final double expectedRecovery;
    private final double fraudExposure;
    private final double retryCost;
    private final double friction;
    private final double netValue;

    public ExpectedValue(double expectedRecovery, double fraudExposure, double retryCost, double friction) {
        this.expectedRecovery = expectedRecovery;
        this.fraudExposure = fraudExposure;
        this.retryCost = retryCost;
        this.friction = friction;
        this.netValue = expectedRecovery - fraudExposure - retryCost - friction;
    }

    public double getExpectedRecovery() { return expectedRecovery; }
    public double getFraudExposure() { return fraudExposure; }
    public double getRetryCost() { return retryCost; }
    public double getFriction() { return friction; }
    public double getNetValue() { return netValue; }
}
