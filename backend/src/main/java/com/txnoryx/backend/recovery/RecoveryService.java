package com.txnoryx.backend.recovery;

import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
import com.txnoryx.backend.risk.RiskIntelligence;
import com.txnoryx.backend.risk.RiskResult;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecoveryService {

    private final TransactionRepository transactionRepository;
    private final RecoveryEngine recoveryEngine;
    private final PaymentRecoverySimulator simulator;
    private final RecoveryActionRepository recoveryRepository;
    private final RiskIntelligence riskIntelligence;

    public RecoveryService(TransactionRepository transactionRepository,
                           RecoveryEngine recoveryEngine,
                           PaymentRecoverySimulator simulator,
                           RecoveryActionRepository recoveryRepository,
                           RiskIntelligence riskIntelligence) {
        this.transactionRepository = transactionRepository;
        this.recoveryEngine = recoveryEngine;
        this.simulator = simulator;
        this.recoveryRepository = recoveryRepository;
        this.riskIntelligence = riskIntelligence;
    }

    @org.springframework.transaction.annotation.Transactional
    public RecoveryResult recover(String transactionId) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionId(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"));

        RiskResult intel = riskIntelligence.analyze(transaction);
        RecoveryDecision decision = recoveryEngine.decideIntelligent(transaction, intel.getFraudProbability(), intel.getRecoveryProbability());
        RetryStrategy retryStrategy = recoveryEngine.toRetryStrategy(decision);

        RecoveryResult result;

        if (decision.getStrategy() == RecoveryStrategy.RETRY) {
            result = executeRetry(transaction, decision);
            result.setRetryStrategy(retryStrategy);
        }
        else if (decision.getStrategy() == RecoveryStrategy.ALTERNATIVE_ROUTE) {
            result = executeAlternativeRoute(transaction, decision);
            result.setRetryStrategy(RetryStrategy.SWITCH_ROUTE);
        }
        else if (decision.getStrategy() == RecoveryStrategy.VERIFY) {
            result = new RecoveryResult(transactionId, RecoveryStrategy.VERIFY, RetryStrategy.REQUEST_AUTHENTICATION, "PENDING_AUTH", 0, decision.getProbability(), "Request authentication");
        }
        else if (decision.getStrategy() == RecoveryStrategy.ESCALATE) {
            result = new RecoveryResult(transactionId, RecoveryStrategy.ESCALATE, RetryStrategy.HUMAN_REVIEW, "ESCALATED", 0, decision.getProbability(), "Human review required");
        }
        else if (decision.getStrategy() == RecoveryStrategy.BLOCK) {
            result = new RecoveryResult(transactionId, RecoveryStrategy.BLOCK, RetryStrategy.BLOCK, "BLOCKED", 0, decision.getProbability(), "Transaction blocked");
        }
        else {
            result = new RecoveryResult(transactionId, RecoveryStrategy.NO_ACTION, RetryStrategy.HUMAN_REVIEW, "NO_ACTION", 0, decision.getProbability(), "No recovery required");
        }

        recoveryRepository.save(
                new RecoveryAction(
                        transactionId,
                        result.getStrategy(),
                        result.getStatus(),
                        result.getAttempts(),
                        result.getProbability(),
                        result.getMessage(),
                        LocalDateTime.now()
                )
        );

        return result;
    }

    private RecoveryResult executeRetry(
            Transaction transaction,
            RecoveryDecision decision) {

        int attempts = 0;

        while (attempts < decision.getMaxAttempts()) {

            attempts++;

            if (simulator.retry()) {

                return new RecoveryResult(transaction.getTransactionId(), RecoveryStrategy.RETRY, "RECOVERED", attempts, decision.getProbability(), "Payment recovered through retry");
            }
        }

        return new RecoveryResult(transaction.getTransactionId(), RecoveryStrategy.RETRY, "FAILED", decision.getMaxAttempts(), decision.getProbability(), "Retry attempts exhausted");
    }

    private RecoveryResult executeAlternativeRoute(
            Transaction transaction,
            RecoveryDecision decision) {

        boolean success = simulator.alternativeRoute();

        return new RecoveryResult(transaction.getTransactionId(), RecoveryStrategy.ALTERNATIVE_ROUTE, success ? "RECOVERED" : "FAILED", 1, decision.getProbability(), success ? "Payment recovered through alternative route" : "Alternative route failed");
    }
}