package com.txnoryx.backend.safety;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "txnoryx.safety")
public class SafetyPolicy {
    private int fraudBlock = 80;
    private int riskReview = 70;
    private int maxRetries = 3;
    private int confidenceReview = 60;
    private double amountThreshold = 100000;

    public int getFraudBlock() { return fraudBlock; } public void setFraudBlock(int v) { this.fraudBlock = v; }
    public int getRiskReview() { return riskReview; } public void setRiskReview(int v) { this.riskReview = v; }
    public int getMaxRetries() { return maxRetries; } public void setMaxRetries(int v) { this.maxRetries = v; }
    public int getConfidenceReview() { return confidenceReview; } public void setConfidenceReview(int v) { this.confidenceReview = v; }
    public double getAmountThreshold() { return amountThreshold; } public void setAmountThreshold(double v) { this.amountThreshold = v; }
}
