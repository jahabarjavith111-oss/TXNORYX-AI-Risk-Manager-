package com.txnoryx.backend.fraud;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FraudAnalysis {

    private String transactionId;

    private int riskScore;

    private FraudRisk riskLevel;

    private boolean suspicious;

    private List<RiskFactor> factors;

    private String recommendation;

    public FraudAnalysis(String transactionId,
                         int riskScore,
                         FraudRisk riskLevel,
                         boolean suspicious,
                         List<RiskFactor> factors,
                         String recommendation) {
        this.transactionId = transactionId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.suspicious = suspicious;
        this.factors = factors;
        this.recommendation = recommendation;
    }

    public String getTransactionId() { return transactionId; }
    public int getRiskScore() { return riskScore; }
    public FraudRisk getRiskLevel() { return riskLevel; }
    public boolean isSuspicious() { return suspicious; }
    public List<RiskFactor> getFactors() { return factors; }
    public String getRecommendation() { return recommendation; }
}