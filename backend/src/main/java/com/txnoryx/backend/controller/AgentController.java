package com.txnoryx.backend.controller;

import com.txnoryx.backend.agent.AgentActionRecord;
import com.txnoryx.backend.agent.AgentResult;
import com.txnoryx.backend.agent.AutonomousWorkflowService;
import com.txnoryx.backend.security.IdempotencyStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AutonomousWorkflowService workflowService;
    private final IdempotencyStore idempotencyStore;

    public AgentController(AutonomousWorkflowService workflowService, IdempotencyStore idempotencyStore) {
        this.workflowService = workflowService; this.idempotencyStore=idempotencyStore;
    }

    @PostMapping(value = "/execute/{transactionId}", produces = "application/json")
    public ResponseEntity<?> execute(
            @PathVariable String transactionId, @RequestHeader(value="Idempotency-Key", required=false) String key) {
        String idem= key!=null? key : "agent:"+transactionId;
        if(idempotencyStore.isDuplicate(idem)) return ResponseEntity.status(HttpStatus.CONFLICT).body(java.util.Map.of("error","Duplicate agent execution - idempotent key already used"));
        return ResponseEntity.ok(workflowService.execute(transactionId));
    }

    @GetMapping(value = "/activity", produces = "application/json")
    public List<AgentActionRecord> getActivity() {
        return workflowService.getRecentActivity();
    }
}