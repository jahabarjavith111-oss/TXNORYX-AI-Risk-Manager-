package com.txnoryx.backend.repository;

import com.txnoryx.backend.model.AIAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AIAnalysisRepository
        extends JpaRepository<AIAnalysis, Long> {

    Optional<AIAnalysis> findByTransactionId(String transactionId);

    Optional<AIAnalysis> findByTransactionIdIgnoreCase(String transactionId);
}