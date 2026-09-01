package com.txnoryx.backend.risk;

import com.txnoryx.backend.model.Transaction;
import org.springframework.stereotype.Component;

@Component("riskRecoveryEngine")
public class RecoveryEngine {

    private final com.txnoryx.backend.recovery.RecoveryEngine delegate;

    public RecoveryEngine(com.txnoryx.backend.recovery.RecoveryEngine delegate) {
        this.delegate = delegate;
    }

    public int recoveryProbability(Transaction transaction) {
        return (int) Math.round(delegate.decide(transaction).getProbability() * 100);
    }

    public com.txnoryx.backend.recovery.RecoveryDecision decide(Transaction transaction) {
        return delegate.decide(transaction);
    }
}
