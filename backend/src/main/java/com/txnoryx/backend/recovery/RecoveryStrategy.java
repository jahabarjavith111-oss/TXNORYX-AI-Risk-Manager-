package com.txnoryx.backend.recovery;

public enum RecoveryStrategy {

    RETRY,
    ALTERNATIVE_ROUTE,
    VERIFY,
    ESCALATE,
    BLOCK,
    NO_ACTION
}