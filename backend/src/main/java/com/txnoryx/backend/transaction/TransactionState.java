package com.txnoryx.backend.transaction;

public enum TransactionState {
    INITIATED, PROCESSING, SUCCESS, FAILED, ANALYZING, AI_DECISION, RECOVERY, BLOCKED
}
