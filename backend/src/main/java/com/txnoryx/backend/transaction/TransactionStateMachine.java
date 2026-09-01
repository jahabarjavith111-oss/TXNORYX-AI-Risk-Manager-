package com.txnoryx.backend.transaction;

import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.model.TransactionEvent;
import com.txnoryx.backend.repository.TransactionEventRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Component
public class TransactionStateMachine {
    private final TransactionEventRepository eventRepository;

    private static final Map<TransactionState, Set<TransactionState>> ALLOWED = Map.of(
        TransactionState.INITIATED, Set.of(TransactionState.PROCESSING, TransactionState.FAILED),
        TransactionState.PROCESSING, Set.of(TransactionState.SUCCESS, TransactionState.FAILED),
        TransactionState.FAILED, Set.of(TransactionState.ANALYZING, TransactionState.BLOCKED),
        TransactionState.ANALYZING, Set.of(TransactionState.AI_DECISION),
        TransactionState.AI_DECISION, Set.of(TransactionState.RECOVERY, TransactionState.BLOCKED),
        TransactionState.RECOVERY, Set.of(TransactionState.SUCCESS, TransactionState.FAILED, TransactionState.BLOCKED),
        TransactionState.BLOCKED, Set.of(),
        TransactionState.SUCCESS, Set.of()
    );

    public TransactionStateMachine(TransactionEventRepository eventRepository) { this.eventRepository = eventRepository; }

    public boolean canTransition(TransactionState from, TransactionState to) {
        return ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }

    public void transition(Transaction tx, TransactionState from, TransactionState to, String description) {
        if (!canTransition(from, to)) throw new IllegalStateException("Illegal transition " + from + " → " + to);
        TransactionEvent e = new TransactionEvent();
        e.setTransactionId(tx.getTransactionId());
        e.setEventType(from.name() + "→" + to.name());
        e.setDescription(description);
        e.setTimestamp(LocalDateTime.now());
        eventRepository.save(e);
        tx.setStatus(to.name());
    }
}
