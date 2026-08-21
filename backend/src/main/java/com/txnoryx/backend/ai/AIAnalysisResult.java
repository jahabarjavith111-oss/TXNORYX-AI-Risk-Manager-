package com.txnoryx.backend.ai;

public class AIAnalysisResult {

    private int riskScore;

    private String riskLevel;

    private double confidence;

    private String rootCause;

    private String recommendation;

    private String explanation;

    public AIAnalysisResult() {}

    public AIAnalysisResult(int riskScore, String riskLevel, double confidence,
                            String rootCause, String recommendation, String explanation) {
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.confidence = confidence;
        this.rootCause = rootCause;
        this.recommendation = recommendation;
        this.explanation = explanation;
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
}