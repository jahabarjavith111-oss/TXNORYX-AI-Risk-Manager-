package com.txnoryx.backend.audit;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditService {
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) { this.repository = repository; }

    public AuditLog log(String transactionId, int riskScore, String riskLevel, int fraud, int recovery, String aiDecision, String safetyDecision, String reason, int confidence, double expectedValue, String routeCode) {
        AuditLog a = new AuditLog(transactionId, riskScore, riskLevel, fraud, recovery, aiDecision, safetyDecision, reason, confidence, expectedValue, routeCode);
        return repository.save(a);
    }

    public List<AuditLog> getByTransaction(String transactionId) { return repository.findByTransactionIdOrderByTimestampDesc(transactionId); }
    public List<AuditLog> recent() { return repository.findTop20ByOrderByTimestampDesc(); }
}
