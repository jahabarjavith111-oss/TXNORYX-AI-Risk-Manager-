package com.txnoryx.backend.repository;

import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.dto.TransactionSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    long countByStatus(String status);

    long countByUserId(Long userId);

    long countByUserIdAndDeviceId(Long userId, String deviceId);

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.userId = :userId AND t.status IN ('FAILED','TIMEOUT','DECLINED')")
    long countFailuresByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt IS NOT NULL ORDER BY t.createdAt DESC")
    List<Transaction> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT NEW com.txnoryx.backend.dto.TransactionSummaryDTO(t.transactionId, t.amount, t.paymentMethod, t.status, t.createdAt) FROM Transaction t WHERE t.createdAt IS NOT NULL ORDER BY t.createdAt DESC")
    List<TransactionSummaryDTO> findRecentTransactionSummaries();
}