package com.txnoryx.backend.controller;

import com.txnoryx.backend.audit.AuditLog;
import com.txnoryx.backend.audit.AuditService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) { this.auditService = auditService; }

    @GetMapping("/{transactionId}")
    public List<AuditLog> get(@PathVariable String transactionId) { return auditService.getByTransaction(transactionId); }

    @GetMapping
    public List<AuditLog> recent() { return auditService.recent(); }
}
