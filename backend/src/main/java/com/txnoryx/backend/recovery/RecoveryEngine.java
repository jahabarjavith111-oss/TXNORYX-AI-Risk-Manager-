package com.txnoryx.backend.recovery;

import com.txnoryx.backend.model.Transaction;

import org.springframework.stereotype.Component;

@Component
public class RecoveryEngine {

    public RecoveryDecision decide(Transaction transaction) {

        String status = transaction.getStatus();
        String reason = transaction.getFailureReason();

        if ("TIMEOUT".equals(status)) {

            return new RecoveryDecision(RecoveryStrategy.RETRY,
                    "Temporary gateway timeout", 0.87, 2);
        }

        if (reason != null &&
                reason.toLowerCase().contains("gateway")) {

            return new RecoveryDecision(RecoveryStrategy.RETRY,
                    "Gateway failure may be temporary", 0.82, 2);
        }

        if ("DECLINED".equals(status)) {

            return new RecoveryDecision(RecoveryStrategy.ALTERNATIVE_ROUTE,
                    "Primary payment route declined", 0.68, 1);
        }

        if ("SUSPICIOUS".equals(status)) {

            return new RecoveryDecision(RecoveryStrategy.ESCALATE,
                    "Suspicious transaction requires review", 0.95, 0);
        }

        return new RecoveryDecision(RecoveryStrategy.NO_ACTION,
                "No recovery required", 0.99, 0);
    }
}