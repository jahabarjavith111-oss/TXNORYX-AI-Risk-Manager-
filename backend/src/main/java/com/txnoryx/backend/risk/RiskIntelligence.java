package com.txnoryx.backend.risk;

import com.txnoryx.backend.failure.FailureAnalyzer;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.fraud.FraudDetectionEngine;
import com.txnoryx.backend.fraud.FraudAnalysis;
import com.txnoryx.backend.recovery.RecoveryDecision;
import org.springframework.stereotype.Component;

@Component
public class RiskIntelligence {

    private final RiskEngine riskEngine;
    private final FraudDetectionEngine fraudDetectionEngine;
    private final com.txnoryx.backend.recovery.RecoveryEngine recoveryEngine;
    private final FailureAnalyzer failureAnalyzer;

    public RiskIntelligence(RiskEngine riskEngine, FraudDetectionEngine fraudDetectionEngine, com.txnoryx.backend.recovery.RecoveryEngine recoveryEngine, FailureAnalyzer failureAnalyzer) {
        this.riskEngine = riskEngine;
        this.fraudDetectionEngine = fraudDetectionEngine;
        this.recoveryEngine = recoveryEngine;
        this.failureAnalyzer = failureAnalyzer;
    }

    public RiskResult analyze(Transaction transaction, Double ollamaConfidence) {
        FailureResult failure = failureAnalyzer.analyze(transaction);
        RiskResult base = riskEngine.calculate(transaction);
        FraudAnalysis fraud = fraudDetectionEngine.analyze(transaction);
        RecoveryDecision recovery = recoveryEngine.decide(transaction);
        int fraudProbability = Math.min(100, fraud.getRiskScore());
        int recoveryProbability = (int) Math.round(recovery.getProbability() * 100);
        if (recovery.getStrategy().name().equals("ESCALATE") || recovery.getStrategy().name().equals("NO_ACTION") && "SUSPICIOUS".equals(transaction.getStatus())) {
            recoveryProbability = Math.min(recoveryProbability, 15);
        }
        if ("SUSPICIOUS".equals(transaction.getStatus())) {
            recoveryProbability = Math.min(recoveryProbability, 12);
        }
        int confidence = computeConfidence(base.getScore(), ollamaConfidence);
        return new RiskResult(base.getScore(), base.getLevel(), base.getReason(), fraudProbability, recoveryProbability, confidence);
    }

    public RiskResult analyze(Transaction transaction) {
        return analyze(transaction, null);
    }

    private int computeConfidence(int score, Double ollamaConf) {
        int dist = Math.min(Math.abs(score - 30), Math.min(Math.abs(score - 60), Math.abs(score - 80)));
        int distanceBoost = Math.min(30, dist);
        int base = 55;
        int ollamaBoost = 0;
        if (ollamaConf != null) {
            ollamaBoost = (int) Math.round(Math.min(1.0, Math.max(0.0, ollamaConf)) * 20);
        } else {
            ollamaBoost = 16;
        }
        int raw = base + distanceBoost + ollamaBoost;
        if (score <= 5 || score >= 95) raw += 5;
        return Math.min(100, Math.max(0, raw));
    }
}
