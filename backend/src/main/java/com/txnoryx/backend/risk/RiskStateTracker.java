package com.txnoryx.backend.risk;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RiskStateTracker {
    private final Map<String, List<Integer>> history = new ConcurrentHashMap<>();

    public void record(String transactionId, int score) {
        history.computeIfAbsent(transactionId, k -> new ArrayList<>()).add(score);
    }

    public List<Integer> getHistory(String transactionId) {
        return history.getOrDefault(transactionId, List.of());
    }

    public int current(String transactionId) {
        List<Integer> h = history.get(transactionId);
        return h != null && !h.isEmpty() ? h.get(h.size()-1) : 0;
    }

    public boolean isEscalating(String transactionId) {
        List<Integer> h = history.get(transactionId);
        if (h == null || h.size() < 2) return false;
        return h.get(h.size()-1) > h.get(h.size()-2);
    }
}
