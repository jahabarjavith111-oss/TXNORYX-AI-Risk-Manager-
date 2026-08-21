package com.txnoryx.backend.service;

import com.txnoryx.backend.dto.DashboardStatsResponse;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
import com.txnoryx.backend.dto.TransactionSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private TransactionRepository transactionRepository;

    public DashboardService() {}

    private int amountRiskScore(BigDecimal amount) {
        if (amount == null) return 1;
        double d = amount.doubleValue();
        if (d >= 50000) return 3;
        if (d >= 10000) return 2;
        return 1;
    }

    private int statusRiskScore(String status) {
        if (status == null) return 1;
        switch (status.trim().toUpperCase()) {
            case "SUSPICIOUS": return 4;
            case "FAILED":
            case "DECLINED":
            case "TIMEOUT": return 3;
            case "RECOVERED": return 2;
            case "SUCCESS":
            case "PENDING": return 1;
            default: return 1;
        }
    }

    private int hybridRiskScore(String status, BigDecimal amount) {
        int s = statusRiskScore(status);
        int a = amountRiskScore(amount);
        return Math.max(s, a);
    }

    private String riskLabel(int score) {
        return switch (score) {
            case 4 -> "CRITICAL";
            case 3 -> "HIGH";
            case 2 -> "MEDIUM";
            case 1 -> "LOW";
            default -> "LOW";
        };
    }

    public DashboardStatsResponse getStats() {

        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionSummaryDTO> recentTxns =
                transactionRepository.findRecentTransactionSummaries();

        BigDecimal totalVolume =
                transactions.stream()
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        long total = transactions.size();

        long successful =
                transactions.stream()
                        .filter(t -> "SUCCESS".equals(t.getStatus()))
                        .count();

        long failed =
                transactions.stream()
                        .filter(t -> "FAILED".equals(t.getStatus())
                                || "TIMEOUT".equals(t.getStatus())
                                || "DECLINED".equals(t.getStatus()))
                        .count();

        long recovered =
                transactions.stream()
                        .filter(t -> "RECOVERED".equals(t.getStatus()))
                        .count();

        // --- NEW: status breakdown counts (PENDING, SUCCESS, FAILED, TIMEOUT, DECLINED, SUSPICIOUS, RECOVERED) ---
        long[] statusBreakdown = new long[7];
        for (Transaction t : transactions) {
            String s = t.getStatus();
            switch (s) {
                case "PENDING": statusBreakdown[0]++; break;
                case "SUCCESS": statusBreakdown[1]++; break;
                case "FAILED": statusBreakdown[2]++; break;
                case "TIMEOUT": statusBreakdown[3]++; break;
                case "DECLINED": statusBreakdown[4]++; break;
                case "SUSPICIOUS": statusBreakdown[5]++; break;
                case "RECOVERED": statusBreakdown[6]++; break;
            }
        }

        // --- NEW: payment method breakdown ---
        long[] paymentMethodBreakdown = new long[4];
        for (Transaction t : transactions) {
            String pm = t.getPaymentMethod();
            switch (pm) {
                case "UPI": paymentMethodBreakdown[0]++; break;
                case "CARD": paymentMethodBreakdown[1]++; break;
                case "NET BANKING": paymentMethodBreakdown[2]++; break;
                case "WALLET": paymentMethodBreakdown[3]++; break;
            }
        }

        // --- NEW: risk breakdown (LOW/MEDIUM/HIGH/CRITICAL counts) ---
        long[] riskBreakdown = new long[4];
        for (Transaction t : transactions) {
            int score = hybridRiskScore(t.getStatus(), t.getAmount());
            switch (score) {
                case 4: riskBreakdown[3]++; break; // CRITICAL
                case 3: riskBreakdown[2]++; break; // HIGH
                case 2: riskBreakdown[1]++; break; // MEDIUM
                case 1:
                default: riskBreakdown[0]++; break; // LOW
            }
        }

        // --- NEW: total suspicious + total high-value ---
        long totalSuspicious =
                transactions.stream()
                        .filter(t -> "SUSPICIOUS".equals(t.getStatus()))
                        .count();
        long totalHighValue =
                transactions.stream()
                        .filter(t -> t.getAmount() != null && t.getAmount().doubleValue() >= 50000)
                        .count();

        DashboardStatsResponse stats = new DashboardStatsResponse();
        stats.setTotalTransactions(total);
        stats.setTotalVolume(totalVolume);
        stats.setSuccessfulTransactions(successful);
        stats.setFailedTransactions(failed);
        stats.setHighRiskTransactions((int) Math.max(
                statusRiskScore("SUSPICIOUS"),
                amountRiskScore(totalVolume)
        ));
        stats.setRecoveredTransactions(recovered);
        stats.setStatusBreakdown(statusBreakdown);
        stats.setPaymentMethodBreakdown(paymentMethodBreakdown);
        stats.setRiskBreakdown(riskBreakdown);
        stats.setTotalSuspicious(totalSuspicious);
        stats.setTotalHighValue(totalHighValue);

        return stats;
    }
}