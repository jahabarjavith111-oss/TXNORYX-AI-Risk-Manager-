package com.txnoryx.backend.routing;

public class RouteDecision {
    private final PaymentRoute selectedRoute;
    private final String reason;

    public RouteDecision(PaymentRoute selectedRoute, String reason) {
        this.selectedRoute = selectedRoute; this.reason = reason;
    }

    public PaymentRoute getSelectedRoute() { return selectedRoute; }
    public String getReason() { return reason; }
}
