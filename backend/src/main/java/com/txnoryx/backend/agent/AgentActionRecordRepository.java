package com.txnoryx.backend.agent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentActionRecordRepository
        extends JpaRepository<AgentActionRecord, Long> {

    List<AgentActionRecord> findTop20ByOrderByCreatedAtDesc();
}