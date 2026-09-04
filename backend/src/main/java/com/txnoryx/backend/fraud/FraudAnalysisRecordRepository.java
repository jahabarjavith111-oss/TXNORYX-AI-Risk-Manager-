package com.txnoryx.backend.fraud;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FraudAnalysisRecordRepository extends JpaRepository<FraudAnalysisRecord, Long> {
    Optional<FraudAnalysisRecord> findByTransactionId(String transactionId);

    List<FraudAnalysisRecord> findAllByTransactionIdIgnoreCase(String transactionId);
}
