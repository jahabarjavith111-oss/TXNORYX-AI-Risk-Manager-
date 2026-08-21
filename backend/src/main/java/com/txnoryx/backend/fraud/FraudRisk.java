package com.txnoryx.backend.fraud;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum FraudRisk {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    @JsonValue
    public String toValue() {
        return name();
    }
}