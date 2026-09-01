package com.txnoryx.backend.recovery;

public enum RetryStrategy {
    RETRY_NOW,
    DELAY_RETRY,
    SWITCH_ROUTE,
    REQUEST_AUTHENTICATION,
    HUMAN_REVIEW,
    BLOCK
}
