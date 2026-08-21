package com.txnoryx.backend.recovery;

import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.repository.TransactionRepository;
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

    public RecoveryService(TransactionRepository transactionRepository,
                           RecoveryEngine recoveryEngine,
                           PaymentRecoverySimulator simulator,
                           RecoveryActionRepository recoveryRepository) {
        this.transactionRepository = transactionRepository;
        this.recoveryEngine = recoveryEngine;
        this.simulator = simulator;
        this.recoveryRepository = recoveryRepository;
    }

    @org.springframework.transaction.annotation.Transactional
    public RecoveryResult recover(String transactionId) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionId(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"));

        RecoveryDecision decision =
                recoveryEngine.decide(transaction);

        RecoveryResult result;

        if (decision.getStrategy() == RecoveryStrategy.RETRY) {
            result = executeRetry(transaction, decision);
        }
        else if (decision.getStrategy() == RecoveryStrategy.ALTERNATIVE_ROUTE) {
            result = executeAlternativeRoute(transaction, decision);
        }
        else if (decision.getStrategy() == RecoveryStrategy.ESCALATE) {
            result = new RecoveryResult(transactionId, RecoveryStrategy.ESCALATE, "ESCALATED", 0, decision.getProbability(), "Recovery escalated for review");
        }
        else {
            result = new RecoveryResult(transactionId, RecoveryStrategy.NO_ACTION, "NO_ACTION", 0, decision.getProbability(), "No recovery required");
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