package com.txnoryx.backend.dto;

public class SimulateRequest {

    public SimulateRequest() {}
    public SimulateRequest(String scenario) { this.scenario = scenario; }

    private String scenario;


    public String getScenario() {
        return scenario;
    }


    public void setScenario(String scenario) {
        this.scenario = scenario;
    }
}
