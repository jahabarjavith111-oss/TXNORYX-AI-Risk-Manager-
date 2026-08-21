package com.txnoryx.backend.agent;

import lombok.*;
import com.txnoryx.backend.agent.AgentAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentResult {

    private String transactionId;

    private AgentAction action;

    private String status;

    private String message;

    private double confidence;

    public AgentResult(String transactionId,
                       AgentAction action,
                       String status,
                       String message,
                       double confidence) {
        this.transactionId = transactionId;
        this.action = action;
        this.status = status;
        this.message = message;
        this.confidence = confidence;
    }

    public String getTransactionId() { return transactionId; }
    public AgentAction getAction() { return action; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public double getConfidence() { return confidence; }
}