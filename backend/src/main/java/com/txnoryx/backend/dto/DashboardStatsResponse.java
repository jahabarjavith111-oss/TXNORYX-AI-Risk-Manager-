package com.txnoryx.backend.dto;

import lombok.*;

import java.math.BigDecimal;

public class DashboardStatsResponse {

    private long totalTransactions;
    private BigDecimal totalVolume;
    private long successfulTransactions;
    private long failedTransactions;
    private long highRiskTransactions;
    private long recoveredTransactions;

    // NEW: breakdown fields
    private long[] statusBreakdown;     // [PENDING, SUCCESS, FAILED, TIMEOUT, DECLINED, SUSPICIOUS, RECOVERED]
    private long[] paymentMethodBreakdown; // [UPI, CARD, NET BANKING, WALLET]
    private long[] riskBreakdown;      // [LOW, MEDIUM, HIGH, CRITICAL]
    private long totalSuspicious;
    private long totalHighValue;

    // Risk scoring helpers (hybrid worst-of-both)
    private static final int STATUS_SCORE_SUSPICIOUS = 4;
    private static final int STATUS_SCORE_HIGH_RISK = 3;
    private static final int STATUS_SCORE_MEDIUM = 2;
    private static final int STATUS_SCORE_LOW = 1;
    private static final int AMOUNT_SCORE_HIGH = 3;
    private static final int AMOUNT_SCORE_MEDIUM = 2;
    private static final int AMOUNT_SCORE_LOW = 1;

    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public BigDecimal getTotalVolume() { return totalVolume; }
    public void setTotalVolume(BigDecimal totalVolume) { this.totalVolume = totalVolume; }

    public long getSuccessfulTransactions() { return successfulTransactions; }
    public void setSuccessfulTransactions(long successfulTransactions) { this.successfulTransactions = successfulTransactions; }

    public long getFailedTransactions() { return failedTransactions; }
    public void setFailedTransactions(long failedTransactions) { this.failedTransactions = failedTransactions; }

    public long getHighRiskTransactions() { return highRiskTransactions; }
    public void setHighRiskTransactions(long highRiskTransactions) { this.highRiskTransactions = highRiskTransactions; }

    public long getRecoveredTransactions() { return recoveredTransactions; }
    public void setRecoveredTransactions(long recoveredTransactions) { this.recoveredTransactions = recoveredTransactions; }

    // --- NEW: status breakdown ---
    public long[] getStatusBreakdown() { return statusBreakdown; }
    public void setStatusBreakdown(long[] statusBreakdown) { this.statusBreakdown = statusBreakdown; }

    // --- NEW: payment method breakdown ---
    public long[] getPaymentMethodBreakdown() { return paymentMethodBreakdown; }
    public void setPaymentMethodBreakdown(long[] paymentMethodBreakdown) { this.paymentMethodBreakdown = paymentMethodBreakdown; }

    // --- NEW: risk breakdown ---
    public long[] getRiskBreakdown() { return riskBreakdown; }
    public void setRiskBreakdown(long[] riskBreakdown) { this.riskBreakdown = riskBreakdown; }

    // --- NEW: aggregates ---
    public long getTotalSuspicious() { return totalSuspicious; }
    public void setTotalSuspicious(long totalSuspicious) { this.totalSuspicious = totalSuspicious; }

    public long getTotalHighValue() { return totalHighValue; }
    public void setTotalHighValue(long totalHighValue) { this.totalHighValue = totalHighValue; }
}