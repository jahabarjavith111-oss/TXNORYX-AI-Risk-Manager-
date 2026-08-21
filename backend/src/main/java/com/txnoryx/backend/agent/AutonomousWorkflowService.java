package com.txnoryx.backend.agent;

import com.txnoryx.backend.agent.AgentResult;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
import com.txnoryx.backend.risk.RiskEngine;
import com.txnoryx.backend.risk.RiskResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AutonomousWorkflowService {

    private final TransactionRepository transactionRepository;
    private final RiskEngine riskEngine;
    private final AutonomousAgent autonomousAgent;
    private final ActionExecutor actionExecutor;
    private final AgentActionRecordRepository agentActionRecordRepository;

    public AutonomousWorkflowService(TransactionRepository transactionRepository,
                                     RiskEngine riskEngine,
                                     AutonomousAgent autonomousAgent,
                                     ActionExecutor actionExecutor,
                                     AgentActionRecordRepository agentActionRecordRepository) {
        this.transactionRepository = transactionRepository;
        this.riskEngine = riskEngine;
        this.autonomousAgent = autonomousAgent;
        this.actionExecutor = actionExecutor;
        this.agentActionRecordRepository = agentActionRecordRepository;
    }

    public AgentResult execute(String transactionId) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionId(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"));

        RiskResult risk =
                riskEngine.calculate(transaction);

        AgentDecision decision =
                autonomousAgent.decide(
                        transaction,
                        risk);

        AgentResult result = actionExecutor.execute(
                transaction,
                decision);

        agentActionRecordRepository.save(
                new AgentActionRecord(
                        result.getTransactionId(),
                        result.getAction(),
                        result.getStatus(),
                        result.getMessage(),
                        result.getConfidence(),
                        LocalDateTime.now()));

        return result;
    }

    public java.util.List<AgentActionRecord> getRecentActivity() {
        return agentActionRecordRepository.findTop20ByOrderByCreatedAtDesc();
    }
}