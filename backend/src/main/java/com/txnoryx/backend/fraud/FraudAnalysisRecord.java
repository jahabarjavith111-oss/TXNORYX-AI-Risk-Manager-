package com.txnoryx.backend.fraud;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fraud_analysis")
public class FraudAnalysisRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    private int riskScore;

    @Enumerated(EnumType.STRING)
    private FraudRisk riskLevel;

    private boolean suspicious;

    private String recommendation;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "fraudAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RiskFactorRecord> factors = new ArrayList<>();

    public FraudAnalysisRecord() {
    }

    public FraudAnalysisRecord(String transactionId, int riskScore, FraudRisk riskLevel, boolean suspicious, String recommendation, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.suspicious = suspicious;
        this.recommendation = recommendation;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
    public FraudRisk getRiskLevel() { return riskLevel; }
    public void setRiskLevel(FraudRisk riskLevel) { this.riskLevel = riskLevel; }
    public boolean isSuspicious() { return suspicious; }
    public void setSuspicious(boolean suspicious) { this.suspicious = suspicious; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<RiskFactorRecord> getFactors() { return factors; }
    public void setFactors(List<RiskFactorRecord> factors) { this.factors = factors; }

    public void addFactor(RiskFactorRecord factor) {
        factor.setFraudAnalysis(this);
        this.factors.add(factor);
    }
}
