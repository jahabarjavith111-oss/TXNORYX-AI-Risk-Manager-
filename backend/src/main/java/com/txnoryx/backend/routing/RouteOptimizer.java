package com.txnoryx.backend.routing;

import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component
public class RouteOptimizer {
    private final PaymentRouteRepository repository;

    public RouteOptimizer(PaymentRouteRepository repository) { this.repository = repository; }

    public RouteDecision selectBestRoute(String failedRoute) {
        List<PaymentRoute> routes = repository.findByIsActiveTrue();
        if (routes.isEmpty()) return null;
        PaymentRoute best = routes.stream().filter(r -> !r.getRouteCode().equals(failedRoute)).max(Comparator.comparingDouble(PaymentRoute::getSuccessRate)).orElse(routes.get(0));
        return new RouteDecision(best, "Selected " + best.getRouteCode() + " success " + best.getSuccessRate() + "% latency " + best.getAvgLatency() + "s after " + (failedRoute != null ? failedRoute + " failed" : "initial"));
    }

    public RouteDecision selectBestRoute() { return selectBestRoute(null); }
}
