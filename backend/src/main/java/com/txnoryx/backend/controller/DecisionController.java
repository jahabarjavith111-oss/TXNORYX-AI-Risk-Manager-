package com.txnoryx.backend.controller;

import com.txnoryx.backend.decision.DecisionResult;
import com.txnoryx.backend.decision.EconomicDecisionEngine;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
import com.txnoryx.backend.risk.RiskIntelligence;
import com.txnoryx.backend.routing.PaymentRoute;
import com.txnoryx.backend.routing.RouteOptimizer;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/decision")
public class DecisionController {
    private final TransactionRepository txRepo;
    private final RiskIntelligence riskIntelligence;
    private final EconomicDecisionEngine economicEngine;
    private final RouteOptimizer routeOptimizer;

    public DecisionController(TransactionRepository txRepo, RiskIntelligence riskIntelligence, EconomicDecisionEngine economicEngine, RouteOptimizer routeOptimizer) {
        this.txRepo = txRepo; this.riskIntelligence = riskIntelligence; this.economicEngine = economicEngine; this.routeOptimizer = routeOptimizer;
    }

    @GetMapping("/{transactionId}")
    public Map<String, Object> decide(@PathVariable String transactionId) {
        Transaction tx = txRepo.findByTransactionId(transactionId).orElseThrow();
        var risk = riskIntelligence.analyze(tx);
        var route = routeOptimizer.selectBestRoute();
        PaymentRoute pr = route != null ? route.getSelectedRoute() : null;
        DecisionResult dr = economicEngine.evaluate(tx, risk.getRecoveryProbability(), risk.getFraudProbability(), pr != null ? pr.getCost() : null, pr != null ? pr.getFriction() : null);
        java.util.HashMap<String,Object> m = new java.util.HashMap<>();
        m.put("transactionId", transactionId); m.put("amount", tx.getAmount()); m.put("fraudProbability", risk.getFraudProbability()); m.put("recoveryProbability", risk.getRecoveryProbability());
        m.put("expectedRecovery", dr.getExpectedValue().getExpectedRecovery()); m.put("fraudExposure", dr.getExpectedValue().getFraudExposure()); m.put("retryCost", dr.getExpectedValue().getRetryCost()); m.put("friction", dr.getExpectedValue().getFriction()); m.put("netValue", dr.getExpectedValue().getNetValue());
        m.put("recommendation", dr.getRecommendation()); m.put("reason", dr.getReason()); m.put("route", pr != null ? pr.getRouteCode() : "none");
        return m;
    }
}
