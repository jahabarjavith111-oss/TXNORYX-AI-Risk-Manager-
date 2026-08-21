package com.txnoryx.backend.controller;

import com.txnoryx.backend.agent.AgentActionRecord;
import com.txnoryx.backend.agent.AgentResult;
import com.txnoryx.backend.agent.AutonomousWorkflowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AutonomousWorkflowService workflowService;

    public AgentController(AutonomousWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping(value = "/execute/{transactionId}", produces = "application/json")
    public AgentResult execute(
            @PathVariable String transactionId) {

        return workflowService.execute(transactionId);
    }

    @GetMapping(value = "/activity", produces = "application/json")
    public List<AgentActionRecord> getActivity() {
        return workflowService.getRecentActivity();
    }
}