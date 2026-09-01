package com.txnoryx.backend.decision;

import com.txnoryx.backend.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EconomicDecisionEngine {

    @Value("${txnoryx.economic.retry-cost:10}")
    private double defaultRetryCost;

    @Value("${txnoryx.economic.friction:5}")
    private double defaultFriction;

    @Value("${txnoryx.economic.route-cost-enabled:false}")
    private boolean routeCostEnabled;

    public DecisionResult evaluate(Transaction tx, int recoveryProbability, int fraudProbability, Double routeCost, Double routeFriction) {
        double amount = tx.getAmount() != null ? tx.getAmount().doubleValue() : 0;
        double expectedRecovery = amount * recoveryProbability / 100.0;
        double fraudExposure = amount * fraudProbability / 100.0;
        double retryCost = routeCost != null && routeCostEnabled ? routeCost : defaultRetryCost;
        double friction = routeFriction != null && routeCostEnabled ? routeFriction : defaultFriction;
        ExpectedValue ev = new ExpectedValue(expectedRecovery, fraudExposure, retryCost, friction);
        double net = ev.getNetValue();
        String rec;
        String reason;
        if (fraudProbability >= 80) {
            rec = "BLOCK";
            reason = "Fraud exposure ₹" + String.format("%.0f", fraudExposure) + " exceeds tolerance";
        } else if (net > 0 && recoveryProbability > 50) {
            rec = "RETRY";
            reason = "Expected net ₹" + String.format("%.0f", net) + " favorable (recovery " + recoveryProbability + "%)";
        } else if (net > -retryCost) {
            rec = "REVIEW";
            reason = "Marginal net ₹" + String.format("%.0f", net) + " requires human review";
        } else {
            rec = "BLOCK";
            reason = "Unfavorable net ₹" + String.format("%.0f", net) + " recovery " + recoveryProbability + "% fraud " + fraudProbability + "%";
        }
        return new DecisionResult(rec, ev, reason);
    }

    public DecisionResult evaluate(Transaction tx, int recoveryProbability, int fraudProbability) {
        return evaluate(tx, recoveryProbability, fraudProbability, null, null);
    }
}
