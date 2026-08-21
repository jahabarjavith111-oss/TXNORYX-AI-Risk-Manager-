package com.txnoryx.backend.fraud;

import jakarta.persistence.*;

@Entity
@Table(name = "risk_factors")
public class RiskFactorRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String factor;

    private int score;

    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_analysis_id")
    private FraudAnalysisRecord fraudAnalysis;

    public RiskFactorRecord() {
    }

    public RiskFactorRecord(String factor, int score, String explanation) {
        this.factor = factor;
        this.score = score;
        this.explanation = explanation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFactor() { return factor; }
    public void setFactor(String factor) { this.factor = factor; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public FraudAnalysisRecord getFraudAnalysis() { return fraudAnalysis; }
    public void setFraudAnalysis(FraudAnalysisRecord fraudAnalysis) { this.fraudAnalysis = fraudAnalysis; }
}
