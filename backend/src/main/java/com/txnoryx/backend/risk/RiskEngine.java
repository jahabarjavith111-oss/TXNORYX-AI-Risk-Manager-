package com.txnoryx.backend.risk;

import com.txnoryx.backend.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class RiskEngine {

    public RiskResult calculate(Transaction transaction) {

        int score = 0;
        StringBuilder reasons = new StringBuilder();

        if (transaction.getAmount() != null
                && transaction.getAmount().doubleValue() > 50000) {
            score += 25;
            reasons.append("High transaction amount; ");
        }

        if ("TIMEOUT".equals(transaction.getStatus())) {
            score += 20;
            reasons.append("Payment timeout; ");
        }

        if ("DECLINED".equals(transaction.getStatus())) {
            score += 15;
            reasons.append("Payment declined; ");
        }

        if ("SUSPICIOUS".equals(transaction.getStatus())) {
            score += 30;
            reasons.append("Suspicious transaction flag; ");
            if (transaction.getAmount() != null && transaction.getAmount().doubleValue() > 100000) {
                score += 30;
                reasons.append("Critical high-value suspicious; ");
            }
        }

        if ("DECLINED".equals(transaction.getStatus())
                && transaction.getAmount() != null
                && transaction.getAmount().doubleValue() > 50000) {
            score += 25;
            reasons.append("High-value decline; ");
        }

        if (transaction.getFailureReason() != null
                && transaction.getFailureReason()
                .toLowerCase()
                .contains("gateway")) {
            score += 15;
            reasons.append("Gateway failure; ");
        }

        if (score > 100) {
            score = 100;
        }

        RiskLevel level;

        if (score <= 30) {
            level = RiskLevel.LOW;
        } else if (score <= 60) {
            level = RiskLevel.MEDIUM;
        } else if (score <= 80) {
            level = RiskLevel.HIGH;
        } else {
            level = RiskLevel.CRITICAL;
        }

        return new RiskResult(score, level, reasons.toString());
    }
}