package com.txnoryx.backend.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTransactionIdOrderByTimestampDesc(String transactionId);
    List<AuditLog> findTop20ByOrderByTimestampDesc();
}
