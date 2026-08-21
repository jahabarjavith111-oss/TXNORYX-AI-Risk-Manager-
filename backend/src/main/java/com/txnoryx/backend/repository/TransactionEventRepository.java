package com.txnoryx.backend.repository;

import com.txnoryx.backend.model.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {

    List<TransactionEvent> findByTransactionId(String transactionId);
}