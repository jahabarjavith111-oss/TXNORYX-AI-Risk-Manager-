package com.txnoryx.backend.agent;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AgentAction {

    APPROVE,
    RETRY_PAYMENT,
    VERIFY_PAYMENT,
    ESCALATE,
    BLOCK
}