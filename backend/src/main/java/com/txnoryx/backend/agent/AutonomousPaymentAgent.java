package com.txnoryx.backend.agent;

import com.txnoryx.backend.decision.DecisionResult;
import com.txnoryx.backend.decision.EconomicDecisionEngine;
import com.txnoryx.backend.failure.FailureAnalyzer;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.recovery.RecoveryEngine;
import com.txnoryx.backend.risk.RiskIntelligence;
import com.txnoryx.backend.risk.RiskResult;
import com.txnoryx.backend.routing.PaymentRoute;
import com.txnoryx.backend.routing.RouteDecision;
import com.txnoryx.backend.routing.RouteOptimizer;
import com.txnoryx.backend.audit.AuditService;
import com.txnoryx.backend.safety.SafetyDecision;
import com.txnoryx.backend.safety.SafetyGovernor;
import org.springframework.stereotype.Component;

@Component
public class AutonomousPaymentAgent {
    private final RiskIntelligence riskIntelligence;
    private final FailureAnalyzer failureAnalyzer;
    private final RecoveryEngine recoveryEngine;
    private final EconomicDecisionEngine economicEngine;
    private final RouteOptimizer routeOptimizer;
    private final SafetyGovernor safetyGovernor;
    private final AutonomousAgent autonomousAgent;
    private final AuditService auditService;

    public AutonomousPaymentAgent(RiskIntelligence riskIntelligence, FailureAnalyzer failureAnalyzer, RecoveryEngine recoveryEngine, EconomicDecisionEngine economicEngine, RouteOptimizer routeOptimizer, SafetyGovernor safetyGovernor, AutonomousAgent autonomousAgent, AuditService auditService) {
        this.riskIntelligence = riskIntelligence; this.failureAnalyzer = failureAnalyzer; this.recoveryEngine = recoveryEngine; this.economicEngine = economicEngine; this.routeOptimizer = routeOptimizer; this.safetyGovernor = safetyGovernor; this.autonomousAgent = autonomousAgent; this.auditService = auditService;
    }

    public AgentDecision decide(Transaction tx, int retries) {
        RiskResult risk = riskIntelligence.analyze(tx);
        FailureResult failure = failureAnalyzer.analyze(tx);
        int fraud = risk.getFraudProbability();
        int recovery = risk.getRecoveryProbability();
        int confidence = risk.getConfidence();
        int riskScore = risk.getScore();
        var recoveryDecision = recoveryEngine.decideIntelligent(tx, fraud, recovery);
        RouteDecision route = routeOptimizer.selectBestRoute(failure.getType().name().contains("GATEWAY") ? "Route A" : null);
        PaymentRoute pr = route != null ? route.getSelectedRoute() : null;
        DecisionResult economic = economicEngine.evaluate(tx, recovery, fraud, pr != null ? pr.getCost() : null, pr != null ? pr.getFriction() : null);
        SafetyDecision safety = safetyGovernor.evaluate(tx, fraud, riskScore, confidence, retries);
        AgentContext ctx = new AgentContext(risk, failure, recoveryDecision, economic, route, safety);
        if (!safety.isAllowed()) {
            String override = safety.getAction();
            AgentDecision d = "BLOCK".equals(override) ? new AgentDecision(AgentAction.BLOCK, "Governor: " + safety.getReason(), 0.96) : new AgentDecision(AgentAction.ESCALATE, "Governor: " + safety.getReason(), 0.90);
            try { auditService.log(tx.getTransactionId(), riskScore, risk.getLevel().name(), fraud, recovery, d.getAction().name(), safety.getAction(), d.getReason(), (int)(d.getConfidence()*100), economic.getExpectedValue().getNetValue(), pr != null ? pr.getRouteCode() : null); } catch (Exception ignored) {}
            return d;
        }
        AgentDecision base = autonomousAgent.decideWithFailure(tx, risk, failure);
        AgentDecision result;
        if ("RETRY".equals(economic.getRecommendation()) && recovery > 70 && fraud < 50) {
            if (recoveryDecision.getStrategy().name().equals("RETRY")) result = new AgentDecision(AgentAction.RETRY_PAYMENT, "Agent: " + economic.getReason() + " | " + (route != null ? route.getReason() : ""), 0.93);
            else if (recoveryDecision.getStrategy().name().equals("ALTERNATIVE_ROUTE")) result = new AgentDecision(AgentAction.RETRY_PAYMENT, "Agent: SWITCH " + (pr != null ? pr.getRouteCode() : "route") + " | " + economic.getReason(), 0.92);
            else if ("BLOCK".equals(economic.getRecommendation())) result = new AgentDecision(AgentAction.BLOCK, "Agent: " + economic.getReason(), 0.95);
            else result = base;
        } else if ("BLOCK".equals(economic.getRecommendation())) result = new AgentDecision(AgentAction.BLOCK, "Agent: " + economic.getReason(), 0.95);
        else result = base;
        try { auditService.log(tx.getTransactionId(), riskScore, risk.getLevel().name(), fraud, recovery, result.getAction().name(), safety.getAction(), result.getReason(), (int)(result.getConfidence()*100), economic.getExpectedValue().getNetValue(), pr != null ? pr.getRouteCode() : null); } catch (Exception ignored) {}
        return result;
    }
}
