package com.txnoryx.backend.failure;

import com.txnoryx.backend.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FailureAnalyzer {

    private static final Pattern GATEWAY_TIMEOUT = Pattern.compile("gateway.*timeout|timeout.*gateway|\\b504\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern INSUFFICIENT = Pattern.compile("insufficient.*(fund|balance)", Pattern.CASE_INSENSITIVE);
    private static final Pattern LIMIT = Pattern.compile("limit.*exceed|exceed.*limit|daily limit", Pattern.CASE_INSENSITIVE);
    private static final Pattern NETWORK = Pattern.compile("bank.*timeout|network failure|upi pin expired|response timeout", Pattern.CASE_INSENSITIVE);
    private static final Pattern SUSPICIOUS = Pattern.compile("suspicious|unusual|high.?risk|large value|risk flagged", Pattern.CASE_INSENSITIVE);
    private static final Pattern BANK_DECLINE = Pattern.compile("declined|invalid card|card blocked|declined by bank|card declined", Pattern.CASE_INSENSITIVE);

    public FailureResult analyze(Transaction transaction) {
        return analyze(transaction.getFailureReason(), transaction.getStatus());
    }

    public FailureResult analyze(String reason, String status) {
        String r = reason != null ? reason : "";
        String s = status != null ? status : "";
        String norm = r.isEmpty() ? s : r;

        if (GATEWAY_TIMEOUT.matcher(r).find()) return new FailureResult(FailureType.GATEWAY_TIMEOUT, 0.95, "gateway timeout", norm);
        if (INSUFFICIENT.matcher(r).find()) return new FailureResult(FailureType.INSUFFICIENT_FUNDS, 0.96, "insufficient", norm);
        if (LIMIT.matcher(r).find()) return new FailureResult(FailureType.LIMIT_EXCEEDED, 0.93, "limit exceeded", norm);
        if (SUSPICIOUS.matcher(r).find()) return new FailureResult(FailureType.SUSPICIOUS_ACTIVITY, 0.92, "suspicious", norm);
        if (NETWORK.matcher(r).find()) return new FailureResult(FailureType.NETWORK_FAILURE, 0.90, "network", norm);
        if (BANK_DECLINE.matcher(r).find()) return new FailureResult(FailureType.BANK_DECLINE, 0.88, "bank decline", norm);

        if ("TIMEOUT".equals(s)) return new FailureResult(FailureType.GATEWAY_TIMEOUT, 0.80, "TIMEOUT status", norm);
        if ("DECLINED".equals(s)) return new FailureResult(FailureType.BANK_DECLINE, 0.80, "DECLINED status", norm);
        if ("SUSPICIOUS".equals(s)) return new FailureResult(FailureType.SUSPICIOUS_ACTIVITY, 0.85, "SUSPICIOUS status", norm);
        if ("FAILED".equals(s) && !r.isEmpty()) return new FailureResult(FailureType.UNKNOWN, 0.60, "FAILED", norm);
        return new FailureResult(FailureType.UNKNOWN, 0.50, "unknown", norm);
    }

    public String explain(FailureResult result) {
        switch (result.getType()) {
            case GATEWAY_TIMEOUT: return "Temporary Gateway Failure";
            case BANK_DECLINE: return "Bank Decline";
            case INSUFFICIENT_FUNDS: return "Insufficient Funds";
            case NETWORK_FAILURE: return "Network Failure";
            case SUSPICIOUS_ACTIVITY: return "Suspicious Activity";
            case LIMIT_EXCEEDED: return "Limit Exceeded";
            default: return "Unknown Failure";
        }
    }
}
