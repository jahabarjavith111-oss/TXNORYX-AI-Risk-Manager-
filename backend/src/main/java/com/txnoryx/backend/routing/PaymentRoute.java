package com.txnoryx.backend.routing;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_routes")
public class PaymentRoute {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String routeCode;
    private double successRate;
    private double avgLatency;
    private double cost;
    private double friction;
    private boolean isActive = true;

    public PaymentRoute() {}
    public PaymentRoute(String routeCode, double successRate, double avgLatency, double cost, double friction) {
        this.routeCode = routeCode; this.successRate = successRate; this.avgLatency = avgLatency; this.cost = cost; this.friction = friction;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getRouteCode() { return routeCode; } public void setRouteCode(String c) { this.routeCode = c; }
    public double getSuccessRate() { return successRate; } public void setSuccessRate(double v) { this.successRate = v; }
    public double getAvgLatency() { return avgLatency; } public void setAvgLatency(double v) { this.avgLatency = v; }
    public double getCost() { return cost; } public void setCost(double v) { this.cost = v; }
    public double getFriction() { return friction; } public void setFriction(double v) { this.friction = v; }
    public boolean isActive() { return isActive; } public void setActive(boolean v) { this.isActive = v; }
}
