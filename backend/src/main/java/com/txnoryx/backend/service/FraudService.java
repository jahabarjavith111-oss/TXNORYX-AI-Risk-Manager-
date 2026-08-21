package com.txnoryx.backend.service;

import com.txnoryx.backend.fraud.FraudAnalysis;
import com.txnoryx.backend.fraud.FraudAnalysisRecord;
import com.txnoryx.backend.fraud.FraudAnalysisRecordRepository;
import com.txnoryx.backend.fraud.FraudDetectionEngine;
import com.txnoryx.backend.fraud.FraudRisk;
import com.txnoryx.backend.fraud.RiskFactorRecord;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudService {

    private final FraudDetectionEngine fraudDetectionEngine;
    private final TransactionRepository transactionRepository;
    private final FraudAnalysisRecordRepository fraudAnalysisRecordRepository;

    @Autowired
    public FraudService(FraudDetectionEngine fraudDetectionEngine,
                        TransactionRepository transactionRepository,
                        FraudAnalysisRecordRepository fraudAnalysisRecordRepository) {
        this.fraudDetectionEngine = fraudDetectionEngine;
        this.transactionRepository = transactionRepository;
        this.fraudAnalysisRecordRepository = fraudAnalysisRecordRepository;
    }

    public FraudAnalysis analyzeTransaction(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .map(tx -> {
                    FraudAnalysis analysis = fraudDetectionEngine.analyze(tx);
                    persistAnalysis(analysis);
                    return analysis;
                })
                .orElse(null);
    }

    private void persistAnalysis(FraudAnalysis analysis) {
        FraudAnalysisRecord record = new FraudAnalysisRecord(
                analysis.getTransactionId(),
                analysis.getRiskScore(),
                analysis.getRiskLevel(),
                analysis.isSuspicious(),
                analysis.getRecommendation(),
                LocalDateTime.now()
        );
        if (analysis.getFactors() != null) {
            for (var f : analysis.getFactors()) {
                record.addFactor(new RiskFactorRecord(f.getFactor(), f.getScore(), f.getExplanation()));
            }
        }
        fraudAnalysisRecordRepository.save(record);
    }

    public List<FraudAnalysis> analyzeRecentTransactions(int limit) {
        List<Transaction> transactions = transactionRepository.findTop5ByOrderByCreatedAtDesc();
        return transactions.stream()
                .limit(limit)
                .map(transaction -> {
                    FraudAnalysis analysis = fraudDetectionEngine.analyze(transaction);
                    return analysis;
                })
                .filter(f -> f != null)
                .toList();
    }

    public FraudRisk getRiskLevel(int score) {
        if (score <= 30) {
            return FraudRisk.LOW;
        } else if (score <= 60) {
            return FraudRisk.MEDIUM;
        } else if (score <= 80) {
            return FraudRisk.HIGH;
        } else {
            return FraudRisk.CRITICAL;
        }
    }
}