package com.txnoryx.backend.routing;

public class RoutePerformance {
    private final String routeCode;
    private final double successRate;
    private final double avgLatency;

    public RoutePerformance(String routeCode, double successRate, double avgLatency) {
        this.routeCode = routeCode; this.successRate = successRate; this.avgLatency = avgLatency;
    }

    public String getRouteCode() { return routeCode; }
    public double getSuccessRate() { return successRate; }
    public double getAvgLatency() { return avgLatency; }
}
