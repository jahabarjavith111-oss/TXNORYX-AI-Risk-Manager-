package com.txnoryx.backend.agent;

import com.txnoryx.backend.decision.DecisionResult;
import com.txnoryx.backend.failure.FailureResult;
import com.txnoryx.backend.recovery.RecoveryDecision;
import com.txnoryx.backend.risk.RiskResult;
import com.txnoryx.backend.routing.RouteDecision;
import com.txnoryx.backend.safety.SafetyDecision;

public class AgentContext {
    private RiskResult risk;
    private FailureResult failure;
    private RecoveryDecision recovery;
    private DecisionResult economic;
    private RouteDecision route;
    private SafetyDecision safety;

    public AgentContext() {}
    public AgentContext(RiskResult risk, FailureResult failure, RecoveryDecision recovery, DecisionResult economic, RouteDecision route, SafetyDecision safety) {
        this.risk = risk; this.failure = failure; this.recovery = recovery; this.economic = economic; this.route = route; this.safety = safety;
    }

    public RiskResult getRisk() { return risk; } public void setRisk(RiskResult v) { this.risk = v; }
    public FailureResult getFailure() { return failure; } public void setFailure(FailureResult v) { this.failure = v; }
    public RecoveryDecision getRecovery() { return recovery; } public void setRecovery(RecoveryDecision v) { this.recovery = v; }
    public DecisionResult getEconomic() { return economic; } public void setEconomic(DecisionResult v) { this.economic = v; }
    public RouteDecision getRoute() { return route; } public void setRoute(RouteDecision v) { this.route = v; }
    public SafetyDecision getSafety() { return safety; } public void setSafety(SafetyDecision v) { this.safety = v; }
}
