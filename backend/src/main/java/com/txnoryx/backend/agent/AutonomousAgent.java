package com.txnoryx.backend.agent;

import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.risk.RiskResult;

import org.springframework.stereotype.Service;

@Service
public class AutonomousAgent {

    public AgentDecision decide(
            Transaction transaction,
            RiskResult risk) {

        if (risk.getLevel().name().equals("CRITICAL")) {

            return new AgentDecision(AgentAction.BLOCK, "Critical risk detected", 0.95);
        }

        if ("TIMEOUT".equals(transaction.getStatus())) {

            return new AgentDecision(AgentAction.RETRY_PAYMENT, "Payment timeout detected", 0.92);
        }

        if ("DECLINED".equals(transaction.getStatus())) {

            return new AgentDecision(AgentAction.VERIFY_PAYMENT, "Payment declined and requires verification", 0.89);
        }

        if (risk.getLevel().name().equals("HIGH")) {

            return new AgentDecision(AgentAction.ESCALATE, "High risk requires manual review", 0.93);
        }

        return new AgentDecision(AgentAction.APPROVE, "Transaction appears normal", 0.97);
    }
}