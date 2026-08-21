package com.txnoryx.backend.agent;

import com.txnoryx.backend.model.Transaction;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class ActionExecutor {

    private final PaymentSimulator paymentSimulator;

    public ActionExecutor(PaymentSimulator paymentSimulator) {
        this.paymentSimulator = paymentSimulator;
    }

    public AgentResult execute(
            Transaction transaction,
            AgentDecision decision) {

        switch (decision.getAction()) {

            case RETRY_PAYMENT:
                return retry(transaction, decision);

            case VERIFY_PAYMENT:
                return verify(transaction, decision);

            case ESCALATE:
                return escalate(transaction, decision);

            case BLOCK:
                return block(transaction, decision);

            case APPROVE:
            default:
                return approve(transaction, decision);
        }
    }

    private AgentResult retry(
            Transaction transaction,
            AgentDecision decision) {

        String result = paymentSimulator.retryPayment();

        return new AgentResult(transaction.getTransactionId(), AgentAction.RETRY_PAYMENT, result,
                        result.equals("SUCCESS")
                                ? "Payment recovered automatically"
                                : "Automatic retry failed",
                        decision.getConfidence());
    }

    private AgentResult verify(
            Transaction transaction,
            AgentDecision decision) {

        return new AgentResult(transaction.getTransactionId(), AgentAction.VERIFY_PAYMENT, "PENDING_VERIFICATION",
                        "Payment verification initiated",
                        decision.getConfidence());
    }

    private AgentResult escalate(
            Transaction transaction,
            AgentDecision decision) {

        return new AgentResult(transaction.getTransactionId(), AgentAction.ESCALATE, "ESCALATED",
                        "Transaction escalated for manual review",
                        decision.getConfidence());
    }

    private AgentResult block(
            Transaction transaction,
            AgentDecision decision) {

        return new AgentResult(transaction.getTransactionId(), AgentAction.BLOCK, "BLOCKED",
                        "Transaction blocked due to critical risk",
                        decision.getConfidence());
    }

    private AgentResult approve(
            Transaction transaction,
            AgentDecision decision) {

        return new AgentResult(transaction.getTransactionId(), AgentAction.APPROVE, "APPROVED",
                        "Transaction approved",
                        decision.getConfidence());
    }
}