package com.txnoryx.backend.fraud;

import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.model.User;
import com.txnoryx.backend.repository.TransactionRepository;
import com.txnoryx.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FraudDetectionEngine {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public FraudDetectionEngine(TransactionRepository transactionRepository,
                                UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public FraudAnalysis analyze(Transaction transaction) {

        int score = 0;
        List<RiskFactor> factors = new ArrayList<>();

        if (transaction.getAmount() != null
                && transaction.getAmount().doubleValue() > 50000) {
            score += 25;
            factors.add(new RiskFactor("HIGH_AMOUNT", 25, "Transaction amount is unusually high"));
        }

        if ("SUSPICIOUS".equals(transaction.getStatus())) {
            score += 35;
            factors.add(new RiskFactor("SUSPICIOUS_STATUS", 35, "Transaction has been flagged as suspicious"));
        }

        if ("DECLINED".equals(transaction.getStatus())) {
            score += 15;
            factors.add(new RiskFactor("PAYMENT_DECLINED", 15, "Payment was declined"));
        }

        if ("TIMEOUT".equals(transaction.getStatus())) {
            score += 10;
            factors.add(new RiskFactor("TIMEOUT", 10, "Payment gateway timeout detected"));
        }

        if (transaction.getFailureReason() != null
                && transaction.getFailureReason().toLowerCase().contains("gateway")) {
            score += 15;
            factors.add(new RiskFactor("GATEWAY_FAILURE", 15, "Gateway failure detected"));
        }

        int velocityScore = calculateVelocityRisk(transaction);
        if (velocityScore > 0) {
            score += velocityScore;
            factors.add(new RiskFactor("HIGH_VELOCITY", velocityScore, "Unusually high transaction frequency detected"));
        }

        int newUserScore = calculateNewUserRisk(transaction);
        if (newUserScore > 0) {
            score += newUserScore;
            factors.add(new RiskFactor("NEW_USER", newUserScore, "Transaction from newly registered user (within 7 days)"));
        }

        int newDeviceScore = calculateNewDeviceRisk(transaction);
        if (newDeviceScore > 0) {
            score += newDeviceScore;
            factors.add(new RiskFactor("NEW_DEVICE", newDeviceScore, "Transaction from unrecognized device for this user"));
        }

        int geoScore = calculateGeoAnomaly(transaction);
        if (geoScore > 0) {
            score += geoScore;
            factors.add(new RiskFactor("GEO_ANOMALY", geoScore, "Geographic anomaly: location differs from user's usual location"));
        }

        int failureScore = calculateMultipleFailureRisk(transaction);
        if (failureScore > 0) {
            score += failureScore;
            factors.add(new RiskFactor("MULTIPLE_FAILURES", failureScore, "Multiple recent failures detected for this user"));
        }

        if (score > 100) {
            score = 100;
        }

        FraudRisk risk;
        if (score <= 30) {
            risk = FraudRisk.LOW;
        } else if (score <= 60) {
            risk = FraudRisk.MEDIUM;
        } else if (score <= 80) {
            risk = FraudRisk.HIGH;
        } else {
            risk = FraudRisk.CRITICAL;
        }

        boolean suspicious = risk == FraudRisk.HIGH || risk == FraudRisk.CRITICAL;

        String recommendation;
        if (risk == FraudRisk.CRITICAL) {
            recommendation = "BLOCK_TRANSACTION";
        } else if (risk == FraudRisk.HIGH) {
            recommendation = "ESCALATE_FOR_REVIEW";
        } else if (risk == FraudRisk.MEDIUM) {
            recommendation = "MONITOR_TRANSACTION";
        } else {
            recommendation = "ALLOW_TRANSACTION";
        }

        return new FraudAnalysis(
                transaction.getTransactionId(),
                score,
                risk,
                suspicious,
                factors,
                recommendation);
    }

    private int calculateVelocityRisk(Transaction transaction) {
        if (transaction.getUserId() == null) {
            return 0;
        }
        long count = transactionRepository.countByUserId(transaction.getUserId());
        if (count >= 10) {
            return 30;
        }
        if (count >= 5) {
            return 15;
        }
        return 0;
    }

    private int calculateNewUserRisk(Transaction transaction) {
        if (transaction.getUserId() == null) {
            return 0;
        }
        return userRepository.findById(transaction.getUserId())
                .map(user -> {
                    if (user.getCreatedAt() == null) {
                        return 0;
                    }
                    return user.getCreatedAt().isAfter(LocalDateTime.now().minusDays(7)) ? 20 : 0;
                })
                .orElse(0);
    }

    private int calculateNewDeviceRisk(Transaction transaction) {
        if (transaction.getUserId() == null || transaction.getDeviceId() == null) {
            return 0;
        }
        long count = transactionRepository.countByUserIdAndDeviceId(
                transaction.getUserId(), transaction.getDeviceId());
        return count <= 1 ? 20 : 0;
    }

    private int calculateGeoAnomaly(Transaction transaction) {
        if (transaction.getUserId() == null || transaction.getLocation() == null) {
            return 0;
        }
        List<Transaction> history = transactionRepository.findByUserId(transaction.getUserId());
        if (history.size() < 3) {
            return 0;
        }
        Map<String, Long> freq = history.stream()
                .filter(t -> t.getLocation() != null)
                .collect(Collectors.groupingBy(Transaction::getLocation, Collectors.counting()));
        if (freq.isEmpty()) {
            return 0;
        }
        String dominant = freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        if (dominant != null && !dominant.equals(transaction.getLocation())) {
            return 25;
        }
        return 0;
    }

    private int calculateMultipleFailureRisk(Transaction transaction) {
        if (transaction.getUserId() == null) {
            return 0;
        }
        long failures = transactionRepository.countFailuresByUserId(transaction.getUserId());
        return failures >= 3 ? 20 : 0;
    }
}